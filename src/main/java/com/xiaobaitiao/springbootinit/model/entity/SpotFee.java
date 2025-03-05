package com.xiaobaitiao.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName spot_fee
 */
@TableName(value ="spot_fee")
@Data
public class SpotFee implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}