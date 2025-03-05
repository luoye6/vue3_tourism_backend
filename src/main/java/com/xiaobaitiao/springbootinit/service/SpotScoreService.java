package com.xiaobaitiao.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaobaitiao.springbootinit.model.dto.spotScore.SpotScoreQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.SpotScore;
import com.xiaobaitiao.springbootinit.model.vo.SpotScoreVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 景点评分表服务
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
public interface SpotScoreService extends IService<SpotScore> {

    /**
     * 校验数据
     *
     * @param spotScore
     * @param add 对创建的数据进行校验
     */
    void validSpotScore(SpotScore spotScore, boolean add);

    /**
     * 获取查询条件
     *
     * @param spotScoreQueryRequest
     * @return
     */
    QueryWrapper<SpotScore> getQueryWrapper(SpotScoreQueryRequest spotScoreQueryRequest);
    
    /**
     * 获取景点评分表封装
     *
     * @param spotScore
     * @param request
     * @return
     */
    SpotScoreVO getSpotScoreVO(SpotScore spotScore, HttpServletRequest request);

    /**
     * 分页获取景点评分表封装
     *
     * @param spotScorePage
     * @param request
     * @return
     */
    Page<SpotScoreVO> getSpotScoreVOPage(Page<SpotScore> spotScorePage, HttpServletRequest request);

    /**
     * 获取景点的平均评分
     *
     * @param spotId 景点 ID
     * @return 平均评分
     */
    Double getAverageScoreBySpotId(Long spotId);
}
