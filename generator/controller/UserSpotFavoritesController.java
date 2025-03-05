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
import com.xiaobaitiao.springbootinit.model.dto.userSpotFavorites.UserSpotFavoritesAddRequest;
import com.xiaobaitiao.springbootinit.model.dto.userSpotFavorites.UserSpotFavoritesEditRequest;
import com.xiaobaitiao.springbootinit.model.dto.userSpotFavorites.UserSpotFavoritesQueryRequest;
import com.xiaobaitiao.springbootinit.model.dto.userSpotFavorites.UserSpotFavoritesUpdateRequest;
import com.xiaobaitiao.springbootinit.model.entity.UserSpotFavorites;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.UserSpotFavoritesVO;
import com.xiaobaitiao.springbootinit.service.UserSpotFavoritesService;
import com.xiaobaitiao.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户景点收藏关联表接口
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@RestController
@RequestMapping("/userSpotFavorites")
@Slf4j
public class UserSpotFavoritesController {

    @Resource
    private UserSpotFavoritesService userSpotFavoritesService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建用户景点收藏关联表
     *
     * @param userSpotFavoritesAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addUserSpotFavorites(@RequestBody UserSpotFavoritesAddRequest userSpotFavoritesAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userSpotFavoritesAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        UserSpotFavorites userSpotFavorites = new UserSpotFavorites();
        BeanUtils.copyProperties(userSpotFavoritesAddRequest, userSpotFavorites);
        // 数据校验
        userSpotFavoritesService.validUserSpotFavorites(userSpotFavorites, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        userSpotFavorites.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = userSpotFavoritesService.save(userSpotFavorites);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newUserSpotFavoritesId = userSpotFavorites.getId();
        return ResultUtils.success(newUserSpotFavoritesId);
    }

    /**
     * 删除用户景点收藏关联表
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUserSpotFavorites(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserSpotFavorites oldUserSpotFavorites = userSpotFavoritesService.getById(id);
        ThrowUtils.throwIf(oldUserSpotFavorites == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldUserSpotFavorites.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = userSpotFavoritesService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新用户景点收藏关联表（仅管理员可用）
     *
     * @param userSpotFavoritesUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserSpotFavorites(@RequestBody UserSpotFavoritesUpdateRequest userSpotFavoritesUpdateRequest) {
        if (userSpotFavoritesUpdateRequest == null || userSpotFavoritesUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        UserSpotFavorites userSpotFavorites = new UserSpotFavorites();
        BeanUtils.copyProperties(userSpotFavoritesUpdateRequest, userSpotFavorites);
        // 数据校验
        userSpotFavoritesService.validUserSpotFavorites(userSpotFavorites, false);
        // 判断是否存在
        long id = userSpotFavoritesUpdateRequest.getId();
        UserSpotFavorites oldUserSpotFavorites = userSpotFavoritesService.getById(id);
        ThrowUtils.throwIf(oldUserSpotFavorites == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = userSpotFavoritesService.updateById(userSpotFavorites);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户景点收藏关联表（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserSpotFavoritesVO> getUserSpotFavoritesVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        UserSpotFavorites userSpotFavorites = userSpotFavoritesService.getById(id);
        ThrowUtils.throwIf(userSpotFavorites == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(userSpotFavoritesService.getUserSpotFavoritesVO(userSpotFavorites, request));
    }

    /**
     * 分页获取用户景点收藏关联表列表（仅管理员可用）
     *
     * @param userSpotFavoritesQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserSpotFavorites>> listUserSpotFavoritesByPage(@RequestBody UserSpotFavoritesQueryRequest userSpotFavoritesQueryRequest) {
        long current = userSpotFavoritesQueryRequest.getCurrent();
        long size = userSpotFavoritesQueryRequest.getPageSize();
        // 查询数据库
        Page<UserSpotFavorites> userSpotFavoritesPage = userSpotFavoritesService.page(new Page<>(current, size),
                userSpotFavoritesService.getQueryWrapper(userSpotFavoritesQueryRequest));
        return ResultUtils.success(userSpotFavoritesPage);
    }

    /**
     * 分页获取用户景点收藏关联表列表（封装类）
     *
     * @param userSpotFavoritesQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserSpotFavoritesVO>> listUserSpotFavoritesVOByPage(@RequestBody UserSpotFavoritesQueryRequest userSpotFavoritesQueryRequest,
                                                               HttpServletRequest request) {
        long current = userSpotFavoritesQueryRequest.getCurrent();
        long size = userSpotFavoritesQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<UserSpotFavorites> userSpotFavoritesPage = userSpotFavoritesService.page(new Page<>(current, size),
                userSpotFavoritesService.getQueryWrapper(userSpotFavoritesQueryRequest));
        // 获取封装类
        return ResultUtils.success(userSpotFavoritesService.getUserSpotFavoritesVOPage(userSpotFavoritesPage, request));
    }

    /**
     * 分页获取当前登录用户创建的用户景点收藏关联表列表
     *
     * @param userSpotFavoritesQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<UserSpotFavoritesVO>> listMyUserSpotFavoritesVOByPage(@RequestBody UserSpotFavoritesQueryRequest userSpotFavoritesQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(userSpotFavoritesQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        userSpotFavoritesQueryRequest.setUserId(loginUser.getId());
        long current = userSpotFavoritesQueryRequest.getCurrent();
        long size = userSpotFavoritesQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<UserSpotFavorites> userSpotFavoritesPage = userSpotFavoritesService.page(new Page<>(current, size),
                userSpotFavoritesService.getQueryWrapper(userSpotFavoritesQueryRequest));
        // 获取封装类
        return ResultUtils.success(userSpotFavoritesService.getUserSpotFavoritesVOPage(userSpotFavoritesPage, request));
    }

    /**
     * 编辑用户景点收藏关联表（给用户使用）
     *
     * @param userSpotFavoritesEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editUserSpotFavorites(@RequestBody UserSpotFavoritesEditRequest userSpotFavoritesEditRequest, HttpServletRequest request) {
        if (userSpotFavoritesEditRequest == null || userSpotFavoritesEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        UserSpotFavorites userSpotFavorites = new UserSpotFavorites();
        BeanUtils.copyProperties(userSpotFavoritesEditRequest, userSpotFavorites);
        // 数据校验
        userSpotFavoritesService.validUserSpotFavorites(userSpotFavorites, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = userSpotFavoritesEditRequest.getId();
        UserSpotFavorites oldUserSpotFavorites = userSpotFavoritesService.getById(id);
        ThrowUtils.throwIf(oldUserSpotFavorites == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldUserSpotFavorites.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = userSpotFavoritesService.updateById(userSpotFavorites);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
