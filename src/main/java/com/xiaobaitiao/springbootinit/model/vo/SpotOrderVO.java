package com.xiaobaitiao.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaobaitiao.springbootinit.model.entity.SpotOrder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
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
     * 用户 id
     */
    private Long userId;

    /**
     * 门票id
     */
    private Long spotFeeId;
    /**
     * 景点名称
     */
    private String spotName;
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



    private static final long serialVersionUID = 1L;

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
        return spotOrderVO;
    }
}
