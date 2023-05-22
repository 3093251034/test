package com.czd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czd.domain.ResponseResult;
import com.czd.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-04-26 17:27:30
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
