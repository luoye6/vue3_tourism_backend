package com.xiaobaitiao.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 景点表视图
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class SpotVO implements Serializable {

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
     * @param spotVO
     * @return
     */
    public static Spot voToObj(SpotVO spotVO) {
        if (spotVO == null) {
            return null;
        }
        Spot spot = new Spot();
        BeanUtils.copyProperties(spotVO, spot);
        List<String> tagList = spotVO.getTagList();
        spot.setTags(JSONUtil.toJsonStr(tagList));
        return spot;
    }

    /**
     * 对象转封装类
     *
     * @param spot
     * @return
     */
    public static SpotVO objToVo(Spot spot) {
        if (spot == null) {
            return null;
        }
        SpotVO spotVO = new SpotVO();
        BeanUtils.copyProperties(spot, spotVO);
        spotVO.setTagList(JSONUtil.toList(spot.getTags(), String.class));
        return spotVO;
    }
}
