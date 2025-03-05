package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.UserSpotFavoritesMapper;
import com.xiaobaitiao.springbootinit.model.dto.userSpotFavorites.UserSpotFavoritesQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.UserSpotFavorites;
import com.xiaobaitiao.springbootinit.model.entity.UserSpotFavoritesFavour;
import com.xiaobaitiao.springbootinit.model.entity.UserSpotFavoritesThumb;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.UserSpotFavoritesVO;
import com.xiaobaitiao.springbootinit.model.vo.UserVO;
import com.xiaobaitiao.springbootinit.service.UserSpotFavoritesService;
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
 * 用户景点收藏关联表服务实现
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Service
@Slf4j
public class UserSpotFavoritesServiceImpl extends ServiceImpl<UserSpotFavoritesMapper, UserSpotFavorites> implements UserSpotFavoritesService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param userSpotFavorites
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validUserSpotFavorites(UserSpotFavorites userSpotFavorites, boolean add) {
        ThrowUtils.throwIf(userSpotFavorites == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String title = userSpotFavorites.getTitle();
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
     * @param userSpotFavoritesQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<UserSpotFavorites> getQueryWrapper(UserSpotFavoritesQueryRequest userSpotFavoritesQueryRequest) {
        QueryWrapper<UserSpotFavorites> queryWrapper = new QueryWrapper<>();
        if (userSpotFavoritesQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = userSpotFavoritesQueryRequest.getId();
        Long notId = userSpotFavoritesQueryRequest.getNotId();
        String title = userSpotFavoritesQueryRequest.getTitle();
        String content = userSpotFavoritesQueryRequest.getContent();
        String searchText = userSpotFavoritesQueryRequest.getSearchText();
        String sortField = userSpotFavoritesQueryRequest.getSortField();
        String sortOrder = userSpotFavoritesQueryRequest.getSortOrder();
        List<String> tagList = userSpotFavoritesQueryRequest.getTags();
        Long userId = userSpotFavoritesQueryRequest.getUserId();
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
     * 获取用户景点收藏关联表封装
     *
     * @param userSpotFavorites
     * @param request
     * @return
     */
    @Override
    public UserSpotFavoritesVO getUserSpotFavoritesVO(UserSpotFavorites userSpotFavorites, HttpServletRequest request) {
        // 对象转封装类
        UserSpotFavoritesVO userSpotFavoritesVO = UserSpotFavoritesVO.objToVo(userSpotFavorites);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = userSpotFavorites.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        userSpotFavoritesVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        long userSpotFavoritesId = userSpotFavorites.getId();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            // 获取点赞
            QueryWrapper<UserSpotFavoritesThumb> userSpotFavoritesThumbQueryWrapper = new QueryWrapper<>();
            userSpotFavoritesThumbQueryWrapper.in("userSpotFavoritesId", userSpotFavoritesId);
            userSpotFavoritesThumbQueryWrapper.eq("userId", loginUser.getId());
            UserSpotFavoritesThumb userSpotFavoritesThumb = userSpotFavoritesThumbMapper.selectOne(userSpotFavoritesThumbQueryWrapper);
            userSpotFavoritesVO.setHasThumb(userSpotFavoritesThumb != null);
            // 获取收藏
            QueryWrapper<UserSpotFavoritesFavour> userSpotFavoritesFavourQueryWrapper = new QueryWrapper<>();
            userSpotFavoritesFavourQueryWrapper.in("userSpotFavoritesId", userSpotFavoritesId);
            userSpotFavoritesFavourQueryWrapper.eq("userId", loginUser.getId());
            UserSpotFavoritesFavour userSpotFavoritesFavour = userSpotFavoritesFavourMapper.selectOne(userSpotFavoritesFavourQueryWrapper);
            userSpotFavoritesVO.setHasFavour(userSpotFavoritesFavour != null);
        }
        // endregion

        return userSpotFavoritesVO;
    }

    /**
     * 分页获取用户景点收藏关联表封装
     *
     * @param userSpotFavoritesPage
     * @param request
     * @return
     */
    @Override
    public Page<UserSpotFavoritesVO> getUserSpotFavoritesVOPage(Page<UserSpotFavorites> userSpotFavoritesPage, HttpServletRequest request) {
        List<UserSpotFavorites> userSpotFavoritesList = userSpotFavoritesPage.getRecords();
        Page<UserSpotFavoritesVO> userSpotFavoritesVOPage = new Page<>(userSpotFavoritesPage.getCurrent(), userSpotFavoritesPage.getSize(), userSpotFavoritesPage.getTotal());
        if (CollUtil.isEmpty(userSpotFavoritesList)) {
            return userSpotFavoritesVOPage;
        }
        // 对象列表 => 封装对象列表
        List<UserSpotFavoritesVO> userSpotFavoritesVOList = userSpotFavoritesList.stream().map(userSpotFavorites -> {
            return UserSpotFavoritesVO.objToVo(userSpotFavorites);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = userSpotFavoritesList.stream().map(UserSpotFavorites::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> userSpotFavoritesIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> userSpotFavoritesIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> userSpotFavoritesIdSet = userSpotFavoritesList.stream().map(UserSpotFavorites::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);
            // 获取点赞
            QueryWrapper<UserSpotFavoritesThumb> userSpotFavoritesThumbQueryWrapper = new QueryWrapper<>();
            userSpotFavoritesThumbQueryWrapper.in("userSpotFavoritesId", userSpotFavoritesIdSet);
            userSpotFavoritesThumbQueryWrapper.eq("userId", loginUser.getId());
            List<UserSpotFavoritesThumb> userSpotFavoritesUserSpotFavoritesThumbList = userSpotFavoritesThumbMapper.selectList(userSpotFavoritesThumbQueryWrapper);
            userSpotFavoritesUserSpotFavoritesThumbList.forEach(userSpotFavoritesUserSpotFavoritesThumb -> userSpotFavoritesIdHasThumbMap.put(userSpotFavoritesUserSpotFavoritesThumb.getUserSpotFavoritesId(), true));
            // 获取收藏
            QueryWrapper<UserSpotFavoritesFavour> userSpotFavoritesFavourQueryWrapper = new QueryWrapper<>();
            userSpotFavoritesFavourQueryWrapper.in("userSpotFavoritesId", userSpotFavoritesIdSet);
            userSpotFavoritesFavourQueryWrapper.eq("userId", loginUser.getId());
            List<UserSpotFavoritesFavour> userSpotFavoritesFavourList = userSpotFavoritesFavourMapper.selectList(userSpotFavoritesFavourQueryWrapper);
            userSpotFavoritesFavourList.forEach(userSpotFavoritesFavour -> userSpotFavoritesIdHasFavourMap.put(userSpotFavoritesFavour.getUserSpotFavoritesId(), true));
        }
        // 填充信息
        userSpotFavoritesVOList.forEach(userSpotFavoritesVO -> {
            Long userId = userSpotFavoritesVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            userSpotFavoritesVO.setUser(userService.getUserVO(user));
            userSpotFavoritesVO.setHasThumb(userSpotFavoritesIdHasThumbMap.getOrDefault(userSpotFavoritesVO.getId(), false));
            userSpotFavoritesVO.setHasFavour(userSpotFavoritesIdHasFavourMap.getOrDefault(userSpotFavoritesVO.getId(), false));
        });
        // endregion

        userSpotFavoritesVOPage.setRecords(userSpotFavoritesVOList);
        return userSpotFavoritesVOPage;
    }

}
