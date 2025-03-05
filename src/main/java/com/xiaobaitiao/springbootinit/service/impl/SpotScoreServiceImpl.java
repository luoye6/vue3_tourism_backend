package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.constant.SpotScoreConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.SpotScoreMapper;
import com.xiaobaitiao.springbootinit.model.dto.spotScore.SpotScoreQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.SpotScore;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.SpotScoreVO;
import com.xiaobaitiao.springbootinit.service.SpotScoreService;
import com.xiaobaitiao.springbootinit.service.UserService;
import com.xiaobaitiao.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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
    @Resource
    private SpotScoreMapper spotScoreMapper;

    @Override
    public Double getAverageScoreBySpotId(Long spotId) {
        return spotScoreMapper.getAverageScoreBySpotId(spotId);
    }
    /**
     * 校验数据
     *
     * @param spotScore
     * @param add       对创建的数据进行校验
     */
    @Override
    public void validSpotScore(SpotScore spotScore, boolean add) {
        ThrowUtils.throwIf(spotScore == null, ErrorCode.PARAMS_ERROR);
        Long spotId = spotScore.getSpotId();
        Integer score = spotScore.getScore();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(spotId == null || spotId <= 0, ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(!SpotScoreConstant.SPOT_SCORE.contains(score.toString()), ErrorCode.PARAMS_ERROR);
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
        Long id = spotScoreQueryRequest.getId();
        Long spotId = spotScoreQueryRequest.getSpotId();
        Long userId = spotScoreQueryRequest.getUserId();
        Integer score = spotScoreQueryRequest.getScore();
        String sortField = spotScoreQueryRequest.getSortField();
        String sortOrder = spotScoreQueryRequest.getSortOrder();
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(spotId), "spotId", spotId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(score), "score", score);
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
        return SpotScoreVO.objToVo(spotScore);
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
        List<SpotScoreVO> spotScoreVOList = spotScoreList.stream().map(SpotScoreVO::objToVo).collect(Collectors.toList());

        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = spotScoreList.stream().map(SpotScore::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        spotScoreVOList.forEach(spotScoreVO -> {
            Long userId = spotScoreVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            spotScoreVO.setUserVO(userService.getUserVO(user));
        });
        // endregion

        spotScoreVOPage.setRecords(spotScoreVOList);
        return spotScoreVOPage;
    }

}
