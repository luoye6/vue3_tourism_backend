package com.xiaobaitiao.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaobaitiao.springbootinit.model.dto.spot.SpotQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.vo.SpotVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 景点表服务
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
public interface SpotService extends IService<Spot> {

    /**
     * 校验数据
     *
     * @param spot
     * @param add 对创建的数据进行校验
     */
    void validSpot(Spot spot, boolean add);

    /**
     * 获取查询条件
     *
     * @param spotQueryRequest
     * @return
     */
    QueryWrapper<Spot> getQueryWrapper(SpotQueryRequest spotQueryRequest);
    
    /**
     * 获取景点表封装
     *
     * @param spot
     * @param request
     * @return
     */
    SpotVO getSpotVO(Spot spot, HttpServletRequest request);

    /**
     * 分页获取景点表封装
     *
     * @param spotPage
     * @param request
     * @return
     */
    Page<SpotVO> getSpotVOPage(Page<Spot> spotPage, HttpServletRequest request);
}
