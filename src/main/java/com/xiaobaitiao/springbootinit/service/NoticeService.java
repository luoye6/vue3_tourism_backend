package com.xiaobaitiao.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaobaitiao.springbootinit.model.dto.notice.NoticeQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.Notice;
import com.xiaobaitiao.springbootinit.model.vo.NoticeVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 公告服务
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
public interface NoticeService extends IService<Notice> {

    /**
     * 校验数据
     *
     * @param notice
     * @param add 对创建的数据进行校验
     */
    void validNotice(Notice notice, boolean add);

    /**
     * 获取查询条件
     *
     * @param noticeQueryRequest
     * @return
     */
    QueryWrapper<Notice> getQueryWrapper(NoticeQueryRequest noticeQueryRequest);
    
    /**
     * 获取公告封装
     *
     * @param notice
     * @param request
     * @return
     */
    NoticeVO getNoticeVO(Notice notice, HttpServletRequest request);

    /**
     * 分页获取公告封装
     *
     * @param noticePage
     * @param request
     * @return
     */
    Page<NoticeVO> getNoticeVOPage(Page<Notice> noticePage, HttpServletRequest request);
}
