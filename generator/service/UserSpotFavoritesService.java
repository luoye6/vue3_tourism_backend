package com.xiaobaitiao.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaobaitiao.springbootinit.model.dto.userSpotFavorites.UserSpotFavoritesQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.UserSpotFavorites;
import com.xiaobaitiao.springbootinit.model.vo.UserSpotFavoritesVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户景点收藏关联表服务
 *
 * @author 程序员小白条
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
public interface UserSpotFavoritesService extends IService<UserSpotFavorites> {

    /**
     * 校验数据
     *
     * @param userSpotFavorites
     * @param add 对创建的数据进行校验
     */
    void validUserSpotFavorites(UserSpotFavorites userSpotFavorites, boolean add);

    /**
     * 获取查询条件
     *
     * @param userSpotFavoritesQueryRequest
     * @return
     */
    QueryWrapper<UserSpotFavorites> getQueryWrapper(UserSpotFavoritesQueryRequest userSpotFavoritesQueryRequest);
    
    /**
     * 获取用户景点收藏关联表封装
     *
     * @param userSpotFavorites
     * @param request
     * @return
     */
    UserSpotFavoritesVO getUserSpotFavoritesVO(UserSpotFavorites userSpotFavorites, HttpServletRequest request);

    /**
     * 分页获取用户景点收藏关联表封装
     *
     * @param userSpotFavoritesPage
     * @param request
     * @return
     */
    Page<UserSpotFavoritesVO> getUserSpotFavoritesVOPage(Page<UserSpotFavorites> userSpotFavoritesPage, HttpServletRequest request);
}
