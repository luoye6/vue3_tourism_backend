package com.xiaobaitiao.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaobaitiao.springbootinit.model.entity.SpotFee;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
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
     * 景点id
     */
    private Long spotId;
    /**
     * 景点名
     */
    private String spotName;
    /**
     * 管理员ID
     */
    private Long adminId;

    /**
     * 门票描述
     */
    private String spotFeeDescription;

    /**
     * 门票价格
     */
    private BigDecimal spotFeePrice;

    /**
     * 景点门票数量
     */
    private Integer spotFeeNumber;

    /**
     * 门票可用状态 1可用 0不可用 默认 0
     */
    private Integer spotFeeStatus;

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
     * @param spotFeeVO
     * @return
     */
    public static SpotFee voToObj(SpotFeeVO spotFeeVO) {
        if (spotFeeVO == null) {
            return null;
        }
        SpotFee spotFee = new SpotFee();
        BeanUtils.copyProperties(spotFeeVO, spotFee);
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
        return spotFeeVO;
    }
}
