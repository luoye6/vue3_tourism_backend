package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.UserSpotFavoritesMapper;
import com.xiaobaitiao.springbootinit.model.dto.userSpotFavorites.UserSpotFavoritesQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.entity.UserSpotFavorites;
import com.xiaobaitiao.springbootinit.model.vo.UserSpotFavoritesVO;
import com.xiaobaitiao.springbootinit.service.SpotService;
import com.xiaobaitiao.springbootinit.service.UserService;
import com.xiaobaitiao.springbootinit.service.UserSpotFavoritesService;
import com.xiaobaitiao.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户景点收藏关联表服务实现
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Service
@Slf4j
public class UserSpotFavoritesServiceImpl extends ServiceImpl<UserSpotFavoritesMapper, UserSpotFavorites> implements UserSpotFavoritesService {

    @Resource
    private UserService userService;
    @Resource
    private SpotService spotService;
    /**
     * 校验数据
     *
     * @param userSpotFavorites
     * @param add               对创建的数据进行校验
     */
    @Override
    public void validUserSpotFavorites(UserSpotFavorites userSpotFavorites, boolean add) {
        ThrowUtils.throwIf(userSpotFavorites == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        Long spotId = userSpotFavorites.getSpotId();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(spotId == null || spotId < 0, ErrorCode.PARAMS_ERROR);
        }

    }

    /**
     * 获取查询条件
     *
     * @param userSpotFavoritesQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<UserSpotFavorites> getQueryWrapper(UserSpotFavoritesQueryRequest userSpotFavoritesQueryRequest) {
        QueryWrapper<UserSpotFavorites> queryWrapper = new QueryWrapper<>();
        if (userSpotFavoritesQueryRequest == null) {
            return queryWrapper;
        }
        Long id = userSpotFavoritesQueryRequest.getId();
        Long userId = userSpotFavoritesQueryRequest.getUserId();
        Long spotId = userSpotFavoritesQueryRequest.getSpotId();
        Integer status = userSpotFavoritesQueryRequest.getStatus();
        String remark = userSpotFavoritesQueryRequest.getRemark();
        String sortField = userSpotFavoritesQueryRequest.getSortField();
        String sortOrder = userSpotFavoritesQueryRequest.getSortOrder();
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(remark), "remark", remark);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(spotId), "spotId", spotId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取用户景点收藏关联表封装
     *
     * @param userSpotFavorites
     * @param request
     * @return
     */
    @Override
    public UserSpotFavoritesVO getUserSpotFavoritesVO(UserSpotFavorites userSpotFavorites, HttpServletRequest request) {
        // 对象转封装类
        return UserSpotFavoritesVO.objToVo(userSpotFavorites);
    }

    /**
     * 分页获取用户景点收藏关联表封装
     *
     * @param userSpotFavoritesPage
     * @param request
     * @return
     */
    @Override
    public Page<UserSpotFavoritesVO> getUserSpotFavoritesVOPage(Page<UserSpotFavorites> userSpotFavoritesPage, HttpServletRequest request) {
        List<UserSpotFavorites> userSpotFavoritesList = userSpotFavoritesPage.getRecords();
        Page<UserSpotFavoritesVO> userSpotFavoritesVOPage = new Page<>(userSpotFavoritesPage.getCurrent(), userSpotFavoritesPage.getSize(), userSpotFavoritesPage.getTotal());

        if (CollUtil.isEmpty(userSpotFavoritesList)) {
            return userSpotFavoritesVOPage;
        }

        // 获取所有的 spotId
        List<Long> spotIds = userSpotFavoritesList.stream()
                .map(UserSpotFavorites::getSpotId)
                .collect(Collectors.toList());

        // 根据 spotId 查询景点信息，假设有一个 spotService 可以批量查询景点信息
        Map<Long, Spot> spotMap = spotService.listByIds(spotIds).stream()
                .collect(Collectors.toMap(Spot::getId, spot -> spot));

        // 对象列表 => 封装对象列表
        List<UserSpotFavoritesVO> userSpotFavoritesVOList = userSpotFavoritesList.stream().map(userSpotFavorites -> {
            UserSpotFavoritesVO userSpotFavoritesVO = UserSpotFavoritesVO.objToVo(userSpotFavorites);

            // 根据 spotId 获取景点信息
            Spot spot = spotMap.get(userSpotFavorites.getSpotId());
            if (spot != null) {
                userSpotFavoritesVO.setSpotName(spot.getSpotName());
                userSpotFavoritesVO.setSpotAvatar(spot.getSpotAvatar());
                userSpotFavoritesVO.setSpotLocation(spot.getSpotLocation());
                userSpotFavoritesVO.setViewNum(spot.getViewNum());
                userSpotFavoritesVO.setFavourNum(spot.getFavourNum());
            }

            return userSpotFavoritesVO;
        }).collect(Collectors.toList());

        userSpotFavoritesVOPage.setRecords(userSpotFavoritesVOList);
        return userSpotFavoritesVOPage;
    }

}
