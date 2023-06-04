package com.example.bookreviewsystem.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 评分请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RankDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private Double score;

    private Long scoreCount;

    private Double yourScore;

}
