package com.example.bookreviewsystem.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CommentDetail implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private String content;

    private Integer userId;
    private String username;
    private String userAvatar;

    private Integer bookId;
    private String bookName;
    private String bookCover;

    /**
     * 审核状态
     * 0 审核中，1 审核成功，-1 审核未通过
     */
    private Integer status;

    private String statusReason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
