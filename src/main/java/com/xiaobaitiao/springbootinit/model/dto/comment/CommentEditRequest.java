package com.xiaobaitiao.springbootinit.model.dto.comment;

import lombok.Data;

import java.io.Serializable;

/**
 * 编辑请求
 *
 * @author <a href="https://github.com/jacffg">码羊</a>
 */
@Data
public class CommentEditRequest implements Serializable {

    /**
     * 评论 ID
     */
    private Long id;


    /**
     * 评论内容
     */
    private String content;



    private static final long serialVersionUID = 1L;
}