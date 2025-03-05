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
import com.xiaobaitiao.springbootinit.model.dto.spotFee.SpotFeeAddRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotFee.SpotFeeEditRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotFee.SpotFeeQueryRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotFee.SpotFeeUpdateRequest;
import com.xiaobaitiao.springbootinit.model.entity.SpotFee;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.SpotFeeVO;
import com.xiaobaitiao.springbootinit.service.SpotFeeService;
import com.xiaobaitiao.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
        spotFee.setUserId(loginUser.getId());
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
        if (!oldSpotFee.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
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
        spotFeeQueryRequest.setUserId(loginUser.getId());
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
        if (!oldSpotFee.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = spotFeeService.updateById(spotFee);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
