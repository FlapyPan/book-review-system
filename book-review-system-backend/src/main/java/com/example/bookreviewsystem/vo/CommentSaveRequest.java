package com.example.bookreviewsystem.vo;

import cn.dev33.satoken.stp.StpUtil;
import com.example.bookreviewsystem.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 书评保存请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CommentSaveRequest extends BaseSaveRequest<Comment> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;

    private String content;

    private Integer bookId;

    private Integer status;

    private String statusReason;

    @Override
    public Comment toEntity() {
        Comment comment = new Comment();
        BeanUtils.copyProperties(this, comment);
        comment.setId(null);
        comment.setUserId(StpUtil.getLoginIdAsInt()); // 当前用户
        comment.setStatus(0); // 默认为审核中
        comment.setStatusReason(null);
        return comment;
    }
}
