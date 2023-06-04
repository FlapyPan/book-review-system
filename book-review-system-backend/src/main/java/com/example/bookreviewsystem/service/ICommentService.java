package com.example.bookreviewsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookreviewsystem.entity.Comment;
import com.example.bookreviewsystem.vo.CommentDetail;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author author
 * @since 2023-04-11
 */
public interface ICommentService extends IService<Comment> {

    /**
     * 查询所有书评
     */
    List<CommentDetail> listDetail();

    /**
     * 根据用户查询书评
     */
    List<CommentDetail> listByUserId(Integer userId);

    /**
     * 根据用户和审核状态查询书评
     */
    List<CommentDetail> listByUserId(Integer userId, int status);

    /**
     * 根据书籍查询书评
     */
    List<CommentDetail> listByBookId(Integer bookId);

}
