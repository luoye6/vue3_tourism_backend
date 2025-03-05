package com.xiaobaitiao.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
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
     * 景点 id
     */
    private Long spotId;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 评分（满分5）
     */
    private Integer score;

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
     * 关联查询用户
     */
    private UserVO userVO;
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
        return spotScoreVO;
    }
}
