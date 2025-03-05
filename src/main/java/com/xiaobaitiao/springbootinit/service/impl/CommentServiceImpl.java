package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.BusinessException;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.CommentMapper;
import com.xiaobaitiao.springbootinit.model.dto.comment.CommentQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.Comment;
import com.xiaobaitiao.springbootinit.model.entity.Post;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.CommentVO;
import com.xiaobaitiao.springbootinit.model.vo.UserVO;
import com.xiaobaitiao.springbootinit.service.CommentService;
import com.xiaobaitiao.springbootinit.service.PostService;
import com.xiaobaitiao.springbootinit.service.UserService;
import com.xiaobaitiao.springbootinit.utils.SqlUtils;
import com.xiaobaitiao.springbootinit.utils.WordUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 帖子评论服务实现
 *
 */
@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    /**
     * 校验数据
     *
     * @param comment
     * @param add     对创建的数据进行校验
     */
    @Override
    public void validComment(Comment comment, boolean add) {
        ThrowUtils.throwIf(comment == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String content = comment.getContent();
        Long parentId = comment.getParentId();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(content), ErrorCode.PARAMS_ERROR);
            //校验父id
            if (parentId != null && parentId > 0) {
                Comment commentParent = this.getById(parentId);
                ThrowUtils.throwIf(commentParent == null, ErrorCode.NOT_FOUND_ERROR);
            }

        }
        // 修改数据时，有参数则校验
        // todo 补充校验规则
        if (StringUtils.isNotBlank(content)) {
            if (WordUtils.containsForbiddenWords(content)){
                ThrowUtils.throwIf(WordUtils.containsForbiddenWords(content), ErrorCode.WORD_FORBIDDEN_ERROR, "包含违禁词");
            }
            ThrowUtils.throwIf(content.length() > 1024, ErrorCode.PARAMS_ERROR, "评论过长");
        }
    }

    /**
     * 获取查询条件
     *
     * @param commentQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Comment> getQueryWrapper(CommentQueryRequest commentQueryRequest) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        if (commentQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = commentQueryRequest.getId();

        String content = commentQueryRequest.getContent();
        String sortField = commentQueryRequest.getSortField();
        String sortOrder = commentQueryRequest.getSortOrder();
        Long userId = commentQueryRequest.getUserId();
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取帖子评论封装
     *
     * @param comment
     * @param request
     * @return
     */
    @Override
    public CommentVO getCommentVO(Comment comment, HttpServletRequest request) {
        // 对象转封装类
        CommentVO commentVO = CommentVO.objToVo(comment);

        // region 可选
        // 1. 关联查询用户信息
        Long userId = comment.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        commentVO.setUser(userVO);

        // endregion

        return commentVO;
    }

    /**
     * 分页获取帖子评论封装
     *
     * @param commentPage
     * @param request
     * @return
     */
    @Override
    public Page<CommentVO> getCommentVOPage(Page<Comment> commentPage, HttpServletRequest request) {
        List<Comment> commentList = commentPage.getRecords();
        Page<CommentVO> commentVOPage = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        if (CollUtil.isEmpty(commentList)) {
            return commentVOPage;
        }
        // 对象列表 => 封装对象列表
        List<CommentVO> commentVOList = commentList.stream().map(comment -> {
            return CommentVO.objToVo(comment);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = commentList.stream().map(Comment::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        commentVOList.forEach(commentVO -> {
            Long userId = commentVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            commentVO.setUser(userService.getUserVO(user));
        });
        // endregion

        commentVOPage.setRecords(commentVOList);
        return commentVOPage;
    }

    /**
     * 根据帖子Id获取评论
     *
     * @param postId
     * @param request
     * @return
     */
    @Override
    public List<CommentVO> getCommentsByPostId(long postId, HttpServletRequest request) {
//        1.参数校验
        if (postId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Post post = postService.getById(postId);
        ThrowUtils.throwIf(post == null, ErrorCode.NOT_FOUND_ERROR);

//        2. 获取该帖子下所有的评论
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("postId", postId);
        queryWrapper.orderByDesc("createTime");
        List<Comment> commentList = this.list(queryWrapper);
        if (commentList.isEmpty()){
            return  null;
        }


        //纪录
        HashMap<Long, CommentVO> map = new HashMap<>();

//        3.找出一级评论（顶级父Id为空）
        Iterator<Comment> iterator = commentList.iterator();
        while (iterator.hasNext()) {
            Comment comment = iterator.next();
            if (comment.getAncestorId() == null) {
                CommentVO commentVO = CommentVO.objToVo(comment);
                // 初始化 replies，防止为 null
                commentVO.setReplies(new ArrayList<>());
                // 补充信息
                User user = userService.getById(commentVO.getUserId());
                commentVO.setUser(userService.getUserVO(user));
                map.put(comment.getId(), commentVO);
                iterator.remove(); // 使用 Iterator 安全删除元素
            }
        }

        System.out.println("map="+map);

        System.out.println("mapSize="+map.size());

//        4.遍历剩下的列表
        for (Comment comment : commentList) {
            Long ancestorId = comment.getAncestorId();
            if (map.containsKey(ancestorId)) {
                CommentVO questoncomment = map.get(ancestorId);
                //添加进去
                List<CommentVO> replies = questoncomment.getReplies();
                CommentVO commentVO = CommentVO.objToVo(comment);
                //补充信息
                User user = userService.getById( commentVO.getUserId());
                commentVO.setUser(userService.getUserVO(user));
                //添加回复人
                Long parentId = comment.getParentId();
                Comment parentComment = this.getById(parentId);
                if (parentComment==null){
                    continue;
                }
                User repliedUser = userService.getById(parentComment.getUserId());
                commentVO.setRepliedUser(userService.getUserVO(repliedUser));
                replies.add(commentVO);
            }
        }
        List<CommentVO> res = new ArrayList<>();
        for (CommentVO commentVO : map.values()) {
            res.add(commentVO);
        }
        // 使用 Lambda 表达式按 createTime 降序排序
        res.sort((c1, c2) -> c2.getCreateTime().compareTo(c1.getCreateTime())); // 降序
        res.stream().forEach(commentVo->{
            if (commentVo.getReplies()!=null&&commentVo.getReplies().size()>=2){
                commentVo.getReplies().sort(Comparator.comparing(CommentVO::getCreateTime)); // 升序
            }
        });
        return res;
    }

}
