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
import com.xiaobaitiao.springbootinit.model.dto.spotFee.*;
import com.xiaobaitiao.springbootinit.model.entity.SpotFee;
import com.xiaobaitiao.springbootinit.model.entity.SpotOrder;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.SpotFeeVO;
import com.xiaobaitiao.springbootinit.service.SpotFeeService;
import com.xiaobaitiao.springbootinit.service.SpotOrderService;
import com.xiaobaitiao.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * 景点门票表接口
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@RestController
@RequestMapping("/spotFee")
@Slf4j
public class SpotFeeController {

    @Resource
    private SpotFeeService spotFeeService;
    @Resource
    private SpotOrderService spotOrderService;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建景点门票表
     *
     * @param spotFeeAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addSpotFee(@RequestBody SpotFeeAddRequest spotFeeAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(spotFeeAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        SpotFee spotFee = new SpotFee();
        BeanUtils.copyProperties(spotFeeAddRequest, spotFee);
        // 数据校验
        spotFeeService.validSpotFee(spotFee, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        spotFee.setAdminId(loginUser.getId());
        // 写入数据库
        boolean result = spotFeeService.save(spotFee);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newSpotFeeId = spotFee.getId();
        return ResultUtils.success(newSpotFeeId);
    }

    /**
     * 删除景点门票表
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteSpotFee(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        SpotFee oldSpotFee = spotFeeService.getById(id);
        ThrowUtils.throwIf(oldSpotFee == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldSpotFee.getAdminId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = spotFeeService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新景点门票表（仅管理员可用）
     *
     * @param spotFeeUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateSpotFee(@RequestBody SpotFeeUpdateRequest spotFeeUpdateRequest) {
        if (spotFeeUpdateRequest == null || spotFeeUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        SpotFee spotFee = new SpotFee();
        BeanUtils.copyProperties(spotFeeUpdateRequest, spotFee);
        // 数据校验
        spotFeeService.validSpotFee(spotFee, false);
        // 判断是否存在
        long id = spotFeeUpdateRequest.getId();
        SpotFee oldSpotFee = spotFeeService.getById(id);
        ThrowUtils.throwIf(oldSpotFee == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = spotFeeService.updateById(spotFee);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取景点门票表（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<SpotFeeVO> getSpotFeeVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        SpotFee spotFee = spotFeeService.getById(id);
        ThrowUtils.throwIf(spotFee == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(spotFeeService.getSpotFeeVO(spotFee, request));
    }

    /**
     * 分页获取景点门票表列表（仅管理员可用）
     *
     * @param spotFeeQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<SpotFee>> listSpotFeeByPage(@RequestBody SpotFeeQueryRequest spotFeeQueryRequest) {
        long current = spotFeeQueryRequest.getCurrent();
        long size = spotFeeQueryRequest.getPageSize();
        // 查询数据库
        Page<SpotFee> spotFeePage = spotFeeService.page(new Page<>(current, size),
                spotFeeService.getQueryWrapper(spotFeeQueryRequest));
        return ResultUtils.success(spotFeePage);
    }

    /**
     * 分页获取景点门票表列表（封装类）
     *
     * @param spotFeeQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<SpotFeeVO>> listSpotFeeVOByPage(@RequestBody SpotFeeQueryRequest spotFeeQueryRequest,
                                                             HttpServletRequest request) {
        long current = spotFeeQueryRequest.getCurrent();
        long size = spotFeeQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<SpotFee> spotFeePage = spotFeeService.page(new Page<>(current, size),
                spotFeeService.getQueryWrapper(spotFeeQueryRequest));
        // 获取封装类
        return ResultUtils.success(spotFeeService.getSpotFeeVOPage(spotFeePage, request));
    }

    /**
     * 分页获取当前登录用户创建的景点门票表列表
     *
     * @param spotFeeQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<SpotFeeVO>> listMySpotFeeVOByPage(@RequestBody SpotFeeQueryRequest spotFeeQueryRequest,
                                                               HttpServletRequest request) {
        ThrowUtils.throwIf(spotFeeQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        spotFeeQueryRequest.setAdminId(loginUser.getId());
        long current = spotFeeQueryRequest.getCurrent();
        long size = spotFeeQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<SpotFee> spotFeePage = spotFeeService.page(new Page<>(current, size),
                spotFeeService.getQueryWrapper(spotFeeQueryRequest));
        // 获取封装类
        return ResultUtils.success(spotFeeService.getSpotFeeVOPage(spotFeePage, request));
    }

    /**
     * 编辑景点门票表（给用户使用）
     *
     * @param spotFeeEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editSpotFee(@RequestBody SpotFeeEditRequest spotFeeEditRequest, HttpServletRequest request) {
        if (spotFeeEditRequest == null || spotFeeEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        SpotFee spotFee = new SpotFee();
        BeanUtils.copyProperties(spotFeeEditRequest, spotFee);
        // 数据校验
        spotFeeService.validSpotFee(spotFee, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = spotFeeEditRequest.getId();
        SpotFee oldSpotFee = spotFeeService.getById(id);
        ThrowUtils.throwIf(oldSpotFee == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldSpotFee.getAdminId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = spotFeeService.updateById(spotFee);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 购票功能（多线程安全问题和事务回滚）
     *
     * @param buySpotFeeRequest 购票请求
     * @param request           HTTP 请求
     * @return 是否成功
     */
    @PostMapping("/buy")
    public synchronized BaseResponse<Boolean> buySpotFee(@RequestBody BuySpotFeeRequest buySpotFeeRequest, HttpServletRequest request) {
        // 1. 参数校验
        if (buySpotFeeRequest == null || buySpotFeeRequest.getSpotFeeId() == null || buySpotFeeRequest.getSpotFeeQuantity() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }

        // 2. 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 3. 查询门票信息
        SpotFee spotFee = spotFeeService.getByIdWithLock(buySpotFeeRequest.getSpotFeeId());
        if (spotFee == null || spotFee.getIsDelete() == 1) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "门票不存在");
        }

        // 4. 查询用户信息
        User user = userService.getById(userId);
        if (user == null || user.getIsDelete() == 1) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }

