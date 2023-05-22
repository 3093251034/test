package com.czd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czd.constants.SystemConstants;
import com.czd.domain.ResponseResult;
import com.czd.domain.entity.Comment;
import com.czd.enums.AppHttpCodeEnum;
import com.czd.exception.SystemException;
import com.czd.domain.vo.CommentVo;
import com.czd.domain.vo.PageVo;
import com.czd.mapper.CommentMapper;
import com.czd.service.CommentService;
import com.czd.service.UserService;
import com.czd.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-04-26 17:27:32
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        //对articleId进行判断， commentType为0，再继续判断articleId
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        //根评论 rootId为 -1
        queryWrapper.eq(Comment::getRootId,-1);

        //评论类型
        queryWrapper.eq(Comment::getType, commentType);

        //分页
        Page<Comment> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        //查询所有的根评论对应的子评论集合，并赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //赋值
            commentVo.setChildren(children);
        }

        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
//        comment.setCreateBy(SecurityUtils.getUserId());
        //评论内容不能为空
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 根据根评论id查询对应的子评论集合
     * @param id 根评论id
     * @return
     */
    private List<CommentVo> getChildren(Long id){

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> commentList = list(queryWrapper);

        return toCommentVoList(commentList);
    }

    private List<CommentVo> toCommentVoList(List<Comment> commentList){

        List<CommentVo> commentVoList = BeanCopyUtils.copyBeanList(commentList,CommentVo.class);

        //遍历Vo集合
        for (CommentVo commentVo : commentVoList) {
            //通过createBy查询用户昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUserName(nickName);

            //通过toCommentUserId查询用户的昵称并赋值
            //若toCommentUserId不为-1才进行查询
            if (commentVo.getToCommentUserId() != -1){
                String toCommentUsername = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUsername);
            }
        }
        return commentVoList;
    }
}
