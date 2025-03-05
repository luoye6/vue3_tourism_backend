package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.SpotMapper;
import com.xiaobaitiao.springbootinit.model.dto.spot.SpotQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.entity.SpotFavour;
import com.xiaobaitiao.springbootinit.model.entity.SpotThumb;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.SpotVO;
import com.xiaobaitiao.springbootinit.model.vo.UserVO;
import com.xiaobaitiao.springbootinit.service.SpotService;
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
 * 景点表服务实现
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Service
@Slf4j
public class SpotServiceImpl extends ServiceImpl<SpotMapper, Spot> implements SpotService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param spot
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validSpot(Spot spot, boolean add) {
        ThrowUtils.throwIf(spot == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String title = spot.getTitle();
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
     * @param spotQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Spot> getQueryWrapper(SpotQueryRequest spotQueryRequest) {
        QueryWrapper<Spot> queryWrapper = new QueryWrapper<>();
        if (spotQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = spotQueryRequest.getId();
        Long notId = spotQueryRequest.getNotId();
        String title = spotQueryRequest.getTitle();
        String content = spotQueryRequest.getContent();
        String searchText = spotQueryRequest.getSearchText();
        String sortField = spotQueryRequest.getSortField();
        String sortOrder = spotQueryRequest.getSortOrder();
        List<String> tagList = spotQueryRequest.getTags();
        Long userId = spotQueryRequest.getUserId();
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
     * 获取景点表封装
     *
     * @param spot
     * @param request
     * @return
     */
    @Override
    public SpotVO getSpotVO(Spot spot, HttpServletRequest request) {
        // 对象转封装类
        SpotVO spotVO = SpotVO.objToVo(spot);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = spot.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        spotVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        long spotId = spot.getId();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            // 获取点赞
            QueryWrapper<SpotThumb> spotThumbQueryWrapper = new QueryWrapper<>();
            spotThumbQueryWrapper.in("spotId", spotId);
            spotThumbQueryWrapper.eq("userId", loginUser.getId());
            SpotThumb spotThumb = spotThumbMapper.selectOne(spotThumbQueryWrapper);
            spotVO.setHasThumb(spotThumb != null);
            // 获取收藏
            QueryWrapper<SpotFavour> spotFavourQueryWrapper = new QueryWrapper<>();
            spotFavourQueryWrapper.in("spotId", spotId);
            spotFavourQueryWrapper.eq("userId", loginUser.getId());
            SpotFavour spotFavour = spotFavourMapper.selectOne(spotFavourQueryWrapper);
            spotVO.setHasFavour(spotFavour != null);
        }
        // endregion

        return spotVO;
    }

    /**
     * 分页获取景点表封装
     *
     * @param spotPage
     * @param request
     * @return
     */
    @Override
    public Page<SpotVO> getSpotVOPage(Page<Spot> spotPage, HttpServletRequest request) {
        List<Spot> spotList = spotPage.getRecords();
        Page<SpotVO> spotVOPage = new Page<>(spotPage.getCurrent(), spotPage.getSize(), spotPage.getTotal());
        if (CollUtil.isEmpty(spotList)) {
            return spotVOPage;
        }
        // 对象列表 => 封装对象列表
        List<SpotVO> spotVOList = spotList.stream().map(spot -> {
            return SpotVO.objToVo(spot);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = spotList.stream().map(Spot::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> spotIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> spotIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> spotIdSet = spotList.stream().map(Spot::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);
            // 获取点赞
            QueryWrapper<SpotThumb> spotThumbQueryWrapper = new QueryWrapper<>();
            spotThumbQueryWrapper.in("spotId", spotIdSet);
            spotThumbQueryWrapper.eq("userId", loginUser.getId());
            List<SpotThumb> spotSpotThumbList = spotThumbMapper.selectList(spotThumbQueryWrapper);
            spotSpotThumbList.forEach(spotSpotThumb -> spotIdHasThumbMap.put(spotSpotThumb.getSpotId(), true));
            // 获取收藏
            QueryWrapper<SpotFavour> spotFavourQueryWrapper = new QueryWrapper<>();
            spotFavourQueryWrapper.in("spotId", spotIdSet);
            spotFavourQueryWrapper.eq("userId", loginUser.getId());
            List<SpotFavour> spotFavourList = spotFavourMapper.selectList(spotFavourQueryWrapper);
            spotFavourList.forEach(spotFavour -> spotIdHasFavourMap.put(spotFavour.getSpotId(), true));
        }
        // 填充信息
        spotVOList.forEach(spotVO -> {
            Long userId = spotVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            spotVO.setUser(userService.getUserVO(user));
            spotVO.setHasThumb(spotIdHasThumbMap.getOrDefault(spotVO.getId(), false));
            spotVO.setHasFavour(spotIdHasFavourMap.getOrDefault(spotVO.getId(), false));
        });
        // endregion

        spotVOPage.setRecords(spotVOList);
        return spotVOPage;
    }

}
