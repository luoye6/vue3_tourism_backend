package com.xiaobaitiao.springbootinit.model.dto.spotScore;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新景点评分表请求
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class SpotScoreUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 景点 id
     */
    private Long spotId;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 评分（满分5）
     */
    private Integer score;


    private static final long serialVersionUID = 1L;
}