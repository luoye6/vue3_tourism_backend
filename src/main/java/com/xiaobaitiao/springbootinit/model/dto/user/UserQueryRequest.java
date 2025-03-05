package com.xiaobaitiao.springbootinit.model.dto.user;

import com.xiaobaitiao.springbootinit.common.PageRequest;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询请求
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 开放平台id
     */
    private String unionId;

    /**
     * 公众号openId
     */
    private String mpOpenId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 简介
     */
    private String userProfile;
    /**
     * 联系方式
     */
    private String userPhone;
    /**
     * 用户余额
     */
    private BigDecimal balance;
    /**
     * 用户 AI 剩余可使用次数
     */
    private Integer aiRemainNumber;
    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}