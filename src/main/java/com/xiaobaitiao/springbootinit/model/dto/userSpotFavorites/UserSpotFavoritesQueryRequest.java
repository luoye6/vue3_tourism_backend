package com.xiaobaitiao.springbootinit.model.dto.userSpotFavorites;

import com.xiaobaitiao.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询用户景点收藏关联表请求
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserSpotFavoritesQueryRequest extends PageRequest implements Serializable {


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