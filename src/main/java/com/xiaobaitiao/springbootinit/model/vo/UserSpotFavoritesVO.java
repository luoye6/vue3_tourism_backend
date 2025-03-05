package com.xiaobaitiao.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
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
     * 景点名称
     */
    private String spotName;
    /**
     * 景点封面
     */
    private String spotAvatar;
    /**
     * 景点坐标
     */
    private String spotLocation;
    /**
     * 收藏量
     */
    private Integer favourNum;

    /**
     * 浏览量
     */
    private Integer viewNum;
    /**
     * 1-正常收藏 0-取消收藏
     */
    private Integer status;

    /**
     * 用户备注
     */
    private String remark;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
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
        return userSpotFavoritesVO;
    }
}
