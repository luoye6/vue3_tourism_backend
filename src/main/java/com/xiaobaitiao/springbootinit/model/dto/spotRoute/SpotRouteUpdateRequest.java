package com.xiaobaitiao.springbootinit.model.dto.spotRoute;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新景点路线表请求
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class SpotRouteUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * adminId
     */
    private Long adminId;
    /**
     * 景点 id（字符串数组，用逗号分割，顺序从前往后)
     */
    private List<String> spotIdList;

    /**
     * 路线封面图
     */
    private String spotRouteAvatar;

    /**
     * 路线描述
     */
    private String spotRouteDescription;



    private static final long serialVersionUID = 1L;
}