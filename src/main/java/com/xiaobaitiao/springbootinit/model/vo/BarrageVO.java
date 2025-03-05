package com.xiaobaitiao.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.xiaobaitiao.springbootinit.model.entity.Barrage;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 弹幕视图
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class BarrageVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 弹幕文本
     */
    private String message;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 是否精选（默认0，精选为1）
     */
    private Integer isSelected;

    private static final long serialVersionUID = 1L;

    /**
     * 封装类转对象
     *
     * @param barrageVO
     * @return
     */
    public static Barrage voToObj(BarrageVO barrageVO) {
        if (barrageVO == null) {
            return null;
        }
        Barrage barrage = new Barrage();
        BeanUtils.copyProperties(barrageVO, barrage);
        return barrage;
    }

    /**
     * 对象转封装类
     *
     * @param barrage
     * @return
     */
    public static BarrageVO objToVo(Barrage barrage) {
        if (barrage == null) {
            return null;
        }
        BarrageVO barrageVO = new BarrageVO();
        BeanUtils.copyProperties(barrage, barrageVO);
        return barrageVO;
    }
}
