package com.xiaobaitiao.springbootinit.model.dto.spot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 编辑景点表请求
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class SpotEditRequest implements Serializable {
    /**
     * id
     */
    private Long id;
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
    /**
     * 收藏量
     */
    private Integer favourNum;

    /**
     * 浏览量
     */
    private Integer viewNum;

    /**
     * 景点状态（1开放，0关闭，默认关闭）
     */
    private Integer spotStatus;




    private static final long serialVersionUID = 1L;
}