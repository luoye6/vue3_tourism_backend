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
import com.xiaobaitiao.springbootinit.model.dto.spot.SpotAddRequest;
import com.xiaobaitiao.springbootinit.model.dto.spot.SpotEditRequest;
import com.xiaobaitiao.springbootinit.model.dto.spot.SpotQueryRequest;
import com.xiaobaitiao.springbootinit.model.dto.spot.SpotUpdateRequest;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.SpotVO;
import com.xiaobaitiao.springbootinit.service.SpotService;
import com.xiaobaitiao.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 景点表接口
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@RestController
@RequestMapping("/spot")
@Slf4j
public class SpotController {

    @Resource
    private SpotService spotService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建景点表
     *
     * @param spotAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addSpot(@RequestBody SpotAddRequest spotAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(spotAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Spot spot = new Spot();
        BeanUtils.copyProperties(spotAddRequest, spot);
        // 数据校验
        spotService.validSpot(spot, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        spot.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = spotService.save(spot);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newSpotId = spot.getId();
        return ResultUtils.success(newSpotId);
    }

    /**
     * 删除景点表
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteSpot(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Spot oldSpot = spotService.getById(id);
        ThrowUtils.throwIf(oldSpot == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldSpot.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = spotService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新景点表（仅管理员可用）
     *
     * @param spotUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateSpot(@RequestBody SpotUpdateRequest spotUpdateRequest) {
        if (spotUpdateRequest == null || spotUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Spot spot = new Spot();
        BeanUtils.copyProperties(spotUpdateRequest, spot);
        // 数据校验
        spotService.validSpot(spot, false);
        // 判断是否存在
        long id = spotUpdateRequest.getId();
        Spot oldSpot = spotService.getById(id);
        ThrowUtils.throwIf(oldSpot == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = spotService.updateById(spot);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取景点表（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<SpotVO> getSpotVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Spot spot = spotService.getById(id);
        ThrowUtils.throwIf(spot == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(spotService.getSpotVO(spot, request));
    }

    /**
     * 分页获取景点表列表（仅管理员可用）
     *
     * @param spotQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Spot>> listSpotByPage(@RequestBody SpotQueryRequest spotQueryRequest) {
        long current = spotQueryRequest.getCurrent();
        long size = spotQueryRequest.getPageSize();
        // 查询数据库
        Page<Spot> spotPage = spotService.page(new Page<>(current, size),
                spotService.getQueryWrapper(spotQueryRequest));
        return ResultUtils.success(spotPage);
    }

    /**
     * 分页获取景点表列表（封装类）
     *
     * @param spotQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<SpotVO>> listSpotVOByPage(@RequestBody SpotQueryRequest spotQueryRequest,
                                                               HttpServletRequest request) {
        long current = spotQueryRequest.getCurrent();
        long size = spotQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Spot> spotPage = spotService.page(new Page<>(current, size),
                spotService.getQueryWrapper(spotQueryRequest));
        // 获取封装类
        return ResultUtils.success(spotService.getSpotVOPage(spotPage, request));
    }

    /**
     * 分页获取当前登录用户创建的景点表列表
     *
     * @param spotQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<SpotVO>> listMySpotVOByPage(@RequestBody SpotQueryRequest spotQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(spotQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        spotQueryRequest.setUserId(loginUser.getId());
        long current = spotQueryRequest.getCurrent();
        long size = spotQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Spot> spotPage = spotService.page(new Page<>(current, size),
                spotService.getQueryWrapper(spotQueryRequest));
        // 获取封装类
        return ResultUtils.success(spotService.getSpotVOPage(spotPage, request));
    }

    /**
     * 编辑景点表（给用户使用）
     *
     * @param spotEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editSpot(@RequestBody SpotEditRequest spotEditRequest, HttpServletRequest request) {
        if (spotEditRequest == null || spotEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Spot spot = new Spot();
        BeanUtils.copyProperties(spotEditRequest, spot);
        // 数据校验
        spotService.validSpot(spot, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = spotEditRequest.getId();
        Spot oldSpot = spotService.getById(id);
        ThrowUtils.throwIf(oldSpot == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldSpot.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = spotService.updateById(spot);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
