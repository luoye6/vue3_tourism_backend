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
import com.xiaobaitiao.springbootinit.model.dto.notice.NoticeAddRequest;
import com.xiaobaitiao.springbootinit.model.dto.notice.NoticeQueryRequest;
import com.xiaobaitiao.springbootinit.model.dto.notice.NoticeUpdateRequest;
import com.xiaobaitiao.springbootinit.model.entity.Notice;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.NoticeVO;
import com.xiaobaitiao.springbootinit.service.NoticeService;
import com.xiaobaitiao.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 公告接口
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@RestController
@RequestMapping("/notice")
@Slf4j
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建公告
     *
     * @param noticeAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addNotice(@RequestBody NoticeAddRequest noticeAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(noticeAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeAddRequest, notice);
        // 数据校验
        noticeService.validNotice(notice, true);
        // 写入数据库
        boolean result = noticeService.save(notice);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newNoticeId = notice.getId();
        return ResultUtils.success(newNoticeId);
    }

    /**
     * 删除公告
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteNotice(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        Notice oldNotice = noticeService.getById(id);
        ThrowUtils.throwIf(oldNotice == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅管理员可删除
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = noticeService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新公告（仅管理员可用）
     *
     * @param noticeUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateNotice(@RequestBody NoticeUpdateRequest noticeUpdateRequest) {
        if (noticeUpdateRequest == null || noticeUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeUpdateRequest, notice);
        // 数据校验
        noticeService.validNotice(notice, false);
        // 判断是否存在
        long id = noticeUpdateRequest.getId();
        Notice oldNotice = noticeService.getById(id);
        ThrowUtils.throwIf(oldNotice == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = noticeService.updateById(notice);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取公告（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<NoticeVO> getNoticeVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Notice notice = noticeService.getById(id);
        ThrowUtils.throwIf(notice == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(noticeService.getNoticeVO(notice, request));
    }

    /**
     * 分页获取公告列表（仅管理员可用）
     *
     * @param noticeQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Notice>> listNoticeByPage(@RequestBody NoticeQueryRequest noticeQueryRequest) {
        long current = noticeQueryRequest.getCurrent();
        long size = noticeQueryRequest.getPageSize();
        // 查询数据库
        Page<Notice> noticePage = noticeService.page(new Page<>(current, size),
                noticeService.getQueryWrapper(noticeQueryRequest));
        return ResultUtils.success(noticePage);
    }

    /**
     * 分页获取公告列表（封装类）
     *
     * @param noticeQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<NoticeVO>> listNoticeVOByPage(@RequestBody NoticeQueryRequest noticeQueryRequest,
                                                           HttpServletRequest request) {
        long current = noticeQueryRequest.getCurrent();
        long size = noticeQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Notice> noticePage = noticeService.page(new Page<>(current, size),
                noticeService.getQueryWrapper(noticeQueryRequest));
        // 获取封装类
        return ResultUtils.success(noticeService.getNoticeVOPage(noticePage, request));
    }

    /**
     * 分页获取当前登录用户创建的公告列表
     *
     * @param noticeQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<NoticeVO>> listMyNoticeVOByPage(@RequestBody NoticeQueryRequest noticeQueryRequest,
                                                             HttpServletRequest request) {
        ThrowUtils.throwIf(noticeQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        noticeQueryRequest.setNoticeAdminId(loginUser.getId());
        long current = noticeQueryRequest.getCurrent();
        long size = noticeQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Notice> noticePage = noticeService.page(new Page<>(current, size),
                noticeService.getQueryWrapper(noticeQueryRequest));
        // 获取封装类
        return ResultUtils.success(noticeService.getNoticeVOPage(noticePage, request));
    }



    // endregion
}
