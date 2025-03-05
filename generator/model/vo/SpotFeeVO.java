package com.xiaobaitiao.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.xiaobaitiao.springbootinit.model.entity.SpotFee;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 景点门票表视图
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class SpotFeeVO implements Serializable {

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
     * @param spotFeeVO
     * @return
     */
    public static SpotFee voToObj(SpotFeeVO spotFeeVO) {
        if (spotFeeVO == null) {
            return null;
        }
        SpotFee spotFee = new SpotFee();
        BeanUtils.copyProperties(spotFeeVO, spotFee);
        List<String> tagList = spotFeeVO.getTagList();
        spotFee.setTags(JSONUtil.toJsonStr(tagList));
        return spotFee;
    }

    /**
     * 对象转封装类
     *
     * @param spotFee
     * @return
     */
    public static SpotFeeVO objToVo(SpotFee spotFee) {
        if (spotFee == null) {
            return null;
        }
        SpotFeeVO spotFeeVO = new SpotFeeVO();
        BeanUtils.copyProperties(spotFee, spotFeeVO);
        spotFeeVO.setTagList(JSONUtil.toList(spotFee.getTags(), String.class));
        return spotFeeVO;
    }
}
