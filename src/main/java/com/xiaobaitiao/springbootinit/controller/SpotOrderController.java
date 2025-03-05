package com.xiaobaitiao.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaobaitiao.springbootinit.annotation.AuthCheck;
import com.xiaobaitiao.springbootinit.common.BaseResponse;
import com.xiaobaitiao.springbootinit.common.DeleteRequest;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.common.ResultUtils;
import com.xiaobaitiao.springbootinit.constant.UserConstant;
import com.xiaobaitiao.springbootinit.exception.BusinessException;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.model.dto.spotOrder.SpotOrderAddRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotOrder.SpotOrderEditRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotOrder.SpotOrderQueryRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotOrder.SpotOrderUpdateRequest;
import com.xiaobaitiao.springbootinit.model.entity.SpotOrder;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.SpotOrderVO;
import com.xiaobaitiao.springbootinit.service.SpotFeeService;
import com.xiaobaitiao.springbootinit.service.SpotOrderService;
import com.xiaobaitiao.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 景点订单表接口
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@RestController
@RequestMapping("/spotOrder")
@Slf4j
public class SpotOrderController {

    @Resource
    private SpotOrderService spotOrderService;
    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建景点订单表
     *
     * @param spotOrderAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addSpotOrder(@RequestBody SpotOrderAddRequest spotOrderAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(spotOrderAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        SpotOrder spotOrder = new SpotOrder();
        BeanUtils.copyProperties(spotOrderAddRequest, spotOrder);
        // 数据校验
        spotOrderService.validSpotOrder(spotOrder, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        spotOrder.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = spotOrderService.save(spotOrder);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newSpotOrderId = spotOrder.getId();
        return ResultUtils.success(newSpotOrderId);
    }

    /**
     * 删除景点订单表
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteSpotOrder(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        SpotOrder oldSpotOrder = spotOrderService.getById(id);
        ThrowUtils.throwIf(oldSpotOrder == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldSpotOrder.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = spotOrderService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新景点订单表（仅管理员可用）
     *
     * @param spotOrderUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateSpotOrder(@RequestBody SpotOrderUpdateRequest spotOrderUpdateRequest) {
        if (spotOrderUpdateRequest == null || spotOrderUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        SpotOrder spotOrder = new SpotOrder();
        BeanUtils.copyProperties(spotOrderUpdateRequest, spotOrder);
        // 数据校验
        spotOrderService.validSpotOrder(spotOrder, false);
        // 判断是否存在
        long id = spotOrderUpdateRequest.getId();
        SpotOrder oldSpotOrder = spotOrderService.getById(id);
        ThrowUtils.throwIf(oldSpotOrder == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = spotOrderService.updateById(spotOrder);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取景点订单表（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<SpotOrderVO> getSpotOrderVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        SpotOrder spotOrder = spotOrderService.getById(id);
        ThrowUtils.throwIf(spotOrder == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(spotOrderService.getSpotOrderVO(spotOrder, request));
    }

    /**
     * 分页获取景点订单表列表（仅管理员可用）
     *
     * @param spotOrderQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<SpotOrder>> listSpotOrderByPage(@RequestBody SpotOrderQueryRequest spotOrderQueryRequest) {
        long current = spotOrderQueryRequest.getCurrent();
        long size = spotOrderQueryRequest.getPageSize();
        // 查询数据库
        Page<SpotOrder> spotOrderPage = spotOrderService.page(new Page<>(current, size),
                spotOrderService.getQueryWrapper(spotOrderQueryRequest));
        return ResultUtils.success(spotOrderPage);
    }

    /**
     * 分页获取景点订单表列表（封装类）
     *
     * @param spotOrderQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<SpotOrderVO>> listSpotOrderVOByPage(@RequestBody SpotOrderQueryRequest spotOrderQueryRequest,
                                                               HttpServletRequest request) {
        long current = spotOrderQueryRequest.getCurrent();
        long size = spotOrderQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<SpotOrder> spotOrderPage = spotOrderService.page(new Page<>(current, size),
                spotOrderService.getQueryWrapper(spotOrderQueryRequest));
        // 获取封装类
        return ResultUtils.success(spotOrderService.getSpotOrderVOPage(spotOrderPage, request));
    }

    /**
     * 分页获取当前登录用户创建的景点订单表列表
     *
     * @param spotOrderQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<SpotOrderVO>> listMySpotOrderVOByPage(@RequestBody SpotOrderQueryRequest spotOrderQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(spotOrderQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        spotOrderQueryRequest.setUserId(loginUser.getId());
        long current = spotOrderQueryRequest.getCurrent();
        long size = spotOrderQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<SpotOrder> spotOrderPage = spotOrderService.page(new Page<>(current, size),
                spotOrderService.getQueryWrapper(spotOrderQueryRequest));
        // 获取封装类
        return ResultUtils.success(spotOrderService.getSpotOrderVOPage(spotOrderPage, request));
    }

    /**
     * 编辑景点订单表（给用户使用）
     *
     * @param spotOrderEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editSpotOrder(@RequestBody SpotOrderEditRequest spotOrderEditRequest, HttpServletRequest request) {
        if (spotOrderEditRequest == null || spotOrderEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        SpotOrder spotOrder = new SpotOrder();
        BeanUtils.copyProperties(spotOrderEditRequest, spotOrder);
        // 数据校验
        spotOrderService.validSpotOrder(spotOrder, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = spotOrderEditRequest.getId();
        SpotOrder oldSpotOrder = spotOrderService.getById(id);
        ThrowUtils.throwIf(oldSpotOrder == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldSpotOrder.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = spotOrderService.updateById(spotOrder);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
    /**
     * 根据 userId 和 payStatus 查询订单，返回日期和数字的列表
     *
     * @param userId    用户ID
     * @param payStatus 支付状态
     * @return 返回日期和数字的列表，用于 ECharts 热力图
     */
    @GetMapping("/getTravelData")
    public BaseResponse<List<Map<String, Object>>> getTravelData(
            @RequestParam Long userId,
            @RequestParam Integer payStatus) {
        // 构建查询条件
        SpotOrderQueryRequest queryRequest = new SpotOrderQueryRequest();
        queryRequest.setUserId(userId);
        queryRequest.setPayStatus(payStatus);

        // 查询符合条件的订单
        List<SpotOrder> orderList = spotOrderService.listByQuery(queryRequest);

        // 处理查询结果，生成日期和数字的列表
        List<Map<String, Object>> result = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 统计每个日期的订单数量
        Map<String, Integer> dateCountMap = new HashMap<>();
        for (SpotOrder order : orderList) {
            String dateStr = dateFormat.format(order.getCreateTime());
            dateCountMap.put(dateStr, dateCountMap.getOrDefault(dateStr, 0) + 1);
        }

        // 将统计结果转换为前端需要的格式
        for (Map.Entry<String, Integer> entry : dateCountMap.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", entry.getKey());
            item.put("value", entry.getValue());
            result.add(item);
        }

        return ResultUtils.success(result);
    }
    // endregion
}
