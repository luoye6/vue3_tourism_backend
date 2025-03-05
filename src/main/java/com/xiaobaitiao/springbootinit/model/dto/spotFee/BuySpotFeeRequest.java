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
 * 购买景点门票表请求
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Data
public class BuySpotFeeRequest implements Serializable {
    /**
     * 景点门票 ID
     */
    private Long spotFeeId;

    /**
     * 联系人
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
     * 购买门票的数量
     */
    private Integer spotFeeQuantity;
    private static final long serialVersionUID = 1L;
}