package com.xiaobaitiao.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaobaitiao.springbootinit.model.dto.spotRoute.SpotRouteQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.SpotRoute;
import com.xiaobaitiao.springbootinit.model.vo.SpotRouteVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 景点路线表服务
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
public interface SpotRouteService extends IService<SpotRoute> {

    /**
     * 校验数据
     *
     * @param spotRoute
     * @param add 对创建的数据进行校验
     */
    void validSpotRoute(SpotRoute spotRoute, boolean add);

    /**
     * 获取查询条件
     *
     * @param spotRouteQueryRequest
     * @return
     */
    QueryWrapper<SpotRoute> getQueryWrapper(SpotRouteQueryRequest spotRouteQueryRequest);
    
    /**
     * 获取景点路线表封装
     *
     * @param spotRoute
     * @param request
     * @return
     */
    SpotRouteVO getSpotRouteVO(SpotRoute spotRoute, HttpServletRequest request);

    /**
     * 分页获取景点路线表封装
     *
     * @param spotRoutePage
     * @param request
     * @return
     */
    Page<SpotRouteVO> getSpotRouteVOPage(Page<SpotRoute> spotRoutePage, HttpServletRequest request);
}
