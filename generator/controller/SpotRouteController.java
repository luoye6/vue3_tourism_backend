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
import com.xiaobaitiao.springbootinit.model.dto.spotRoute.SpotRouteAddRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotRoute.SpotRouteEditRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotRoute.SpotRouteQueryRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotRoute.SpotRouteUpdateRequest;
import com.xiaobaitiao.springbootinit.model.entity.SpotRoute;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.SpotRouteVO;
import com.xiaobaitiao.springbootinit.service.SpotRouteService;
import com.xiaobaitiao.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 景点路线表接口
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@RestController
@RequestMapping("/spotRoute")
@Slf4j
public class SpotRouteController {

    @Resource
    private SpotRouteService spotRouteService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建景点路线表
     *
     * @param spotRouteAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addSpotRoute(@RequestBody SpotRouteAddRequest spotRouteAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(spotRouteAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        SpotRoute spotRoute = new SpotRoute();
        BeanUtils.copyProperties(spotRouteAddRequest, spotRoute);
        // 数据校验
        spotRouteService.validSpotRoute(spotRoute, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        spotRoute.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = spotRouteService.save(spotRoute);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newSpotRouteId = spotRoute.getId();
        return ResultUtils.success(newSpotRouteId);
    }

    /**
     * 删除景点路线表
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteSpotRoute(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        SpotRoute oldSpotRoute = spotRouteService.getById(id);
        ThrowUtils.throwIf(oldSpotRoute == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldSpotRoute.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = spotRouteService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新景点路线表（仅管理员可用）
     *
     * @param spotRouteUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateSpotRoute(@RequestBody SpotRouteUpdateRequest spotRouteUpdateRequest) {
        if (spotRouteUpdateRequest == null || spotRouteUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        SpotRoute spotRoute = new SpotRoute();
        BeanUtils.copyProperties(spotRouteUpdateRequest, spotRoute);
        // 数据校验
        spotRouteService.validSpotRoute(spotRoute, false);
        // 判断是否存在
        long id = spotRouteUpdateRequest.getId();
        SpotRoute oldSpotRoute = spotRouteService.getById(id);
        ThrowUtils.throwIf(oldSpotRoute == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = spotRouteService.updateById(spotRoute);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取景点路线表（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<SpotRouteVO> getSpotRouteVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        SpotRoute spotRoute = spotRouteService.getById(id);
        ThrowUtils.throwIf(spotRoute == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(spotRouteService.getSpotRouteVO(spotRoute, request));
    }

    /**
     * 分页获取景点路线表列表（仅管理员可用）
     *
     * @param spotRouteQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<SpotRoute>> listSpotRouteByPage(@RequestBody SpotRouteQueryRequest spotRouteQueryRequest) {
        long current = spotRouteQueryRequest.getCurrent();
        long size = spotRouteQueryRequest.getPageSize();
        // 查询数据库
        Page<SpotRoute> spotRoutePage = spotRouteService.page(new Page<>(current, size),
                spotRouteService.getQueryWrapper(spotRouteQueryRequest));
        return ResultUtils.success(spotRoutePage);
    }

    /**
     * 分页获取景点路线表列表（封装类）
     *
     * @param spotRouteQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<SpotRouteVO>> listSpotRouteVOByPage(@RequestBody SpotRouteQueryRequest spotRouteQueryRequest,
                                                               HttpServletRequest request) {
        long current = spotRouteQueryRequest.getCurrent();
        long size = spotRouteQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<SpotRoute> spotRoutePage = spotRouteService.page(new Page<>(current, size),
                spotRouteService.getQueryWrapper(spotRouteQueryRequest));
        // 获取封装类
        return ResultUtils.success(spotRouteService.getSpotRouteVOPage(spotRoutePage, request));
    }

    /**
     * 分页获取当前登录用户创建的景点路线表列表
     *
     * @param spotRouteQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<SpotRouteVO>> listMySpotRouteVOByPage(@RequestBody SpotRouteQueryRequest spotRouteQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(spotRouteQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        spotRouteQueryRequest.setUserId(loginUser.getId());
        long current = spotRouteQueryRequest.getCurrent();
        long size = spotRouteQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<SpotRoute> spotRoutePage = spotRouteService.page(new Page<>(current, size),
                spotRouteService.getQueryWrapper(spotRouteQueryRequest));
        // 获取封装类
        return ResultUtils.success(spotRouteService.getSpotRouteVOPage(spotRoutePage, request));
    }

    /**
     * 编辑景点路线表（给用户使用）
     *
     * @param spotRouteEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editSpotRoute(@RequestBody SpotRouteEditRequest spotRouteEditRequest, HttpServletRequest request) {
        if (spotRouteEditRequest == null || spotRouteEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        SpotRoute spotRoute = new SpotRoute();
        BeanUtils.copyProperties(spotRouteEditRequest, spotRoute);
        // 数据校验
        spotRouteService.validSpotRoute(spotRoute, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = spotRouteEditRequest.getId();
        SpotRoute oldSpotRoute = spotRouteService.getById(id);
        ThrowUtils.throwIf(oldSpotRoute == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldSpotRoute.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = spotRouteService.updateById(spotRoute);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
