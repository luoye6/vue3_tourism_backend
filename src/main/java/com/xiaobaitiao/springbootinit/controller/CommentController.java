package com.xiaobaitiao.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaobaitiao.springbootinit.annotation.AuthCheck;
import com.xiaobaitiao.springbootinit.common.BaseResponse;
import com.xiaobaitiao.springbootinit.common.DeleteRequest;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.common.ResultUtils;
import com.xiaobaitiao.springbootinit.constant.UserConstant;
import com.xiaobaitiao.springbootinit.exception.BusinessException;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.model.dto.comment.CommentAddRequest;
import com.xiaobaitiao.springbootinit.model.dto.comment.CommentEditRequest;
import com.xiaobaitiao.springbootinit.model.dto.comment.CommentQueryRequest;
import com.xiaobaitiao.springbootinit.model.dto.comment.CommentUpdateRequest;
import com.xiaobaitiao.springbootinit.model.entity.Comment;
import com.xiaobaitiao.springbootinit.model.entity.Post;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.CommentVO;
import com.xiaobaitiao.springbootinit.model.vo.MyCommentVO;
import com.xiaobaitiao.springbootinit.service.CommentService;
import com.xiaobaitiao.springbootinit.service.PostService;
import com.xiaobaitiao.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 帖子评论接口
 *
 */
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;
    @Resource
    private PostService postService;
    // region 增删改查

    /**
     * 创建帖子评论
     *
     * @param commentAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addComment(@RequestBody CommentAddRequest commentAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(commentAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentAddRequest, comment);
        // 数据校验
        commentService.validComment(comment, true);
        Long parentId = commentAddRequest.getParentId();
        //校验父id
        if (parentId != null && parentId > 0) {
            Comment commentParent = commentService.getById(parentId);
            if (commentParent.getAncestorId() == null) {//二级评论
                comment.setAncestorId(commentParent.getId());
            } else {
                comment.setAncestorId(commentParent.getAncestorId());
            }
        }

        User loginUser = userService.getLoginUser(request);
        comment.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = commentService.save(comment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newCommentId = comment.getId();
        return ResultUtils.success(newCommentId);
    }

    /**
     * 删除帖子评论
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteComment(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Comment oldComment = commentService.getById(id);
        ThrowUtils.throwIf(oldComment == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldComment.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = commentService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新帖子评论（仅管理员可用）
     *
     * @param commentUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateComment(@RequestBody CommentUpdateRequest commentUpdateRequest) {
        if (commentUpdateRequest == null || commentUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentUpdateRequest, comment);
        // 数据校验
        commentService.validComment(comment, false);
        // 判断是否存在
        long id = commentUpdateRequest.getId();
        Comment oldComment = commentService.getById(id);
        ThrowUtils.throwIf(oldComment == null, ErrorCode.NOT_FOUND_ERROR);
        comment.setPostId(null);
        // 操作数据库
        boolean result = commentService.updateById(comment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取帖子评论（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<CommentVO> getCommentVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Comment comment = commentService.getById(id);
        ThrowUtils.throwIf(comment == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(commentService.getCommentVO(comment, request));
    }


    /**
     * 分页获取帖子评论列表（仅管理员可用）
     *
     * @param commentQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Comment>> listCommentByPage(@RequestBody CommentQueryRequest commentQueryRequest) {
        long current = commentQueryRequest.getCurrent();
        long size = commentQueryRequest.getPageSize();
        // 查询数据库
        Page<Comment> commentPage = commentService.page(new Page<>(current, size),
                commentService.getQueryWrapper(commentQueryRequest));
        return ResultUtils.success(commentPage);
    }

    /**
     * 分页获取帖子评论列表（封装类）
     *
     * @param commentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<CommentVO>> listCommentVOByPage(@RequestBody CommentQueryRequest commentQueryRequest,
                                                             HttpServletRequest request) {
        long current = commentQueryRequest.getCurrent();
        long size = commentQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Comment> commentPage = commentService.page(new Page<>(current, size),
                commentService.getQueryWrapper(commentQueryRequest));
        // 获取封装类
        return ResultUtils.success(commentService.getCommentVOPage(commentPage, request));
    }

    /**
     * 分页获取当前登录用户创建的帖子评论列表
     *
     * @param commentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<CommentVO>> listMyCommentVOByPage(@RequestBody CommentQueryRequest commentQueryRequest,
                                                               HttpServletRequest request) {
        ThrowUtils.throwIf(commentQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        commentQueryRequest.setUserId(loginUser.getId());
        long current = commentQueryRequest.getCurrent();
        long size = commentQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Comment> commentPage = commentService.page(new Page<>(current, size),
                commentService.getQueryWrapper(commentQueryRequest));
        // 获取封装类
        return ResultUtils.success(commentService.getCommentVOPage(commentPage, request));
    }

    /**
     * 编辑帖子评论（给用户使用）
     *
     * @param commentEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editComment(@RequestBody CommentEditRequest commentEditRequest, HttpServletRequest request) {
        if (commentEditRequest == null || commentEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentEditRequest, comment);
        // 数据校验
        commentService.validComment(comment, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = commentEditRequest.getId();
        Comment oldComment = commentService.getById(id);
        ThrowUtils.throwIf(oldComment == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldComment.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        comment.setPostId(null);
        // 操作数据库
        boolean result = commentService.updateById(comment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion

    /**
     * 根据 帖子id 获取帖子评论（封装类）
     *
     * @param postId
     * @return
     */
    @GetMapping("/get/questonCommnet")
    public BaseResponse<List<CommentVO>> getCommentByPostId(long postId, HttpServletRequest request) {

        // 获取封装类
        return ResultUtils.success(commentService.getCommentsByPostId(postId, request));
    }

    /**
     * 分页获取当前登录用户创建的帖子评论列表
     *
     * @param request
     * @return
     */
    @PostMapping("/myComments")
    public BaseResponse<List<MyCommentVO>> listMyComments(HttpServletRequest request) {
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);

        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        queryWrapper.orderByDesc("updateTime");
        queryWrapper.isNull("ancestorId"); // 添加条件，某列为空
        List<Comment> commentList = commentService.list(queryWrapper);
        //为空
        if (commentList.isEmpty()){
            return ResultUtils.success(null);
        }
        Set<Long> postIdSet = commentList.stream().map(Comment::getPostId).collect(Collectors.toSet());
        QueryWrapper<Post> wrapper = new QueryWrapper<>();
        wrapper.select("id", "title");
        wrapper.in("id", postIdSet);
        List<Post> postList = postService.list(wrapper);
        List<MyCommentVO> res = new ArrayList<>();

// 转换为 Map，key 为 getPostId，value 为 Post
        Map<Long, Post> postMap = postList.stream()
                .collect(Collectors.toMap(
                        Post::getId, // key: postId
                        post -> post       // value: Post 对象
                ));
        commentList.stream().forEach(comment -> {
            MyCommentVO myCommentVO = new MyCommentVO();
            BeanUtils.copyProperties(comment, myCommentVO);

            Post post = postMap.get(comment.getPostId());
            myCommentVO.setPostTitle(post.getTitle());
            myCommentVO.setPostId(post.getId());
            myCommentVO.setPostContent(post.getContent());
            res.add(myCommentVO);
        });

        return ResultUtils.success(res);
    }

}
