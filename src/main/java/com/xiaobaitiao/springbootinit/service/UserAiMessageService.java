package com.xiaobaitiao.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaobaitiao.springbootinit.model.dto.userAiMessage.UserAiMessageQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.UserAiMessage;
import com.xiaobaitiao.springbootinit.model.vo.UserAiMessageVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户对话表服务
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
public interface UserAiMessageService extends IService<UserAiMessage> {

    /**
     * 校验数据
     *
     * @param userAiMessage
     * @param add 对创建的数据进行校验
     */
    void validUserAiMessage(UserAiMessage userAiMessage, boolean add);

    /**
     * 获取查询条件
     *
     * @param userAiMessageQueryRequest
     * @return
     */
    QueryWrapper<UserAiMessage> getQueryWrapper(UserAiMessageQueryRequest userAiMessageQueryRequest);
    
    /**
     * 获取用户对话表封装
     *
     * @param userAiMessage
     * @param request
     * @return
     */
    UserAiMessageVO getUserAiMessageVO(UserAiMessage userAiMessage, HttpServletRequest request);

    /**
     * 分页获取用户对话表封装
     *
     * @param userAiMessagePage
     * @param request
     * @return
     */
    Page<UserAiMessageVO> getUserAiMessageVOPage(Page<UserAiMessage> userAiMessagePage, HttpServletRequest request);

}
