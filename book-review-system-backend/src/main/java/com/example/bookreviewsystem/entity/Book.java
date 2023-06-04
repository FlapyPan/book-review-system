package com.example.bookreviewsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2023-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_book")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String cover;

    private String bookName;

    private String author;

    private String publisher;

    private String translator;

    private LocalDate publishDate;

    private Integer pages;

    private BigDecimal price;

    private String binding;

    private String isbn;

    private Integer status;

    private Integer categoryId;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
