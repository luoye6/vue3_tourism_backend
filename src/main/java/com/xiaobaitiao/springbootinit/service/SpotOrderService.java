package com.xiaobaitiao.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaobaitiao.springbootinit.model.dto.spotOrder.SpotOrderQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.SpotOrder;
import com.xiaobaitiao.springbootinit.model.vo.SpotOrderVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 景点订单表服务
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
public interface SpotOrderService extends IService<SpotOrder> {

    /**
     * 校验数据
     *
     * @param spotOrder
     * @param add 对创建的数据进行校验
     */
    void validSpotOrder(SpotOrder spotOrder, boolean add);

    /**
     * 获取查询条件
     *
     * @param spotOrderQueryRequest
     * @return
     */
    QueryWrapper<SpotOrder> getQueryWrapper(SpotOrderQueryRequest spotOrderQueryRequest);
    
    /**
     * 获取景点订单表封装
     *
     * @param spotOrder
     * @param request
     * @return
     */
    SpotOrderVO getSpotOrderVO(SpotOrder spotOrder, HttpServletRequest request);

    /**
     * 分页获取景点订单表封装
     *
     * @param spotOrderPage
     * @param request
     * @return
     */
    Page<SpotOrderVO> getSpotOrderVOPage(Page<SpotOrder> spotOrderPage, HttpServletRequest request);

    /**
     * 根据查询条件获取订单列表
     *
     * @param queryRequest 查询条件
     * @return 返回订单列表
     */
    List<SpotOrder> listByQuery(SpotOrderQueryRequest queryRequest);
}
