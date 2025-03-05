package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.SpotMapper;
import com.xiaobaitiao.springbootinit.model.dto.spot.SpotQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.vo.SpotVO;
import com.xiaobaitiao.springbootinit.service.SpotService;
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
 * 景点表服务实现
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Service
@Slf4j
public class SpotServiceImpl extends ServiceImpl<SpotMapper, Spot> implements SpotService {
    @Resource
    private SpotMapper spotMapper;
    /**
     * 校验数据
     *
     * @param spot
     * @param add  对创建的数据进行校验
     */
    @Override
    public void validSpot(Spot spot, boolean add) {
        ThrowUtils.throwIf(spot == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String spotName = spot.getSpotName();
        String spotAvatar = spot.getSpotAvatar();
        String spotLocation = spot.getSpotLocation();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(spotName), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(spotAvatar), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(spotLocation), ErrorCode.PARAMS_ERROR);
        }
    }

    /**
     * 获取查询条件
     *
     * @param spotQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Spot> getQueryWrapper(SpotQueryRequest spotQueryRequest) {
        QueryWrapper<Spot> queryWrapper = new QueryWrapper<>();
        if (spotQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = spotQueryRequest.getId();
        Long adminId = spotQueryRequest.getAdminId();
        String spotName = spotQueryRequest.getSpotName();
        String spotLocation = spotQueryRequest.getSpotLocation();
        List<String> spotTagList = spotQueryRequest.getSpotTagList();
        String sortField = spotQueryRequest.getSortField();
        String sortOrder = spotQueryRequest.getSortOrder();
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(spotName), "spotName", spotName);
        queryWrapper.like(StringUtils.isNotBlank(spotLocation), "spotLocation", spotLocation);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(spotTagList)) {
            for (String tag : spotTagList) {
                queryWrapper.like("spotTags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(adminId), "adminId", adminId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取景点表封装
     *
     * @param spot
     * @param request
     * @return
     */
    @Override
    public SpotVO getSpotVO(Spot spot, HttpServletRequest request) {
        // 对象转封装类
        SpotVO spotVO = SpotVO.objToVo(spot);
        return spotVO;
    }

    /**
     * 分页获取景点表封装
     *
     * @param spotPage
     * @param request
     * @return
     */
    @Override
    public Page<SpotVO> getSpotVOPage(Page<Spot> spotPage, HttpServletRequest request) {
        List<Spot> spotList = spotPage.getRecords();
        Page<SpotVO> spotVOPage = new Page<>(spotPage.getCurrent(), spotPage.getSize(), spotPage.getTotal());
        if (CollUtil.isEmpty(spotList)) {
            return spotVOPage;
        }
        // 对象列表 => 封装对象列表
        List<SpotVO> spotVOList = spotList.stream().map(SpotVO::objToVo).collect(Collectors.toList());
        spotVOPage.setRecords(spotVOList);
        return spotVOPage;
    }


    @Override
    public List<Spot> getTop10SpotsByViews() {
        return spotMapper.selectTop10SpotsByViews();
    }

}
