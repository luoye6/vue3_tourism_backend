package com.xiaobaitiao.springbootinit.model.dto.comment;

import com.xiaobaitiao.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/jacffg">码羊</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentQueryRequest extends PageRequest implements Serializable {

    /**
     * 评论 ID
     */
    private Long id;


    /**
     * 面经帖子 ID
     */
    private Long postId;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 父评论 ID，支持多级嵌套回复
     */
    private Long parentId;
    /**
     * 顶级父 ID，支持多级嵌套回复
     */
    private Long ancestorId;


    private static final long serialVersionUID = 1L;
}