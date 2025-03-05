package com.xiaobaitiao.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.xiaobaitiao.springbootinit.model.entity.SpotOrder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 景点订单表视图
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class SpotOrderVO implements Serializable {

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
     * @param spotOrderVO
     * @return
     */
    public static SpotOrder voToObj(SpotOrderVO spotOrderVO) {
        if (spotOrderVO == null) {
            return null;
        }
        SpotOrder spotOrder = new SpotOrder();
        BeanUtils.copyProperties(spotOrderVO, spotOrder);
        List<String> tagList = spotOrderVO.getTagList();
        spotOrder.setTags(JSONUtil.toJsonStr(tagList));
        return spotOrder;
    }

    /**
     * 对象转封装类
     *
     * @param spotOrder
     * @return
     */
    public static SpotOrderVO objToVo(SpotOrder spotOrder) {
        if (spotOrder == null) {
            return null;
        }
        SpotOrderVO spotOrderVO = new SpotOrderVO();
        BeanUtils.copyProperties(spotOrder, spotOrderVO);
        spotOrderVO.setTagList(JSONUtil.toList(spotOrder.getTags(), String.class));
        return spotOrderVO;
    }
}
