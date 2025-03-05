package com.xiaobaitiao.springbootinit.model.dto.userSpotFavorites;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新用户景点收藏关联表请求
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class UserSpotFavoritesUpdateRequest implements Serializable {


    /**
     *
     */
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 景点 ID
     */
    private Long spotId;

    /**
     * 1-正常收藏 0-取消收藏
     */
    private Integer status;

    /**
     * 用户备注
     */
    private String remark;



    private static final long serialVersionUID = 1L;
}