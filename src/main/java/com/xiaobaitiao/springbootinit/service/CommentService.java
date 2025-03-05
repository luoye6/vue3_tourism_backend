package com.xiaobaitiao.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaobaitiao.springbootinit.model.dto.comment.CommentQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.Comment;
import com.xiaobaitiao.springbootinit.model.vo.CommentVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子评论服务
 *
 */
public interface CommentService extends IService<Comment> {

    /**
     * 校验数据
     *
     * @param comment
     * @param add 对创建的数据进行校验
     */
    void validComment(Comment comment, boolean add);

    /**
     * 获取查询条件
     *
     * @param commentQueryRequest
     * @return
     */
    QueryWrapper<Comment> getQueryWrapper(CommentQueryRequest commentQueryRequest);
    
    /**
     * 获取帖子评论封装
     *
     * @param comment
     * @param request
     * @return
     */
    CommentVO getCommentVO(Comment comment, HttpServletRequest request);

    /**
     * 分页获取帖子评论封装
     *
     * @param commentPage
     * @param request
     * @return
     */
    Page<CommentVO> getCommentVOPage(Page<Comment> commentPage, HttpServletRequest request);

    /**
     * 根据帖子Id获取评论
     *
     * @param postId
     * @param request
     * @return
     */

     List<CommentVO> getCommentsByPostId(long postId, HttpServletRequest request) ;
}
