package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.BarrageMapper;
import com.xiaobaitiao.springbootinit.model.dto.barrage.BarrageQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.Barrage;
import com.xiaobaitiao.springbootinit.model.vo.BarrageVO;
import com.xiaobaitiao.springbootinit.service.BarrageService;
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
 * 弹幕服务实现
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Service
@Slf4j
public class BarrageServiceImpl extends ServiceImpl<BarrageMapper, Barrage> implements BarrageService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param barrage
     * @param add     对创建的数据进行校验
     */
    @Override
    public void validBarrage(Barrage barrage, boolean add) {
        ThrowUtils.throwIf(barrage == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String message = barrage.getMessage();
        String userAvatar = barrage.getUserAvatar();
        Long userId = barrage.getUserId();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(message), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(userAvatar), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(userId < 0, ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        // todo 补充校验规则
        if (StringUtils.isNotBlank(message)) {
            ThrowUtils.throwIf(message.length() > 50, ErrorCode.PARAMS_ERROR, "弹幕消息过长");
        }
    }

    /**
     * 获取查询条件
     *
     * @param barrageQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Barrage> getQueryWrapper(BarrageQueryRequest barrageQueryRequest) {
        QueryWrapper<Barrage> queryWrapper = new QueryWrapper<>();
        if (barrageQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        String message = barrageQueryRequest.getMessage();
        Long userId = barrageQueryRequest.getUserId();
        Integer isSelected = barrageQueryRequest.getIsSelected();
        String sortField = barrageQueryRequest.getSortField();
        String sortOrder = barrageQueryRequest.getSortOrder();
        // todo 补充需要的查询条件
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(message), "message", message);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(isSelected), "isSelected", isSelected);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取弹幕封装
     *
     * @param barrage
     * @param request
     * @return
     */
    @Override
    public BarrageVO getBarrageVO(Barrage barrage, HttpServletRequest request) {
        // 对象转封装类
        return BarrageVO.objToVo(barrage);
    }

    /**
     * 分页获取弹幕封装
     *
     * @param barragePage
     * @param request
     * @return
     */
    @Override
    public Page<BarrageVO> getBarrageVOPage(Page<Barrage> barragePage, HttpServletRequest request) {
        List<Barrage> barrageList = barragePage.getRecords();
        Page<BarrageVO> barrageVOPage = new Page<>(barragePage.getCurrent(), barragePage.getSize(), barragePage.getTotal());
        if (CollUtil.isEmpty(barrageList)) {
            return barrageVOPage;
        }
        // 对象列表 => 封装对象列表
        List<BarrageVO> barrageVOList = barrageList.stream().map(BarrageVO::objToVo).collect(Collectors.toList());
        barrageVOPage.setRecords(barrageVOList);
        return barrageVOPage;
    }

}
