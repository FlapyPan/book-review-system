package com.example.bookreviewsystem.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

/**
 * 评分请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RankRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    private Integer bookId;

    @PositiveOrZero
    private Double score;

}
