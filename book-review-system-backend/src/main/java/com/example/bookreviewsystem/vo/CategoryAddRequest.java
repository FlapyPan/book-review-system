package com.example.bookreviewsystem.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 分类添加请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CategoryAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String name;

}
