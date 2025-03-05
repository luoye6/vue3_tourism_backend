package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.SpotRouteMapper;
import com.xiaobaitiao.springbootinit.model.dto.spotRoute.SpotRouteQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.entity.SpotRoute;
import com.xiaobaitiao.springbootinit.model.vo.SpotRouteVO;
import com.xiaobaitiao.springbootinit.service.SpotRouteService;
import com.xiaobaitiao.springbootinit.service.SpotService;
import com.xiaobaitiao.springbootinit.utils.PositionUtil;
import com.xiaobaitiao.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 景点路线表服务实现
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Service
@Slf4j
public class SpotRouteServiceImpl extends ServiceImpl<SpotRouteMapper, SpotRoute> implements SpotRouteService {


    @Resource
    private SpotService spotService;

    /**
     * 校验数据
     *
     * @param spotRoute
     * @param add       对创建的数据进行校验
     */
    @Override
    public void validSpotRoute(SpotRoute spotRoute, boolean add) {
        ThrowUtils.throwIf(spotRoute == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String spotIds = spotRoute.getSpotIds();
        String spotRouteAvatar = spotRoute.getSpotRouteAvatar();
        String spotRouteDescription = spotRoute.getSpotRouteDescription();
        // 创建数据时，参数不能为空
        ThrowUtils.throwIf(StringUtils.isBlank(spotIds), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(StringUtils.isBlank(spotRouteAvatar), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(StringUtils.isBlank(spotRouteDescription), ErrorCode.PARAMS_ERROR);
    }

    /**
     * 获取查询条件
     *
     * @param spotRouteQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<SpotRoute> getQueryWrapper(SpotRouteQueryRequest spotRouteQueryRequest) {
        QueryWrapper<SpotRoute> queryWrapper = new QueryWrapper<>();
        if (spotRouteQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = spotRouteQueryRequest.getId();
        Long adminId = spotRouteQueryRequest.getAdminId();
        List<String> spotIdList = spotRouteQueryRequest.getSpotIdList();
        String spotRouteDescription = spotRouteQueryRequest.getSpotRouteDescription();
        String sortField = spotRouteQueryRequest.getSortField();
        String sortOrder = spotRouteQueryRequest.getSortOrder();
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(spotRouteDescription), "spotRouteDescription", spotRouteDescription);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(spotIdList)) {
            for (String tag : spotIdList) {
                queryWrapper.like("spotIds", "\"" + tag + "\"");
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
     * 获取景点路线表封装
     *
     * @param spotRoute 景点路线实体
     * @param request   HTTP请求
     * @return 返回封装后的 SpotRouteVO
     */
    @Override
    public SpotRouteVO getSpotRouteVO(SpotRoute spotRoute, HttpServletRequest request) {
        // 对象转封装类
        SpotRouteVO spotRouteVO = SpotRouteVO.objToVo(spotRoute);

        // 获取景点ID列表
        List<String> spotIdList = spotRouteVO.getSpotIdList();
        if (CollUtil.isNotEmpty(spotIdList)) {
            // 查询景点名称列表
            List<String> spotNameList = spotIdList.stream()
                    .map(spotId -> {
                        Spot spot = spotService.getById(spotId);
                        return spot != null ? spot.getSpotName() : "未知景点";
                    })
                    .collect(Collectors.toList());
            spotRouteVO.setSpotNameList(spotNameList);

            // 计算景点距离列表
            List<Double> spotDistanceList = new ArrayList<>();
            for (int i = 0; i < spotIdList.size() - 1; i++) {
                String currentSpotId = spotIdList.get(i);
                String nextSpotId = spotIdList.get(i + 1);

                Spot currentSpot = spotService.getById(currentSpotId);
                Spot nextSpot = spotService.getById(nextSpotId);

                if (currentSpot != null && nextSpot != null) {
                    // 解析经纬度
                    double[] currentCoords = parseLocation(currentSpot.getSpotLocation());
                    double[] nextCoords = parseLocation(nextSpot.getSpotLocation());

                    if (currentCoords != null && nextCoords != null) {
                        double distance = PositionUtil.getDistance(
                                currentCoords[1], currentCoords[0], // 当前景点的经度、纬度
                                nextCoords[1], nextCoords[0]        // 下一个景点的经度、纬度
                        );
                        spotDistanceList.add(distance);
                    } else {
                        spotDistanceList.add(0.0); // 无法解析经纬度时，默认距离为0
                    }
                } else {
                    spotDistanceList.add(0.0); // 景点不存在时，默认距离为0
                }
            }
            spotRouteVO.setSpotDistanceList(spotDistanceList);
        }

        return spotRouteVO;
    }
    /**
     * 分页获取景点路线表封装
     *
     * @param spotRoutePage
     * @param request
     * @return
     */
    @Override
    public Page<SpotRouteVO> getSpotRouteVOPage(Page<SpotRoute> spotRoutePage, HttpServletRequest request) {
        List<SpotRoute> spotRouteList = spotRoutePage.getRecords();
        Page<SpotRouteVO> spotRouteVOPage = new Page<>(spotRoutePage.getCurrent(), spotRoutePage.getSize(), spotRoutePage.getTotal());
        if (CollUtil.isEmpty(spotRouteList)) {
            return spotRouteVOPage;
        }

        // 对象列表 => 封装对象列表
        List<SpotRouteVO> spotRouteVOList = spotRouteList.stream().map(spotRoute -> {
            SpotRouteVO spotRouteVO = SpotRouteVO.objToVo(spotRoute);

            // 获取景点ID列表
            List<String> spotIdList = spotRouteVO.getSpotIdList();
            if (CollUtil.isNotEmpty(spotIdList)) {
                // 查询景点名称列表
                List<String> spotNameList = spotIdList.stream()
                        .map(spotId -> {
                            Spot spot = spotService.getById(spotId);
                            return spot != null ? spot.getSpotName() : "未知景点";
                        })
                        .collect(Collectors.toList());
                spotRouteVO.setSpotNameList(spotNameList);

                // 计算景点距离列表
                List<Double> spotDistanceList = new ArrayList<>();
                for (int i = 0; i < spotIdList.size() - 1; i++) {
                    String currentSpotId = spotIdList.get(i);
                    String nextSpotId = spotIdList.get(i + 1);

                    Spot currentSpot = spotService.getById(currentSpotId);
                    Spot nextSpot = spotService.getById(nextSpotId);

                    if (currentSpot != null && nextSpot != null) {
                        // 解析经纬度
                        double[] currentCoords = parseLocation(currentSpot.getSpotLocation());
                        double[] nextCoords = parseLocation(nextSpot.getSpotLocation());

                        if (currentCoords != null && nextCoords != null) {
                            double distance = PositionUtil.getDistance(
                                    currentCoords[1], currentCoords[0], // 当前景点的经度、纬度
                                    nextCoords[1], nextCoords[0]        // 下一个景点的经度、纬度
                            );
                            spotDistanceList.add(distance);
                        } else {
                            spotDistanceList.add(0.0); // 无法解析经纬度时，默认距离为0
                        }
                    } else {
                        spotDistanceList.add(0.0); // 景点不存在时，默认距离为0
                    }
                }
                spotRouteVO.setSpotDistanceList(spotDistanceList);
            }

            return spotRouteVO;
        }).collect(Collectors.toList());

        spotRouteVOPage.setRecords(spotRouteVOList);
        return spotRouteVOPage;
    }

    /**
     * 解析经纬度字符串，返回 [纬度, 经度] 数组
     *
     * @param location 经纬度字符串，格式如 "30.1670° N, 118.0500° E"
     * @return 返回 [纬度, 经度] 数组，解析失败返回 null
     */
    private double[] parseLocation(String location) {
        if (StrUtil.isBlank(location)) {
            return null;
        }
        try {
            String[] parts = location.split(",");
            if (parts.length != 2) {
                return null;
            }
            double latitude = parseCoordinate(parts[0].trim());
            double longitude = parseCoordinate(parts[1].trim());
            return new double[]{latitude, longitude};
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析单个坐标值，如 "30.1670° N"
     *
     * @param coordinate 坐标字符串
     * @return 返回解析后的数值
     */
    private double parseCoordinate(String coordinate) {
        if (StrUtil.isBlank(coordinate)) {
            throw new IllegalArgumentException("坐标字符串为空");
        }
        String[] parts = coordinate.split("°");
        if (parts.length != 2) {
            throw new IllegalArgumentException("坐标格式错误");
        }
        double value = Double.parseDouble(parts[0].trim());
        String direction = parts[1].trim();
        if ("S".equalsIgnoreCase(direction) || "W".equalsIgnoreCase(direction)) {
            value = -value; // 南纬和西经为负数
        }
        return value;
    }

}
