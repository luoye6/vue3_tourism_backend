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
import com.xiaobaitiao.springbootinit.model.dto.barrage.BarrageAddRequest;
import com.xiaobaitiao.springbootinit.model.dto.barrage.BarrageEditRequest;
import com.xiaobaitiao.springbootinit.model.dto.barrage.BarrageQueryRequest;
import com.xiaobaitiao.springbootinit.model.dto.barrage.BarrageUpdateRequest;
import com.xiaobaitiao.springbootinit.model.entity.Barrage;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.BarrageVO;
import com.xiaobaitiao.springbootinit.service.BarrageService;
import com.xiaobaitiao.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 弹幕接口
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@RestController
@RequestMapping("/barrage")
@Slf4j
public class BarrageController {

    @Resource
    private BarrageService barrageService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建弹幕
     *
     * @param barrageAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addBarrage(@RequestBody BarrageAddRequest barrageAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(barrageAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Barrage barrage = new Barrage();
        BeanUtils.copyProperties(barrageAddRequest, barrage);
        User user = userService.getLoginUser(request);
        barrage.setUserId(user.getId());
        // 数据校验
        barrageService.validBarrage(barrage, true);
        // 写入数据库
        boolean result = barrageService.save(barrage);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newBarrageId = barrage.getId();
        return ResultUtils.success(newBarrageId);
    }

    /**
     * 删除弹幕
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteBarrage(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Barrage oldBarrage = barrageService.getById(id);
        ThrowUtils.throwIf(oldBarrage == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldBarrage.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = barrageService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新弹幕（仅管理员可用）
     *
     * @param barrageUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateBarrage(@RequestBody BarrageUpdateRequest barrageUpdateRequest) {
        if (barrageUpdateRequest == null || barrageUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Barrage barrage = new Barrage();
        BeanUtils.copyProperties(barrageUpdateRequest, barrage);
        // 数据校验
        barrageService.validBarrage(barrage, false);
        // 判断是否存在
        long id = barrageUpdateRequest.getId();
        Barrage oldBarrage = barrageService.getById(id);
        ThrowUtils.throwIf(oldBarrage == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = barrageService.updateById(barrage);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取弹幕（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<BarrageVO> getBarrageVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Barrage barrage = barrageService.getById(id);
        ThrowUtils.throwIf(barrage == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(barrageService.getBarrageVO(barrage, request));
    }

    /**
     * 分页获取弹幕列表（仅管理员可用）
     *
     * @param barrageQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Barrage>> listBarrageByPage(@RequestBody BarrageQueryRequest barrageQueryRequest) {
        long current = barrageQueryRequest.getCurrent();
        long size = barrageQueryRequest.getPageSize();
        // 查询数据库
        Page<Barrage> barragePage = barrageService.page(new Page<>(current, size),
                barrageService.getQueryWrapper(barrageQueryRequest));
        return ResultUtils.success(barragePage);
    }

    /**
     * 分页获取弹幕列表（封装类）
     *
     * @param barrageQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<BarrageVO>> listBarrageVOByPage(@RequestBody BarrageQueryRequest barrageQueryRequest,
                                                               HttpServletRequest request) {
        long current = barrageQueryRequest.getCurrent();
        long size = barrageQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 500, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Barrage> barragePage = barrageService.page(new Page<>(current, size),
                barrageService.getQueryWrapper(barrageQueryRequest));
        // 获取封装类
        return ResultUtils.success(barrageService.getBarrageVOPage(barragePage, request));
    }

    /**
     * 分页获取当前登录用户创建的弹幕列表
     *
     * @param barrageQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<BarrageVO>> listMyBarrageVOByPage(@RequestBody BarrageQueryRequest barrageQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(barrageQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        barrageQueryRequest.setUserId(loginUser.getId());
        long current = barrageQueryRequest.getCurrent();
        long size = barrageQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 500, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Barrage> barragePage = barrageService.page(new Page<>(current, size),
                barrageService.getQueryWrapper(barrageQueryRequest));
        // 获取封装类
        return ResultUtils.success(barrageService.getBarrageVOPage(barragePage, request));
    }


}
