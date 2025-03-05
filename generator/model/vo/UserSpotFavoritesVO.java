package com.xiaobaitiao.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.xiaobaitiao.springbootinit.model.entity.UserSpotFavorites;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户景点收藏关联表视图
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class UserSpotFavoritesVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param userSpotFavoritesVO
     * @return
     */
    public static UserSpotFavorites voToObj(UserSpotFavoritesVO userSpotFavoritesVO) {
        if (userSpotFavoritesVO == null) {
            return null;
        }
        UserSpotFavorites userSpotFavorites = new UserSpotFavorites();
        BeanUtils.copyProperties(userSpotFavoritesVO, userSpotFavorites);
        List<String> tagList = userSpotFavoritesVO.getTagList();
        userSpotFavorites.setTags(JSONUtil.toJsonStr(tagList));
        return userSpotFavorites;
    }

    /**
     * 对象转封装类
     *
     * @param userSpotFavorites
     * @return
     */
    public static UserSpotFavoritesVO objToVo(UserSpotFavorites userSpotFavorites) {
        if (userSpotFavorites == null) {
            return null;
        }
        UserSpotFavoritesVO userSpotFavoritesVO = new UserSpotFavoritesVO();
        BeanUtils.copyProperties(userSpotFavorites, userSpotFavoritesVO);
        userSpotFavoritesVO.setTagList(JSONUtil.toList(userSpotFavorites.getTags(), String.class));
        return userSpotFavoritesVO;
    }
}
