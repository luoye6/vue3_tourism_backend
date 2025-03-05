package com.xiaobaitiao.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaobaitiao.springbootinit.model.entity.SpotRoute;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 景点路线表视图
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class SpotRouteVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 景点 id（字符串数组，用逗号分割，顺序从前往后)
     */
    private List<String> spotIdList;
    /**
     * 景点名称列表
     */
    private List<String> spotNameList;
    /**
     * 景点距离列表
     */
    private List<Double> spotDistanceList;
    /**
     * adminId
     */
    private Long adminId;
    /**
     * 路线封面图
     */
    private String spotRouteAvatar;

    /**
     * 路线描述
     */
    private String spotRouteDescription;

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

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    /**
     * 封装类转对象
     *
     * @param spotRouteVO
     * @return
     */
    public static SpotRoute voToObj(SpotRouteVO spotRouteVO) {
        if (spotRouteVO == null) {
            return null;
        }
        SpotRoute spotRoute = new SpotRoute();
        BeanUtils.copyProperties(spotRouteVO, spotRoute);
        List<String> tagList = spotRouteVO.getSpotIdList();
        spotRoute.setSpotIds(JSONUtil.toJsonStr(tagList));
        return spotRoute;
    }

    /**
     * 对象转封装类
     *
     * @param spotRoute
     * @return
     */
    public static SpotRouteVO objToVo(SpotRoute spotRoute) {
        if (spotRoute == null) {
            return null;
        }
        SpotRouteVO spotRouteVO = new SpotRouteVO();
        BeanUtils.copyProperties(spotRoute, spotRouteVO);
        spotRouteVO.setSpotIdList(JSONUtil.toList(spotRoute.getSpotIds(), String.class));
        return spotRouteVO;
    }
}
