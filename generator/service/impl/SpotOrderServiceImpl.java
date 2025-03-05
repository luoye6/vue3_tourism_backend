package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.SpotOrderMapper;
import com.xiaobaitiao.springbootinit.model.dto.spotOrder.SpotOrderQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.SpotOrder;
import com.xiaobaitiao.springbootinit.model.entity.SpotOrderFavour;
import com.xiaobaitiao.springbootinit.model.entity.SpotOrderThumb;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.SpotOrderVO;
import com.xiaobaitiao.springbootinit.model.vo.UserVO;
import com.xiaobaitiao.springbootinit.service.SpotOrderService;
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
 * 景点订单表服务实现
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Service
@Slf4j
public class SpotOrderServiceImpl extends ServiceImpl<SpotOrderMapper, SpotOrder> implements SpotOrderService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param spotOrder
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validSpotOrder(SpotOrder spotOrder, boolean add) {
        ThrowUtils.throwIf(spotOrder == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String title = spotOrder.getTitle();
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
     * @param spotOrderQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<SpotOrder> getQueryWrapper(SpotOrderQueryRequest spotOrderQueryRequest) {
        QueryWrapper<SpotOrder> queryWrapper = new QueryWrapper<>();
        if (spotOrderQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = spotOrderQueryRequest.getId();
        Long notId = spotOrderQueryRequest.getNotId();
        String title = spotOrderQueryRequest.getTitle();
        String content = spotOrderQueryRequest.getContent();
        String searchText = spotOrderQueryRequest.getSearchText();
        String sortField = spotOrderQueryRequest.getSortField();
        String sortOrder = spotOrderQueryRequest.getSortOrder();
        List<String> tagList = spotOrderQueryRequest.getTags();
        Long userId = spotOrderQueryRequest.getUserId();
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
     * 获取景点订单表封装
     *
     * @param spotOrder
     * @param request
     * @return
     */
    @Override
    public SpotOrderVO getSpotOrderVO(SpotOrder spotOrder, HttpServletRequest request) {
        // 对象转封装类
        SpotOrderVO spotOrderVO = SpotOrderVO.objToVo(spotOrder);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = spotOrder.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        spotOrderVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        long spotOrderId = spotOrder.getId();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            // 获取点赞
            QueryWrapper<SpotOrderThumb> spotOrderThumbQueryWrapper = new QueryWrapper<>();
            spotOrderThumbQueryWrapper.in("spotOrderId", spotOrderId);
            spotOrderThumbQueryWrapper.eq("userId", loginUser.getId());
            SpotOrderThumb spotOrderThumb = spotOrderThumbMapper.selectOne(spotOrderThumbQueryWrapper);
            spotOrderVO.setHasThumb(spotOrderThumb != null);
            // 获取收藏
            QueryWrapper<SpotOrderFavour> spotOrderFavourQueryWrapper = new QueryWrapper<>();
            spotOrderFavourQueryWrapper.in("spotOrderId", spotOrderId);
            spotOrderFavourQueryWrapper.eq("userId", loginUser.getId());
            SpotOrderFavour spotOrderFavour = spotOrderFavourMapper.selectOne(spotOrderFavourQueryWrapper);
            spotOrderVO.setHasFavour(spotOrderFavour != null);
        }
        // endregion

        return spotOrderVO;
    }

    /**
     * 分页获取景点订单表封装
     *
     * @param spotOrderPage
     * @param request
     * @return
     */
    @Override
    public Page<SpotOrderVO> getSpotOrderVOPage(Page<SpotOrder> spotOrderPage, HttpServletRequest request) {
        List<SpotOrder> spotOrderList = spotOrderPage.getRecords();
        Page<SpotOrderVO> spotOrderVOPage = new Page<>(spotOrderPage.getCurrent(), spotOrderPage.getSize(), spotOrderPage.getTotal());
        if (CollUtil.isEmpty(spotOrderList)) {
            return spotOrderVOPage;
        }
        // 对象列表 => 封装对象列表
        List<SpotOrderVO> spotOrderVOList = spotOrderList.stream().map(spotOrder -> {
            return SpotOrderVO.objToVo(spotOrder);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = spotOrderList.stream().map(SpotOrder::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> spotOrderIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> spotOrderIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> spotOrderIdSet = spotOrderList.stream().map(SpotOrder::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);
            // 获取点赞
            QueryWrapper<SpotOrderThumb> spotOrderThumbQueryWrapper = new QueryWrapper<>();
            spotOrderThumbQueryWrapper.in("spotOrderId", spotOrderIdSet);
            spotOrderThumbQueryWrapper.eq("userId", loginUser.getId());
            List<SpotOrderThumb> spotOrderSpotOrderThumbList = spotOrderThumbMapper.selectList(spotOrderThumbQueryWrapper);
            spotOrderSpotOrderThumbList.forEach(spotOrderSpotOrderThumb -> spotOrderIdHasThumbMap.put(spotOrderSpotOrderThumb.getSpotOrderId(), true));
            // 获取收藏
            QueryWrapper<SpotOrderFavour> spotOrderFavourQueryWrapper = new QueryWrapper<>();
            spotOrderFavourQueryWrapper.in("spotOrderId", spotOrderIdSet);
            spotOrderFavourQueryWrapper.eq("userId", loginUser.getId());
            List<SpotOrderFavour> spotOrderFavourList = spotOrderFavourMapper.selectList(spotOrderFavourQueryWrapper);
            spotOrderFavourList.forEach(spotOrderFavour -> spotOrderIdHasFavourMap.put(spotOrderFavour.getSpotOrderId(), true));
        }
        // 填充信息
        spotOrderVOList.forEach(spotOrderVO -> {
            Long userId = spotOrderVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            spotOrderVO.setUser(userService.getUserVO(user));
            spotOrderVO.setHasThumb(spotOrderIdHasThumbMap.getOrDefault(spotOrderVO.getId(), false));
            spotOrderVO.setHasFavour(spotOrderIdHasFavourMap.getOrDefault(spotOrderVO.getId(), false));
        });
        // endregion

        spotOrderVOPage.setRecords(spotOrderVOList);
        return spotOrderVOPage;
    }

}
