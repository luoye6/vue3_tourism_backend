package com.xiaobaitiao.springbootinit.controller;

import cn.hutool.core.date.StopWatch;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaobaitiao.springbootinit.annotation.AuthCheck;
import com.xiaobaitiao.springbootinit.common.BaseResponse;
import com.xiaobaitiao.springbootinit.common.DeleteRequest;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.common.ResultUtils;
import com.xiaobaitiao.springbootinit.constant.UserConstant;
import com.xiaobaitiao.springbootinit.exception.BusinessException;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.manager.SparkClient;
import com.xiaobaitiao.springbootinit.manager.model.SparkMessage;
import com.xiaobaitiao.springbootinit.manager.model.SparkSyncChatResponse;
import com.xiaobaitiao.springbootinit.manager.model.request.SparkRequest;
import com.xiaobaitiao.springbootinit.manager.model.response.SparkTextUsage;
import com.xiaobaitiao.springbootinit.model.dto.userAiMessage.UserAiMessageAddRequest;
import com.xiaobaitiao.springbootinit.model.dto.userAiMessage.UserAiMessageEditRequest;
import com.xiaobaitiao.springbootinit.model.dto.userAiMessage.UserAiMessageQueryRequest;
import com.xiaobaitiao.springbootinit.model.dto.userAiMessage.UserAiMessageUpdateRequest;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.entity.UserAiMessage;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.UserAiMessageVO;
import com.xiaobaitiao.springbootinit.service.SpotService;
import com.xiaobaitiao.springbootinit.service.UserAiMessageService;
import com.xiaobaitiao.springbootinit.service.UserService;
import com.xiaobaitiao.springbootinit.utils.WordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 用户对话表接口
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@RestController
@RequestMapping("/userAiMessage")
@Slf4j
public class UserAiMessageController {

    @Resource
    private UserAiMessageService userAiMessageService;
    @Resource
    private SpotService spotService;
    @Resource
    private UserService userService;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    SparkClient sparkClient = new SparkClient();

    {
        sparkClient.appid = "xxxxxxxxxxxxxxxxxxxxxx";
        sparkClient.apiKey = "xxxxxxxxxxxxxxxxxxxxxx";
        sparkClient.apiSecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    }

    // region 增删改查

    /**
     * 创建用户对话表
     *
     * @param userAiMessageAddRequest
     * @param request
     * @return UserAiMessage
     */
    @PostMapping("/add")
    public BaseResponse<UserAiMessage> addUserAiMessage(@RequestBody UserAiMessageAddRequest userAiMessageAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userAiMessageAddRequest == null, ErrorCode.PARAMS_ERROR);
        String userInputText = userAiMessageAddRequest.getUserInputText();
        if (WordUtils.containsForbiddenWords(userInputText)){
            ThrowUtils.throwIf(WordUtils.containsForbiddenWords(userInputText), ErrorCode.WORD_FORBIDDEN_ERROR, "包含违禁词");
        }
        UserAiMessage userAiMessage = new UserAiMessage();
        // 填充默认值
        User loginUser = userService.getLoginUser(request);
        Integer aiRemainNumber = loginUser.getAiRemainNumber();
        // 检查用户余额是否足够，如果不足，直接返回错误信息
        ThrowUtils.throwIf(aiRemainNumber <= 0, ErrorCode.USER_BALANCE_NOT_ENOUGH);
        userAiMessage.setUserId(loginUser.getId());
        userAiMessage.setUserInputText(userInputText);
        StringBuilder presetInformation = new StringBuilder("你是一个旅游景点的推荐官，请根据用户的喜好信息，从数据库中选择某几个景点，最多选择三个景点，给用户概述（注意简短），并给他做一个路线的推荐。\n" +
                "以下是数据库里面包含的景点：");
        List<String> spotNameList = spotService.list().stream().map(Spot::getSpotName).collect(Collectors.toList());
        for (String s : spotNameList) {
            presetInformation.append(s).append(" ");
        }
        presetInformation.append("\n用户喜好信息如下：");
        List<SparkMessage> messages = new ArrayList<>();
        messages.add(SparkMessage.userContent(presetInformation+userInputText));
        String response = "";
        int timeout = 40; // 超时时间，单位为秒
        // 构造请求
        SparkRequest sparkRequest = SparkRequest.builder()
                // 模型回答的tokens的最大长度,非必传，默认为2048。
                // V1.5取值为[1,4096]
                // V2.0取值为[1,8192]
                // V3.0取值为[1,8192]
                .maxTokens(2048)
                .messages(messages)
                // 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高 非必传,取值为[0,1],默认为0.5
                .temperature(0.2)
                .build();
        Future<String> future = threadPoolExecutor.submit(() -> {
            try {
                // 同步调用
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
                SparkTextUsage textUsage = chatResponse.getTextUsage();
                stopWatch.stop();
                long total = stopWatch.getTotal(TimeUnit.SECONDS);
                System.out.println("本次接口调用耗时:" + total + "秒");
                System.out.println("\n回答：" + chatResponse.getContent());
                System.out.println("\n提问tokens：" + textUsage.getPromptTokens()
                        + "，回答tokens：" + textUsage.getCompletionTokens()
                        + "，总消耗tokens：" + textUsage.getTotalTokens());
                return chatResponse.getContent();
//                return AlibabaAIModel.doChatWithHistory(stringBuilder.toString(),recentHistory);
            } catch (Exception exception) {
                throw new RuntimeException("遇到异常");
            }
        });
        try {
            response = future.get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.info("服务器接口调用超时");
        }
        System.out.println(response);
        userAiMessageAddRequest.setAiGenerateText(response);