        // 5. 计算订单总金额
        BigDecimal totalAmount = spotFee.getSpotFeePrice().multiply(new BigDecimal(buySpotFeeRequest.getSpotFeeQuantity()));

        // 6. 检查门票数量是否足够
        if (spotFee.getSpotFeeNumber() < buySpotFeeRequest.getSpotFeeQuantity()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "门票数量不足");
        }

        // 7. 检查用户余额是否足够
        if (user.getBalance().compareTo(totalAmount) < 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户余额不足");
        }

        // 8. 使用事务管理
        // 8.1 扣减门票数量
        // 8.2 扣减用户余额
        // 8.3 新增订单记录
        // 默认已支付
        // 事务回滚
        boolean result = Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            try {
                // 8.1 扣减门票数量
                spotFee.setSpotFeeNumber(spotFee.getSpotFeeNumber() - buySpotFeeRequest.getSpotFeeQuantity());
                boolean updateSpotFeeResult = spotFeeService.updateById(spotFee);
                if (!updateSpotFeeResult) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新门票数量失败");
                }

                // 8.2 扣减用户余额
                user.setBalance(user.getBalance().subtract(totalAmount));
                boolean updateUserResult = userService.updateById(user);
                if (!updateUserResult) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新用户余额失败");
                }

                // 8.3 新增订单记录
                SpotOrder spotOrder = new SpotOrder();
                spotOrder.setUserId(userId);
                spotOrder.setSpotFeeId(buySpotFeeRequest.getSpotFeeId());
                spotOrder.setUserName(buySpotFeeRequest.getUserName());
                spotOrder.setUserPhone(buySpotFeeRequest.getUserPhone());
                spotOrder.setPaymentAmount(totalAmount);
                spotOrder.setPayStatus(1); // 默认已支付
                boolean addOrderResult = spotOrderService.save(spotOrder);
                if (!addOrderResult) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "新增订单失败");
                }

                return true;
            } catch (Exception e) {
                // 事务回滚
                status.setRollbackOnly();
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "购票失败：" + e.getMessage());
            }
        }));

        return ResultUtils.success(result);
    }
    // endregion
}
