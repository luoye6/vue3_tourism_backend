package com.xiaobaitiao.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.xiaobaitiao.springbootinit.model.entity.SpotScore;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 景点评分表视图
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class SpotScoreVO implements Serializable {

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
     * @param spotScoreVO
     * @return
     */
    public static SpotScore voToObj(SpotScoreVO spotScoreVO) {
        if (spotScoreVO == null) {
            return null;
        }
        SpotScore spotScore = new SpotScore();
        BeanUtils.copyProperties(spotScoreVO, spotScore);
        List<String> tagList = spotScoreVO.getTagList();
        spotScore.setTags(JSONUtil.toJsonStr(tagList));
        return spotScore;
    }

    /**
     * 对象转封装类
     *
     * @param spotScore
     * @return
     */
    public static SpotScoreVO objToVo(SpotScore spotScore) {
        if (spotScore == null) {
            return null;
        }
        SpotScoreVO spotScoreVO = new SpotScoreVO();
        BeanUtils.copyProperties(spotScore, spotScoreVO);
        spotScoreVO.setTagList(JSONUtil.toList(spotScore.getTags(), String.class));
        return spotScoreVO;
    }
}
