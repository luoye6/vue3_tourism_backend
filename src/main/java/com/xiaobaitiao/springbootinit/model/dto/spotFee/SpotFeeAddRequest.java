package com.xiaobaitiao.springbootinit.model.dto.spotFee;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 创建景点门票表请求
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class SpotFeeAddRequest implements Serializable {


    /**
     * 景点id
     */
    private Long spotId;

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


    private static final long serialVersionUID = 1L;
}