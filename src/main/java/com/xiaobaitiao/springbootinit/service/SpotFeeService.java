package com.xiaobaitiao.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaobaitiao.springbootinit.model.dto.spotFee.SpotFeeQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.SpotFee;
import com.xiaobaitiao.springbootinit.model.vo.SpotFeeVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 景点门票表服务
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
public interface SpotFeeService extends IService<SpotFee> {
    /**
     * 校验数据
     *
     * @param spotFee
     * @param add 对创建的数据进行校验
     */
    void validSpotFee(SpotFee spotFee, boolean add);

    /**
     * 获取查询条件
     *
     * @param spotFeeQueryRequest
     * @return
     */
    QueryWrapper<SpotFee> getQueryWrapper(SpotFeeQueryRequest spotFeeQueryRequest);
    
    /**
     * 获取景点门票表封装
     *
     * @param spotFee
     * @param request
     * @return
     */
    SpotFeeVO getSpotFeeVO(SpotFee spotFee, HttpServletRequest request);

    /**
     * 分页获取景点门票表封装
     *
     * @param spotFeePage
     * @param request
     * @return
     */
    Page<SpotFeeVO> getSpotFeeVOPage(Page<SpotFee> spotFeePage, HttpServletRequest request);

    /**
     * 根据 ID 查询门票信息并加锁
     *
     * @param id 门票 ID
     * @return 门票信息
     */
     SpotFee getByIdWithLock(Long id);
}
