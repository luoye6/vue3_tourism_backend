package com.xiaobaitiao.springbootinit.model.dto.spot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建景点表请求
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class SpotAddRequest implements Serializable {
    /**
     * adminId
     */
    private Long adminId;
    /**
     * 景点名
     */
    private String spotName;

    /**
     * 景点封面图
     */
    private String spotAvatar;

    /**
     * 景点所在地
     */
    private String spotLocation;
    /**
     * 景点介绍
     */
    private String spotDescription;
    /**
     * 景点标签（JSON字符串数组）
     */
    private List<String> spotTagList;
    private static final long serialVersionUID = 1L;
}