package com.example.bookreviewsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookreviewsystem.entity.Book;
import com.example.bookreviewsystem.entity.Comment;
import com.example.bookreviewsystem.entity.User;
import com.example.bookreviewsystem.mapper.CommentMapper;
import com.example.bookreviewsystem.service.IBookService;
import com.example.bookreviewsystem.service.ICommentService;
import com.example.bookreviewsystem.service.IUserService;
import com.example.bookreviewsystem.vo.CommentDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2023-04-11
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    IUserService userService;

    @Resource
    IBookService bookService;

    @Override
    public List<CommentDetail> listDetail() {
        return this.list(this.lambdaQuery()
                        .orderByAsc(Comment::getStatus) // 未审核的排在前
                        .getWrapper())
                .stream().map(this::toDetail).collect(Collectors.toList());
    }

    @Override
    public List<CommentDetail> listByUserId(Integer userId) {
        return this.list(this.lambdaQuery()
                        .eq(Comment::getUserId, userId)
                        .orderByAsc(Comment::getStatus) // 未审核的排在前
                        .orderByDesc(Comment::getCreatedAt)
                        .getWrapper())
                .stream().map(this::toDetail).collect(Collectors.toList());
    }

    @Override
    public List<CommentDetail> listByUserId(Integer userId, int status) {
        return this.list(this.lambdaQuery()
                        .eq(Comment::getUserId, userId)
                        .eq(Comment::getStatus, status)
                        .orderByDesc(Comment::getCreatedAt)
                        .getWrapper())
                .stream().map(this::toDetail).collect(Collectors.toList());
    }

    @Override
    public List<CommentDetail> listByBookId(Integer bookId) {
        return this.list(this.lambdaQuery()
                        .eq(Comment::getBookId, bookId)
                        .eq(Comment::getStatus, 2) // 只查询审核通过的
                        .orderByDesc(Comment::getCreatedAt)
                        .getWrapper())
                .stream().map(this::toDetail).collect(Collectors.toList());
    }

    private CommentDetail toDetail(Comment comment) {
        CommentDetail detail = new CommentDetail();
        BeanUtils.copyProperties(comment, detail);
        User user = userService.getById(comment.getUserId());
        detail.setUsername(user.getUsername());
        detail.setUserAvatar(user.getAvatar());
        Book book = bookService.getById(comment.getBookId());
        detail.setBookName(book.getBookName());
        detail.setBookCover(book.getCover());
        return detail;
    }
}
