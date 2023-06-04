package com.example.bookreviewsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookreviewsystem.entity.Rank;
import com.example.bookreviewsystem.vo.RankDetail;
import com.example.bookreviewsystem.vo.RankRequest;

/**
 * 评分服务接口
 */
public interface IRankService extends IService<Rank> {

    /**
     * 给书籍评分
     */
    boolean rank(RankRequest request);

    /**
     * 获取书籍评分
     *
     * @param bookId 书籍id
     */
    RankDetail bookScore(Integer bookId);
}
