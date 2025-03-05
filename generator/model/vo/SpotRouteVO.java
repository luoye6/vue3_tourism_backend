package com.xiaobaitiao.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
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
     * @param spotRouteVO
     * @return
     */
    public static SpotRoute voToObj(SpotRouteVO spotRouteVO) {
        if (spotRouteVO == null) {
            return null;
        }
        SpotRoute spotRoute = new SpotRoute();
        BeanUtils.copyProperties(spotRouteVO, spotRoute);
        List<String> tagList = spotRouteVO.getTagList();
        spotRoute.setTags(JSONUtil.toJsonStr(tagList));
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
        spotRouteVO.setTagList(JSONUtil.toList(spotRoute.getTags(), String.class));
        return spotRouteVO;
    }
}
