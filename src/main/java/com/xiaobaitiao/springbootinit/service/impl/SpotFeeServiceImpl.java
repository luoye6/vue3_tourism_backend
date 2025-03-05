package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.SpotFeeMapper;
import com.xiaobaitiao.springbootinit.model.dto.spotFee.SpotFeeQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.entity.SpotFee;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.SpotFeeVO;
import com.xiaobaitiao.springbootinit.model.vo.UserVO;
import com.xiaobaitiao.springbootinit.service.SpotFeeService;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * 景点门票表服务实现
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Service
@Slf4j
public class SpotFeeServiceImpl extends ServiceImpl<SpotFeeMapper, SpotFee> implements SpotFeeService {

    @Resource
    private UserService userService;
    @Resource
    private SpotService spotService;

    @Resource
    private SpotFeeMapper spotFeeMapper;
    /**
     * 校验数据
     *
     * @param spotFee
     * @param add     对创建的数据进行校验
     */
    @Override
    public void validSpotFee(SpotFee spotFee, boolean add) {
        ThrowUtils.throwIf(spotFee == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        Long id = spotFee.getId();
        Long spotId = spotFee.getSpotId();
        Long adminId = spotFee.getAdminId();
        String spotFeeDescription = spotFee.getSpotFeeDescription();
        BigDecimal spotFeePrice = spotFee.getSpotFeePrice();
        Integer spotFeeNumber = spotFee.getSpotFeeNumber();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(spotId == null || spotId <= 0, ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(spotFeePrice == null || spotFeePrice.doubleValue() <= 0.0, ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(spotFeeNumber == null || spotFeeNumber <= 0, ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(spotFeeDescription), ErrorCode.PARAMS_ERROR);
        } else {
            ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "id 不存在");
        }

    }

    /**
     * 获取查询条件
     *
     * @param spotFeeQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<SpotFee> getQueryWrapper(SpotFeeQueryRequest spotFeeQueryRequest) {
        QueryWrapper<SpotFee> queryWrapper = new QueryWrapper<>();
        if (spotFeeQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = spotFeeQueryRequest.getId();
        Long spotId = spotFeeQueryRequest.getSpotId();
        Long adminId = spotFeeQueryRequest.getAdminId();
        String spotFeeDescription = spotFeeQueryRequest.getSpotFeeDescription();
        BigDecimal spotFeePrice = spotFeeQueryRequest.getSpotFeePrice();
        Integer spotFeeNumber = spotFeeQueryRequest.getSpotFeeNumber();
        Integer spotFeeStatus = spotFeeQueryRequest.getSpotFeeStatus();
        String sortField = spotFeeQueryRequest.getSortField();
        String sortOrder = spotFeeQueryRequest.getSortOrder();
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(spotFeeDescription), "spotFeeDescription", spotFeeDescription);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(adminId), "adminId", adminId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(spotId), "spotId", spotId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(spotFeePrice), "spotFeePrice", spotFeePrice);
        queryWrapper.eq(ObjectUtils.isNotEmpty(spotFeeNumber), "spotFeeNumber", spotFeeNumber);
        queryWrapper.eq(ObjectUtils.isNotEmpty(spotFeeStatus), "spotFeeStatus", spotFeeStatus);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取景点门票表封装
     *
     * @param spotFee
     * @param request
     * @return
     */
    @Override
    public SpotFeeVO getSpotFeeVO(SpotFee spotFee, HttpServletRequest request) {
        // 对象转封装类

        return SpotFeeVO.objToVo(spotFee);
    }

    /**
     * 分页获取景点门票表封装
     *
     * @param spotFeePage
     * @param request
     * @return
     */
    @Override
    public Page<SpotFeeVO> getSpotFeeVOPage(Page<SpotFee> spotFeePage, HttpServletRequest request) {
        List<SpotFee> spotFeeList = spotFeePage.getRecords();
        Page<SpotFeeVO> spotFeeVOPage = new Page<>(spotFeePage.getCurrent(), spotFeePage.getSize(), spotFeePage.getTotal());
        if (CollUtil.isEmpty(spotFeeList)) {
            return spotFeeVOPage;
        }

        // 获取所有景点 ID
        List<Long> spotIds = spotFeeList.stream()
                .map(SpotFee::getSpotId)
                .collect(Collectors.toList());

        // 批量查询景点信息
        Map<Long, String> spotNameMap = new HashMap<>();
        if (CollUtil.isNotEmpty(spotIds)) {
            List<Spot> spotList = spotService.listByIds(spotIds);
            spotNameMap = spotList.stream()
                    .collect(Collectors.toMap(Spot::getId, Spot::getSpotName));
        }

        // 对象列表 => 封装对象列表
        Map<Long, String> finalSpotNameMap = spotNameMap;
        List<SpotFeeVO> spotFeeVOList = spotFeeList.stream().map(spotFee -> {
            SpotFeeVO spotFeeVO = SpotFeeVO.objToVo(spotFee);
            // 设置景点名称
            spotFeeVO.setSpotName(finalSpotNameMap.get(spotFee.getSpotId()));
            return spotFeeVO;
        }).collect(Collectors.toList());

        spotFeeVOPage.setRecords(spotFeeVOList);
        return spotFeeVOPage;
    }

    @Override
    public SpotFee getByIdWithLock(Long id) {
        return spotFeeMapper.selectByIdWithLock(id);
    }

}
