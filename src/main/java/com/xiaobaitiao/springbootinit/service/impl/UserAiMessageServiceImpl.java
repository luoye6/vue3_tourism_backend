package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.UserAiMessageMapper;
import com.xiaobaitiao.springbootinit.model.dto.userAiMessage.UserAiMessageQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.UserAiMessage;
import com.xiaobaitiao.springbootinit.model.vo.UserAiMessageVO;
import com.xiaobaitiao.springbootinit.service.UserAiMessageService;
import com.xiaobaitiao.springbootinit.service.UserService;
import com.xiaobaitiao.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户对话表服务实现
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Service
@Slf4j
public class UserAiMessageServiceImpl extends ServiceImpl<UserAiMessageMapper, UserAiMessage> implements UserAiMessageService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param userAiMessage
     * @param add           对创建的数据进行校验
     */
    @Override
    public void validUserAiMessage(UserAiMessage userAiMessage, boolean add) {
        ThrowUtils.throwIf(userAiMessage == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String userInputText = userAiMessage.getUserInputText();
        String aiGenerateText = userAiMessage.getAiGenerateText();
        Long userId = userAiMessage.getUserId();

        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(userInputText), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(userId < 0, ErrorCode.PARAMS_ERROR);
        }else{
            // 修改数据时，有参数则校验
            // todo 补充校验规则
            ThrowUtils.throwIf(userId == null || userId < 0, ErrorCode.PARAMS_ERROR, "用户 ID 错误");
            ThrowUtils.throwIf(StringUtils.isBlank(aiGenerateText), ErrorCode.PARAMS_ERROR);
        }

    }

    /**
     * 获取查询条件
     *
     * @param userAiMessageQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<UserAiMessage> getQueryWrapper(UserAiMessageQueryRequest userAiMessageQueryRequest) {
        QueryWrapper<UserAiMessage> queryWrapper = new QueryWrapper<>();
        if (userAiMessageQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = userAiMessageQueryRequest.getId();
        String userInputText = userAiMessageQueryRequest.getUserInputText();
        String aiGenerateText = userAiMessageQueryRequest.getAiGenerateText();
        Long userId = userAiMessageQueryRequest.getUserId();
        String sortField = userAiMessageQueryRequest.getSortField();
        String sortOrder = userAiMessageQueryRequest.getSortOrder();

        // todo 补充需要的查询条件
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(userInputText), "userInputText", userInputText);
        queryWrapper.like(StringUtils.isNotBlank(aiGenerateText), "aiGenerateText", aiGenerateText);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取用户对话表封装
     *
     * @param userAiMessage
     * @param request
     * @return
     */
    @Override
    public UserAiMessageVO getUserAiMessageVO(UserAiMessage userAiMessage, HttpServletRequest request) {
        // 对象转封装类
        UserAiMessageVO userAiMessageVO = UserAiMessageVO.objToVo(userAiMessage);
        return userAiMessageVO;
    }

    /**
     * 分页获取用户对话表封装
     *
     * @param userAiMessagePage
     * @param request
     * @return
     */
    @Override
    public Page<UserAiMessageVO> getUserAiMessageVOPage(Page<UserAiMessage> userAiMessagePage, HttpServletRequest request) {
        List<UserAiMessage> userAiMessageList = userAiMessagePage.getRecords();
        Page<UserAiMessageVO> userAiMessageVOPage = new Page<>(userAiMessagePage.getCurrent(), userAiMessagePage.getSize(), userAiMessagePage.getTotal());
        if (CollUtil.isEmpty(userAiMessageList)) {
            return userAiMessageVOPage;
        }
        // 对象列表 => 封装对象列表
        List<UserAiMessageVO> userAiMessageVOList = userAiMessageList.stream().map(UserAiMessageVO::objToVo).collect(Collectors.toList());
        userAiMessageVOPage.setRecords(userAiMessageVOList);
        return userAiMessageVOPage;
    }

}