        // 复制属性
        BeanUtils.copyProperties(userAiMessageAddRequest, userAiMessage);

        // 校验数据
        userAiMessageService.validUserAiMessage(userAiMessage, true);


        // 插入数据库
        boolean result = userAiMessageService.save(userAiMessage);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 更新用户 AI 调用余额
        loginUser.setAiRemainNumber(aiRemainNumber-1);
        boolean update = userService.updateById(loginUser);
        ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR);
        long newUserAiMessageId = userAiMessage.getId();
        UserAiMessage generateAnswer = userAiMessageService.getById(newUserAiMessageId);
        return ResultUtils.success(generateAnswer);
    }


    /**
     * 删除用户对话表
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUserAiMessage(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserAiMessage oldUserAiMessage = userAiMessageService.getById(id);
        ThrowUtils.throwIf(oldUserAiMessage == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldUserAiMessage.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = userAiMessageService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新用户对话表（仅管理员可用）
     *
     * @param userAiMessageUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserAiMessage(@RequestBody UserAiMessageUpdateRequest userAiMessageUpdateRequest) {
        if (userAiMessageUpdateRequest == null || userAiMessageUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        UserAiMessage userAiMessage = new UserAiMessage();
        BeanUtils.copyProperties(userAiMessageUpdateRequest, userAiMessage);
        // 数据校验
        userAiMessageService.validUserAiMessage(userAiMessage, false);
        // 判断是否存在
        long id = userAiMessageUpdateRequest.getId();
        UserAiMessage oldUserAiMessage = userAiMessageService.getById(id);
        ThrowUtils.throwIf(oldUserAiMessage == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = userAiMessageService.updateById(userAiMessage);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户对话表（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserAiMessageVO> getUserAiMessageVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        UserAiMessage userAiMessage = userAiMessageService.getById(id);
        ThrowUtils.throwIf(userAiMessage == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(userAiMessageService.getUserAiMessageVO(userAiMessage, request));
    }

    /**
     * 分页获取用户对话表列表（仅管理员可用）
     *
     * @param userAiMessageQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserAiMessage>> listUserAiMessageByPage(@RequestBody UserAiMessageQueryRequest userAiMessageQueryRequest) {
        long current = userAiMessageQueryRequest.getCurrent();
        long size = userAiMessageQueryRequest.getPageSize();
        // 查询数据库
        Page<UserAiMessage> userAiMessagePage = userAiMessageService.page(new Page<>(current, size),
                userAiMessageService.getQueryWrapper(userAiMessageQueryRequest));
        return ResultUtils.success(userAiMessagePage);
    }

    /**
     * 分页获取用户对话表列表（封装类）
     *
     * @param userAiMessageQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserAiMessageVO>> listUserAiMessageVOByPage(@RequestBody UserAiMessageQueryRequest userAiMessageQueryRequest,
                                                                         HttpServletRequest request) {
        long current = userAiMessageQueryRequest.getCurrent();
        long size = userAiMessageQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<UserAiMessage> userAiMessagePage = userAiMessageService.page(new Page<>(current, size),
                userAiMessageService.getQueryWrapper(userAiMessageQueryRequest));
        // 获取封装类
        return ResultUtils.success(userAiMessageService.getUserAiMessageVOPage(userAiMessagePage, request));
    }

    /**
     * 分页获取当前登录用户创建的用户对话表列表
     *
     * @param userAiMessageQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<UserAiMessageVO>> listMyUserAiMessageVOByPage(@RequestBody UserAiMessageQueryRequest userAiMessageQueryRequest,
                                                                           HttpServletRequest request) {
        ThrowUtils.throwIf(userAiMessageQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        userAiMessageQueryRequest.setUserId(loginUser.getId());
        long current = userAiMessageQueryRequest.getCurrent();
        long size = userAiMessageQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<UserAiMessage> userAiMessagePage = userAiMessageService.page(new Page<>(current, size),
                userAiMessageService.getQueryWrapper(userAiMessageQueryRequest));
        // 获取封装类
        return ResultUtils.success(userAiMessageService.getUserAiMessageVOPage(userAiMessagePage, request));
    }

    /**
     * 编辑用户对话表（给用户使用）
     *
     * @param userAiMessageEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editUserAiMessage(@RequestBody UserAiMessageEditRequest userAiMessageEditRequest, HttpServletRequest request) {
        if (userAiMessageEditRequest == null || userAiMessageEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        UserAiMessage userAiMessage = new UserAiMessage();
        BeanUtils.copyProperties(userAiMessageEditRequest, userAiMessage);
        // 数据校验
        userAiMessageService.validUserAiMessage(userAiMessage, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = userAiMessageEditRequest.getId();
        UserAiMessage oldUserAiMessage = userAiMessageService.getById(id);
        ThrowUtils.throwIf(oldUserAiMessage == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldUserAiMessage.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = userAiMessageService.updateById(userAiMessage);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }


}
