package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.SpotScoreMapper;
import com.xiaobaitiao.springbootinit.model.dto.spotScore.SpotScoreQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.SpotScore;
import com.xiaobaitiao.springbootinit.model.entity.SpotScoreFavour;
import com.xiaobaitiao.springbootinit.model.entity.SpotScoreThumb;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.SpotScoreVO;
import com.xiaobaitiao.springbootinit.model.vo.UserVO;
import com.xiaobaitiao.springbootinit.service.SpotScoreService;
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
 * 景点评分表服务实现
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Service
@Slf4j
public class SpotScoreServiceImpl extends ServiceImpl<SpotScoreMapper, SpotScore> implements SpotScoreService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param spotScore
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validSpotScore(SpotScore spotScore, boolean add) {
        ThrowUtils.throwIf(spotScore == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String title = spotScore.getTitle();
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
     * @param spotScoreQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<SpotScore> getQueryWrapper(SpotScoreQueryRequest spotScoreQueryRequest) {
        QueryWrapper<SpotScore> queryWrapper = new QueryWrapper<>();
        if (spotScoreQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = spotScoreQueryRequest.getId();
        Long notId = spotScoreQueryRequest.getNotId();
        String title = spotScoreQueryRequest.getTitle();
        String content = spotScoreQueryRequest.getContent();
        String searchText = spotScoreQueryRequest.getSearchText();
        String sortField = spotScoreQueryRequest.getSortField();
        String sortOrder = spotScoreQueryRequest.getSortOrder();
        List<String> tagList = spotScoreQueryRequest.getTags();
        Long userId = spotScoreQueryRequest.getUserId();
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
     * 获取景点评分表封装
     *
     * @param spotScore
     * @param request
     * @return
     */
    @Override
    public SpotScoreVO getSpotScoreVO(SpotScore spotScore, HttpServletRequest request) {
        // 对象转封装类
        SpotScoreVO spotScoreVO = SpotScoreVO.objToVo(spotScore);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = spotScore.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        spotScoreVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        long spotScoreId = spotScore.getId();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            // 获取点赞
            QueryWrapper<SpotScoreThumb> spotScoreThumbQueryWrapper = new QueryWrapper<>();
            spotScoreThumbQueryWrapper.in("spotScoreId", spotScoreId);
            spotScoreThumbQueryWrapper.eq("userId", loginUser.getId());
            SpotScoreThumb spotScoreThumb = spotScoreThumbMapper.selectOne(spotScoreThumbQueryWrapper);
            spotScoreVO.setHasThumb(spotScoreThumb != null);
            // 获取收藏
            QueryWrapper<SpotScoreFavour> spotScoreFavourQueryWrapper = new QueryWrapper<>();
            spotScoreFavourQueryWrapper.in("spotScoreId", spotScoreId);
            spotScoreFavourQueryWrapper.eq("userId", loginUser.getId());
            SpotScoreFavour spotScoreFavour = spotScoreFavourMapper.selectOne(spotScoreFavourQueryWrapper);
            spotScoreVO.setHasFavour(spotScoreFavour != null);
        }
        // endregion

        return spotScoreVO;
    }

    /**
     * 分页获取景点评分表封装
     *
     * @param spotScorePage
     * @param request
     * @return
     */
    @Override
    public Page<SpotScoreVO> getSpotScoreVOPage(Page<SpotScore> spotScorePage, HttpServletRequest request) {
        List<SpotScore> spotScoreList = spotScorePage.getRecords();
        Page<SpotScoreVO> spotScoreVOPage = new Page<>(spotScorePage.getCurrent(), spotScorePage.getSize(), spotScorePage.getTotal());
        if (CollUtil.isEmpty(spotScoreList)) {
            return spotScoreVOPage;
        }
        // 对象列表 => 封装对象列表
        List<SpotScoreVO> spotScoreVOList = spotScoreList.stream().map(spotScore -> {
            return SpotScoreVO.objToVo(spotScore);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = spotScoreList.stream().map(SpotScore::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> spotScoreIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> spotScoreIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> spotScoreIdSet = spotScoreList.stream().map(SpotScore::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);
            // 获取点赞
            QueryWrapper<SpotScoreThumb> spotScoreThumbQueryWrapper = new QueryWrapper<>();
            spotScoreThumbQueryWrapper.in("spotScoreId", spotScoreIdSet);
            spotScoreThumbQueryWrapper.eq("userId", loginUser.getId());
            List<SpotScoreThumb> spotScoreSpotScoreThumbList = spotScoreThumbMapper.selectList(spotScoreThumbQueryWrapper);
            spotScoreSpotScoreThumbList.forEach(spotScoreSpotScoreThumb -> spotScoreIdHasThumbMap.put(spotScoreSpotScoreThumb.getSpotScoreId(), true));
            // 获取收藏
            QueryWrapper<SpotScoreFavour> spotScoreFavourQueryWrapper = new QueryWrapper<>();
            spotScoreFavourQueryWrapper.in("spotScoreId", spotScoreIdSet);
            spotScoreFavourQueryWrapper.eq("userId", loginUser.getId());
            List<SpotScoreFavour> spotScoreFavourList = spotScoreFavourMapper.selectList(spotScoreFavourQueryWrapper);
            spotScoreFavourList.forEach(spotScoreFavour -> spotScoreIdHasFavourMap.put(spotScoreFavour.getSpotScoreId(), true));
        }
        // 填充信息
        spotScoreVOList.forEach(spotScoreVO -> {
            Long userId = spotScoreVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            spotScoreVO.setUser(userService.getUserVO(user));
            spotScoreVO.setHasThumb(spotScoreIdHasThumbMap.getOrDefault(spotScoreVO.getId(), false));
            spotScoreVO.setHasFavour(spotScoreIdHasFavourMap.getOrDefault(spotScoreVO.getId(), false));
        });
        // endregion

        spotScoreVOPage.setRecords(spotScoreVOList);
        return spotScoreVOPage;
    }

}
