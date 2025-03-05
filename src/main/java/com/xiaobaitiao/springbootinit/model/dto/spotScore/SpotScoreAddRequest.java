package com.xiaobaitiao.springbootinit.model.dto.spotScore;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建景点评分表请求
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class SpotScoreAddRequest implements Serializable {

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