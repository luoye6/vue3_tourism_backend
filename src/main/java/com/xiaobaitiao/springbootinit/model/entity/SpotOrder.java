package com.xiaobaitiao.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName spot_order
 */
@TableName(value ="spot_order")
@Data
public class SpotOrder implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;



    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 门票id
     */
    private Long spotFeeId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 联系电话
     */
    private String userPhone;

    /**
     * 支付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 支付状态（1已支付，0未支付）默认0
     */
    private Integer payStatus;

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