package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.NoticeMapper;
import com.xiaobaitiao.springbootinit.model.dto.notice.NoticeQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.Notice;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.NoticeVO;
import com.xiaobaitiao.springbootinit.model.vo.UserVO;
import com.xiaobaitiao.springbootinit.service.NoticeService;
import com.xiaobaitiao.springbootinit.service.UserService;
import com.xiaobaitiao.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 公告服务实现
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Service
@Slf4j
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param notice
     * @param add    对创建的数据进行校验
     */
    @Override
    public void validNotice(Notice notice, boolean add) {
        ThrowUtils.throwIf(notice == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String noticeTitle = notice.getNoticeTitle();
        String noticeContent = notice.getNoticeContent();
        Long noticeAdminId = notice.getNoticeAdminId();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(noticeTitle), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(noticeContent), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(noticeAdminId < 0, ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        ThrowUtils.throwIf(StringUtils.isBlank(noticeTitle), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(StringUtils.isBlank(noticeContent), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(noticeAdminId < 0, ErrorCode.PARAMS_ERROR);
    }

    /**
     * 获取查询条件
     *
     * @param noticeQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Notice> getQueryWrapper(NoticeQueryRequest noticeQueryRequest) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        if (noticeQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        String noticeTitle = noticeQueryRequest.getNoticeTitle();
        String noticeContent = noticeQueryRequest.getNoticeContent();
        String sortField = noticeQueryRequest.getSortField();
        String sortOrder = noticeQueryRequest.getSortOrder();
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(noticeTitle), "noticeTitle", noticeTitle);
        queryWrapper.like(StringUtils.isNotBlank(noticeContent), "noticeContent", noticeContent);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取公告封装
     *
     * @param notice
     * @param request
     * @return
     */
    @Override
    public NoticeVO getNoticeVO(Notice notice, HttpServletRequest request) {
        // 对象转封装类
        NoticeVO noticeVO = NoticeVO.objToVo(notice);

        // region 可选
        // 1. 关联查询用户信息
        Long userId = notice.getNoticeAdminId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        noticeVO.setUser(userVO);
        return noticeVO;
    }

    /**
     * 分页获取公告封装
     *
     * @param noticePage
     * @param request
     * @return
     */
    @Override
    public Page<NoticeVO> getNoticeVOPage(Page<Notice> noticePage, HttpServletRequest request) {
        List<Notice> noticeList = noticePage.getRecords();
        Page<NoticeVO> noticeVOPage = new Page<>(noticePage.getCurrent(), noticePage.getSize(), noticePage.getTotal());
        if (CollUtil.isEmpty(noticeList)) {
            return noticeVOPage;
        }
        // 对象列表 => 封装对象列表
        List<NoticeVO> noticeVOList = noticeList.stream().map(notice -> {
            return NoticeVO.objToVo(notice);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = noticeList.stream().map(Notice::getNoticeAdminId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        // 填充信息
        noticeVOList.forEach(noticeVO -> {
            Long userId = noticeVO.getNoticeAdminId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            noticeVO.setUser(userService.getUserVO(user));
        });
        // endregion

        noticeVOPage.setRecords(noticeVOList);
        return noticeVOPage;
    }

}
