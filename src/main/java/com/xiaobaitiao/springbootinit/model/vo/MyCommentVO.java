package com.xiaobaitiao.springbootinit.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName comment
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MyCommentVO implements Serializable {
    /**
     * 评论 ID
     */
    private Long id;

    /**
     * 评论内容
     */
    private String content;


    /**
     * 面经帖子 ID
     */
    private Long postId;
    /**
     * 面经帖子标题
     */
    private String postTitle;
    /**
     * 面经帖子内容
     */
    private String postContent;


    /**
     * 更新时间
     */
    private Date updateTime;


}
