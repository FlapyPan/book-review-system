package com.example.bookreviewsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookreviewsystem.entity.Rank;
import com.example.bookreviewsystem.mapper.RankMapper;
import com.example.bookreviewsystem.service.IRankService;
import com.example.bookreviewsystem.vo.RankDetail;
import com.example.bookreviewsystem.vo.RankRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评分服务实现类
 */
@Service
public class RankServiceImpl extends ServiceImpl<RankMapper, Rank> implements IRankService {

    @Override
    public boolean rank(RankRequest request) {
        int userId = StpUtil.getLoginIdAsInt();
        // 查询是否已经存在该用户对该书籍的评分记录
        Rank rank = baseMapper.selectOne(lambdaQuery()
                .eq(Rank::getUserId, userId)
                .eq(Rank::getBookId, request.getBookId())
                .getWrapper());
        // 如果已经存在，则不允许再次评分
        if (rank != null) {
            return false;
        }
        // 如果不存在，则插入新的评分记录
        rank = new Rank();
        rank.setUserId(userId);
        rank.setBookId(request.getBookId());
        rank.setScore(request.getScore());
        return save(rank);
    }

    @Override
    public RankDetail bookScore(Integer bookId) {
        List<Rank> ranks = this.list(lambdaQuery()
                .eq(Rank::getBookId, bookId)
                .getWrapper());
        // 平均分
        double score = ranks.stream()
                .mapToDouble(Rank::getScore)
                .average()
                .orElse(0.0);
        // 评分数量
        long count = ranks.size();
        RankDetail detail = new RankDetail();
        detail.setScore(score);
        detail.setScoreCount(count);
        if (StpUtil.isLogin()) {
            int userId = StpUtil.getLoginIdAsInt();
            // 查询当前用户的评分
            Rank userRank = this.getOne(lambdaQuery()
                    .eq(Rank::getUserId, userId)
                    .eq(Rank::getBookId, bookId)
                    .getWrapper());
            detail.setYourScore(userRank == null ? null : userRank.getScore());
        }
        return detail;
    }
}
