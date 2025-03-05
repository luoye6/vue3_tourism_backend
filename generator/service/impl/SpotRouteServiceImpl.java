package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.SpotRouteMapper;
import com.xiaobaitiao.springbootinit.model.dto.spotRoute.SpotRouteQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.SpotRoute;
import com.xiaobaitiao.springbootinit.model.entity.SpotRouteFavour;
import com.xiaobaitiao.springbootinit.model.entity.SpotRouteThumb;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.SpotRouteVO;
import com.xiaobaitiao.springbootinit.model.vo.UserVO;
import com.xiaobaitiao.springbootinit.service.SpotRouteService;
import com.xiaobaitiao.springbootinit.service.UserService;
import com.xiaobaitiao.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 景点路线表服务实现
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Service
@Slf4j
public class SpotRouteServiceImpl extends ServiceImpl<SpotRouteMapper, SpotRoute> implements SpotRouteService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param spotRoute
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validSpotRoute(SpotRoute spotRoute, boolean add) {
        ThrowUtils.throwIf(spotRoute == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String title = spotRoute.getTitle();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        // todo 补充校验规则
        if (StringUtils.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }
    }

    /**
     * 获取查询条件
     *
     * @param spotRouteQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<SpotRoute> getQueryWrapper(SpotRouteQueryRequest spotRouteQueryRequest) {
        QueryWrapper<SpotRoute> queryWrapper = new QueryWrapper<>();
        if (spotRouteQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = spotRouteQueryRequest.getId();
        Long notId = spotRouteQueryRequest.getNotId();
        String title = spotRouteQueryRequest.getTitle();
        String content = spotRouteQueryRequest.getContent();
        String searchText = spotRouteQueryRequest.getSearchText();
        String sortField = spotRouteQueryRequest.getSortField();
        String sortOrder = spotRouteQueryRequest.getSortOrder();
        List<String> tagList = spotRouteQueryRequest.getTags();
        Long userId = spotRouteQueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取景点路线表封装
     *
     * @param spotRoute
     * @param request
     * @return
     */
    @Override
    public SpotRouteVO getSpotRouteVO(SpotRoute spotRoute, HttpServletRequest request) {
        // 对象转封装类
        SpotRouteVO spotRouteVO = SpotRouteVO.objToVo(spotRoute);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = spotRoute.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        spotRouteVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        long spotRouteId = spotRoute.getId();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            // 获取点赞
            QueryWrapper<SpotRouteThumb> spotRouteThumbQueryWrapper = new QueryWrapper<>();
            spotRouteThumbQueryWrapper.in("spotRouteId", spotRouteId);
            spotRouteThumbQueryWrapper.eq("userId", loginUser.getId());
            SpotRouteThumb spotRouteThumb = spotRouteThumbMapper.selectOne(spotRouteThumbQueryWrapper);
            spotRouteVO.setHasThumb(spotRouteThumb != null);
            // 获取收藏
            QueryWrapper<SpotRouteFavour> spotRouteFavourQueryWrapper = new QueryWrapper<>();
            spotRouteFavourQueryWrapper.in("spotRouteId", spotRouteId);
            spotRouteFavourQueryWrapper.eq("userId", loginUser.getId());
            SpotRouteFavour spotRouteFavour = spotRouteFavourMapper.selectOne(spotRouteFavourQueryWrapper);
            spotRouteVO.setHasFavour(spotRouteFavour != null);
        }
        // endregion

        return spotRouteVO;
    }

    /**
     * 分页获取景点路线表封装
     *
     * @param spotRoutePage
     * @param request
     * @return
     */
    @Override
    public Page<SpotRouteVO> getSpotRouteVOPage(Page<SpotRoute> spotRoutePage, HttpServletRequest request) {
        List<SpotRoute> spotRouteList = spotRoutePage.getRecords();
        Page<SpotRouteVO> spotRouteVOPage = new Page<>(spotRoutePage.getCurrent(), spotRoutePage.getSize(), spotRoutePage.getTotal());
        if (CollUtil.isEmpty(spotRouteList)) {
            return spotRouteVOPage;
        }
        // 对象列表 => 封装对象列表
        List<SpotRouteVO> spotRouteVOList = spotRouteList.stream().map(spotRoute -> {
            return SpotRouteVO.objToVo(spotRoute);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = spotRouteList.stream().map(SpotRoute::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> spotRouteIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> spotRouteIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> spotRouteIdSet = spotRouteList.stream().map(SpotRoute::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);
            // 获取点赞
            QueryWrapper<SpotRouteThumb> spotRouteThumbQueryWrapper = new QueryWrapper<>();
            spotRouteThumbQueryWrapper.in("spotRouteId", spotRouteIdSet);
            spotRouteThumbQueryWrapper.eq("userId", loginUser.getId());
            List<SpotRouteThumb> spotRouteSpotRouteThumbList = spotRouteThumbMapper.selectList(spotRouteThumbQueryWrapper);
            spotRouteSpotRouteThumbList.forEach(spotRouteSpotRouteThumb -> spotRouteIdHasThumbMap.put(spotRouteSpotRouteThumb.getSpotRouteId(), true));
            // 获取收藏
            QueryWrapper<SpotRouteFavour> spotRouteFavourQueryWrapper = new QueryWrapper<>();
            spotRouteFavourQueryWrapper.in("spotRouteId", spotRouteIdSet);
            spotRouteFavourQueryWrapper.eq("userId", loginUser.getId());
            List<SpotRouteFavour> spotRouteFavourList = spotRouteFavourMapper.selectList(spotRouteFavourQueryWrapper);
            spotRouteFavourList.forEach(spotRouteFavour -> spotRouteIdHasFavourMap.put(spotRouteFavour.getSpotRouteId(), true));
        }
        // 填充信息
        spotRouteVOList.forEach(spotRouteVO -> {
            Long userId = spotRouteVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            spotRouteVO.setUser(userService.getUserVO(user));
            spotRouteVO.setHasThumb(spotRouteIdHasThumbMap.getOrDefault(spotRouteVO.getId(), false));
            spotRouteVO.setHasFavour(spotRouteIdHasFavourMap.getOrDefault(spotRouteVO.getId(), false));
        });
        // endregion

        spotRouteVOPage.setRecords(spotRouteVOList);
        return spotRouteVOPage;
    }

}
