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
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.entity.SpotFee;
import com.xiaobaitiao.springbootinit.model.entity.SpotOrder;
import com.xiaobaitiao.springbootinit.model.vo.SpotOrderVO;
import com.xiaobaitiao.springbootinit.service.SpotFeeService;
import com.xiaobaitiao.springbootinit.service.SpotOrderService;
import com.xiaobaitiao.springbootinit.service.SpotService;
import com.xiaobaitiao.springbootinit.service.UserService;
import com.xiaobaitiao.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
    private SpotFeeService spotFeeService;
    @Resource
    private SpotService spotService;

    /**
     * 校验数据
     *
     * @param spotOrder
     * @param add       对创建的数据进行校验
     */
    @Override
    public void validSpotOrder(SpotOrder spotOrder, boolean add) {
        ThrowUtils.throwIf(spotOrder == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        Long spotFeeId = spotOrder.getSpotFeeId();
        String userPhone = spotOrder.getUserPhone();
        BigDecimal paymentAmount = spotOrder.getPaymentAmount();
        Integer payStatus = spotOrder.getPayStatus();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(spotFeeId == null || spotFeeId <= 0, ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(payStatus == null, ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(paymentAmount == null || paymentAmount.doubleValue() <= 0, ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(userPhone)) {
            ThrowUtils.throwIf(userPhone.length() != 11, ErrorCode.PARAMS_ERROR, "手机号码格式不正确");
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
        Long userId = spotOrderQueryRequest.getUserId();
        Long spotFeeId = spotOrderQueryRequest.getSpotFeeId();
        String userName = spotOrderQueryRequest.getUserName();
        String userPhone = spotOrderQueryRequest.getUserPhone();
        Integer payStatus = spotOrderQueryRequest.getPayStatus();
        String sortField = spotOrderQueryRequest.getSortField();
        String sortOrder = spotOrderQueryRequest.getSortOrder();
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userPhone), "userPhone", userPhone);
        queryWrapper.eq(ObjectUtils.isNotEmpty(spotFeeId), "spotFeeId", spotFeeId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(payStatus), "payStatus", payStatus);
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

        // 获取所有的 spotFeeId
        List<Long> spotFeeIds = spotOrderList.stream()
                .map(SpotOrder::getSpotFeeId)
                .collect(Collectors.toList());

        // 根据 spotFeeId 查询 spotFee 列表
        List<SpotFee> spotFeeList = spotFeeService.listByIds(spotFeeIds);

        // 获取所有的 spotId
        List<Long> spotIds = spotFeeList.stream()
                .map(SpotFee::getSpotId)
                .collect(Collectors.toList());

        // 根据 spotId 查询 spot 列表
        Map<Long, Spot> spotMap = spotService.listByIds(spotIds).stream()
                .collect(Collectors.toMap(Spot::getId, spot -> spot));

        // 将 spotFeeId 和 spotId 的映射关系存储
        Map<Long, Long> spotFeeIdToSpotIdMap = spotFeeList.stream()
                .collect(Collectors.toMap(SpotFee::getId, SpotFee::getSpotId));

        // 对象列表 => 封装对象列表
        List<SpotOrderVO> spotOrderVOList = spotOrderList.stream().map(spotOrder -> {
            SpotOrderVO spotOrderVO = SpotOrderVO.objToVo(spotOrder);

            // 获取 spotFeeId 对应的 spotId
            Long spotId = spotFeeIdToSpotIdMap.get(spotOrder.getSpotFeeId());

            // 根据 spotId 获取景点名称
            if (spotId != null) {
                Spot spot = spotMap.get(spotId);
                if (spot != null) {
                    spotOrderVO.setSpotName(spot.getSpotName());
                }
            }

            return spotOrderVO;
        }).collect(Collectors.toList());

        spotOrderVOPage.setRecords(spotOrderVOList);
        return spotOrderVOPage;
    }

    @Override
    public List<SpotOrder> listByQuery(SpotOrderQueryRequest queryRequest) {
        QueryWrapper<SpotOrder> queryWrapper = new QueryWrapper<>();
        if (queryRequest.getUserId() != null) {
            queryWrapper.eq("userId", queryRequest.getUserId());
        }
        if (queryRequest.getPayStatus() != null) {
            queryWrapper.eq("payStatus", queryRequest.getPayStatus());
        }
        return this.list(queryWrapper);
    }

}
