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
import com.xiaobaitiao.springbootinit.model.dto.spotScore.SpotScoreAddRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotScore.SpotScoreEditRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotScore.SpotScoreQueryRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotScore.SpotScoreUpdateRequest;
import com.xiaobaitiao.springbootinit.model.entity.SpotScore;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.SpotScoreVO;
import com.xiaobaitiao.springbootinit.service.SpotScoreService;
import com.xiaobaitiao.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

/**
 * 景点评分表接口
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@RestController
@RequestMapping("/spotScore")
@Slf4j
public class SpotScoreController {

    @Resource
    private SpotScoreService spotScoreService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建景点评分表
     *
     * @param spotScoreAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addSpotScore(@RequestBody SpotScoreAddRequest spotScoreAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(spotScoreAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        SpotScore spotScore = new SpotScore();
        BeanUtils.copyProperties(spotScoreAddRequest, spotScore);
        // 数据校验
        spotScoreService.validSpotScore(spotScore, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        spotScore.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = spotScoreService.save(spotScore);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newSpotScoreId = spotScore.getId();
        return ResultUtils.success(newSpotScoreId);
    }

    /**
     * 删除景点评分表
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteSpotScore(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        SpotScore oldSpotScore = spotScoreService.getById(id);
        ThrowUtils.throwIf(oldSpotScore == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldSpotScore.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = spotScoreService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新景点评分表（仅管理员可用）
     *
     * @param spotScoreUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateSpotScore(@RequestBody SpotScoreUpdateRequest spotScoreUpdateRequest) {
        if (spotScoreUpdateRequest == null || spotScoreUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        SpotScore spotScore = new SpotScore();
        BeanUtils.copyProperties(spotScoreUpdateRequest, spotScore);
        // 数据校验
        spotScoreService.validSpotScore(spotScore, false);
        // 判断是否存在
        long id = spotScoreUpdateRequest.getId();
        SpotScore oldSpotScore = spotScoreService.getById(id);
        ThrowUtils.throwIf(oldSpotScore == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = spotScoreService.updateById(spotScore);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取景点评分表（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<SpotScoreVO> getSpotScoreVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        SpotScore spotScore = spotScoreService.getById(id);
        ThrowUtils.throwIf(spotScore == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(spotScoreService.getSpotScoreVO(spotScore, request));
    }

    /**
     * 分页获取景点评分表列表（仅管理员可用）
     *
     * @param spotScoreQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<SpotScore>> listSpotScoreByPage(@RequestBody SpotScoreQueryRequest spotScoreQueryRequest) {
        long current = spotScoreQueryRequest.getCurrent();
        long size = spotScoreQueryRequest.getPageSize();
        // 查询数据库
        Page<SpotScore> spotScorePage = spotScoreService.page(new Page<>(current, size),
                spotScoreService.getQueryWrapper(spotScoreQueryRequest));
        return ResultUtils.success(spotScorePage);
    }

    /**
     * 分页获取景点评分表列表（封装类）
     *
     * @param spotScoreQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<SpotScoreVO>> listSpotScoreVOByPage(@RequestBody SpotScoreQueryRequest spotScoreQueryRequest,
                                                                 HttpServletRequest request) {
        long current = spotScoreQueryRequest.getCurrent();
        long size = spotScoreQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 500, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<SpotScore> spotScorePage = spotScoreService.page(new Page<>(current, size),
                spotScoreService.getQueryWrapper(spotScoreQueryRequest));
        // 获取封装类
        return ResultUtils.success(spotScoreService.getSpotScoreVOPage(spotScorePage, request));
    }

    /**
     * 分页获取当前登录用户创建的景点评分表列表
     *
     * @param spotScoreQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<SpotScoreVO>> listMySpotScoreVOByPage(@RequestBody SpotScoreQueryRequest spotScoreQueryRequest,
                                                                   HttpServletRequest request) {
        ThrowUtils.throwIf(spotScoreQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        spotScoreQueryRequest.setUserId(loginUser.getId());
        long current = spotScoreQueryRequest.getCurrent();
        long size = spotScoreQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<SpotScore> spotScorePage = spotScoreService.page(new Page<>(current, size),
                spotScoreService.getQueryWrapper(spotScoreQueryRequest));
        // 获取封装类
        return ResultUtils.success(spotScoreService.getSpotScoreVOPage(spotScorePage, request));
    }

    /**
     * 编辑景点评分表（给用户使用）
     *
     * @param spotScoreEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editSpotScore(@RequestBody SpotScoreEditRequest spotScoreEditRequest, HttpServletRequest request) {
        if (spotScoreEditRequest == null || spotScoreEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        SpotScore spotScore = new SpotScore();
        BeanUtils.copyProperties(spotScoreEditRequest, spotScore);
        // 数据校验
        spotScoreService.validSpotScore(spotScore, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = spotScoreEditRequest.getId();
        SpotScore oldSpotScore = spotScoreService.getById(id);
        ThrowUtils.throwIf(oldSpotScore == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldSpotScore.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = spotScoreService.updateById(spotScore);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }


    /**
     * 获取景点的平均评分
     *
     * @param spotId 景点 ID
     * @return 平均评分
     */
    @GetMapping("/averageScore")
    public BaseResponse getAverageScore(@RequestParam Long spotId) {
        Optional<Double> averageScoreBySpotId = Optional.ofNullable(spotScoreService.getAverageScoreBySpotId(spotId));
        if (averageScoreBySpotId.isPresent()) {
            double result = Math.round(averageScoreBySpotId.get() * 100.0) / 100.0;
            return ResultUtils.success(result);
        }
        return ResultUtils.error(ErrorCode.PARAMS_ERROR,"景点评分为空，快来成为第一个评分的人吧");
    }
    // endregion
}
