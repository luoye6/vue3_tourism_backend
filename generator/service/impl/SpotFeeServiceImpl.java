package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.SpotFeeMapper;
import com.xiaobaitiao.springbootinit.model.dto.spotFee.SpotFeeQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.SpotFee;
import com.xiaobaitiao.springbootinit.model.entity.SpotFeeFavour;
import com.xiaobaitiao.springbootinit.model.entity.SpotFeeThumb;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.SpotFeeVO;
import com.xiaobaitiao.springbootinit.model.vo.UserVO;
import com.xiaobaitiao.springbootinit.service.SpotFeeService;
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
 * 景点门票表服务实现
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Service
@Slf4j
public class SpotFeeServiceImpl extends ServiceImpl<SpotFeeMapper, SpotFee> implements SpotFeeService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param spotFee
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validSpotFee(SpotFee spotFee, boolean add) {
        ThrowUtils.throwIf(spotFee == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String title = spotFee.getTitle();
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
     * @param spotFeeQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<SpotFee> getQueryWrapper(SpotFeeQueryRequest spotFeeQueryRequest) {
        QueryWrapper<SpotFee> queryWrapper = new QueryWrapper<>();
        if (spotFeeQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = spotFeeQueryRequest.getId();
        Long notId = spotFeeQueryRequest.getNotId();
        String title = spotFeeQueryRequest.getTitle();
        String content = spotFeeQueryRequest.getContent();
        String searchText = spotFeeQueryRequest.getSearchText();
        String sortField = spotFeeQueryRequest.getSortField();
        String sortOrder = spotFeeQueryRequest.getSortOrder();
        List<String> tagList = spotFeeQueryRequest.getTags();
        Long userId = spotFeeQueryRequest.getUserId();
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
     * 获取景点门票表封装
     *
     * @param spotFee
     * @param request
     * @return
     */
    @Override
    public SpotFeeVO getSpotFeeVO(SpotFee spotFee, HttpServletRequest request) {
        // 对象转封装类
        SpotFeeVO spotFeeVO = SpotFeeVO.objToVo(spotFee);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = spotFee.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        spotFeeVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        long spotFeeId = spotFee.getId();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            // 获取点赞
            QueryWrapper<SpotFeeThumb> spotFeeThumbQueryWrapper = new QueryWrapper<>();
            spotFeeThumbQueryWrapper.in("spotFeeId", spotFeeId);
            spotFeeThumbQueryWrapper.eq("userId", loginUser.getId());
            SpotFeeThumb spotFeeThumb = spotFeeThumbMapper.selectOne(spotFeeThumbQueryWrapper);
            spotFeeVO.setHasThumb(spotFeeThumb != null);
            // 获取收藏
            QueryWrapper<SpotFeeFavour> spotFeeFavourQueryWrapper = new QueryWrapper<>();
            spotFeeFavourQueryWrapper.in("spotFeeId", spotFeeId);
            spotFeeFavourQueryWrapper.eq("userId", loginUser.getId());
            SpotFeeFavour spotFeeFavour = spotFeeFavourMapper.selectOne(spotFeeFavourQueryWrapper);
            spotFeeVO.setHasFavour(spotFeeFavour != null);
        }
        // endregion

        return spotFeeVO;
    }

    /**
     * 分页获取景点门票表封装
     *
     * @param spotFeePage
     * @param request
     * @return
     */
    @Override
    public Page<SpotFeeVO> getSpotFeeVOPage(Page<SpotFee> spotFeePage, HttpServletRequest request) {
        List<SpotFee> spotFeeList = spotFeePage.getRecords();
        Page<SpotFeeVO> spotFeeVOPage = new Page<>(spotFeePage.getCurrent(), spotFeePage.getSize(), spotFeePage.getTotal());
        if (CollUtil.isEmpty(spotFeeList)) {
            return spotFeeVOPage;
        }
        // 对象列表 => 封装对象列表
        List<SpotFeeVO> spotFeeVOList = spotFeeList.stream().map(spotFee -> {
            return SpotFeeVO.objToVo(spotFee);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = spotFeeList.stream().map(SpotFee::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> spotFeeIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> spotFeeIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> spotFeeIdSet = spotFeeList.stream().map(SpotFee::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);
            // 获取点赞
            QueryWrapper<SpotFeeThumb> spotFeeThumbQueryWrapper = new QueryWrapper<>();
            spotFeeThumbQueryWrapper.in("spotFeeId", spotFeeIdSet);
            spotFeeThumbQueryWrapper.eq("userId", loginUser.getId());
            List<SpotFeeThumb> spotFeeSpotFeeThumbList = spotFeeThumbMapper.selectList(spotFeeThumbQueryWrapper);
            spotFeeSpotFeeThumbList.forEach(spotFeeSpotFeeThumb -> spotFeeIdHasThumbMap.put(spotFeeSpotFeeThumb.getSpotFeeId(), true));
            // 获取收藏
            QueryWrapper<SpotFeeFavour> spotFeeFavourQueryWrapper = new QueryWrapper<>();
            spotFeeFavourQueryWrapper.in("spotFeeId", spotFeeIdSet);
            spotFeeFavourQueryWrapper.eq("userId", loginUser.getId());
            List<SpotFeeFavour> spotFeeFavourList = spotFeeFavourMapper.selectList(spotFeeFavourQueryWrapper);
            spotFeeFavourList.forEach(spotFeeFavour -> spotFeeIdHasFavourMap.put(spotFeeFavour.getSpotFeeId(), true));
        }
        // 填充信息
        spotFeeVOList.forEach(spotFeeVO -> {
            Long userId = spotFeeVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            spotFeeVO.setUser(userService.getUserVO(user));
            spotFeeVO.setHasThumb(spotFeeIdHasThumbMap.getOrDefault(spotFeeVO.getId(), false));
            spotFeeVO.setHasFavour(spotFeeIdHasFavourMap.getOrDefault(spotFeeVO.getId(), false));
        });
        // endregion

        spotFeeVOPage.setRecords(spotFeeVOList);
        return spotFeeVOPage;
    }

}
