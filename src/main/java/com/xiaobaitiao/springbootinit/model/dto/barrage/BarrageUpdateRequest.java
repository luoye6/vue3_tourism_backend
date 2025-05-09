package com.xiaobaitiao.springbootinit.model.dto.barrage;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新弹幕请求
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class BarrageUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 弹幕文本
     */
    private String message;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 是否精选（默认0，精选为1）
     */
    private Integer isSelected;



    private static final long serialVersionUID = 1L;
}