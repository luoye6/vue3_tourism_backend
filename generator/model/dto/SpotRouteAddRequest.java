package com.xiaobaitiao.springbootinit.model.dto.spotRoute;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建景点路线表请求
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class SpotRouteAddRequest implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    private static final long serialVersionUID = 1L;
}