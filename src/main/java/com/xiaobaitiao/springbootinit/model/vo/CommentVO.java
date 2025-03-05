package com.xiaobaitiao.springbootinit.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaobaitiao.springbootinit.model.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @TableName comment
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentVO implements Serializable {
    /**
     * 评论 ID
     */
    private Long id;

    /**
     * 面经帖子 ID
     */
    private Long postId;

    /**
     * 用户
     */
    private UserVO user;
    /**
     * 被回复用户
     */
    private UserVO repliedUser;
    /**
     * 创建用户 id
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

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 回复评论
     */
    private List<CommentVO> replies;

    /**
     * 封装类转对象
     *
     * @param commentVO
     * @return
     */
    public static Comment voToObj(CommentVO commentVO) {
        if (commentVO == null) {
            return null;
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentVO, comment);
        return comment;
    }

    /**
     * 对象转封装类
     *
     * @param comment
     * @return
     */
    public static CommentVO objToVo(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);

        return commentVO;
    }

}
