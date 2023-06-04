package com.example.bookreviewsystem.vo;

import com.example.bookreviewsystem.entity.Book;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 书籍保存请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BookSaveRequest extends BaseSaveRequest<Book> implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String cover;
    @NotBlank
    private String bookName;
    @NotBlank
    private String author;
    @NotBlank
    private String publisher;

    private String translator;

    private LocalDate publishDate;
    @NotNull
    @Positive
    private Integer pages;
    @Positive
    private BigDecimal price;

    private String binding;
    @NotBlank
    private String isbn;

    private Integer status;

    private Integer categoryId;

    private String description;

    @Override
    public Book toEntity() {
        Book book = new Book();
        BeanUtils.copyProperties(this, book);
        book.setId(null);
        return book;
    }
}
