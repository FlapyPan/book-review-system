package com.example.bookreviewsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookreviewsystem.entity.Member;
import com.example.bookreviewsystem.mapper.MemberMapper;
import com.example.bookreviewsystem.service.IMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 会员服务实现类
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

    @Transactional
    @Override
    public Optional<LocalDateTime> check() {
        int userId = StpUtil.getLoginIdAsInt(); // 获取用户
        Member member = this.getOne(this.lambdaQuery()
                .eq(Member::getUserId, userId)
                .getWrapper());
        if (member == null) return Optional.empty(); // 检查是否存在会员信息
        LocalDateTime expireAt = member.getExpireAt();
        // 检查会员过期情况
        if (LocalDateTime.now().isAfter(expireAt)) {
            this.removeById(member.getId());// 过期删除会员记录
            return Optional.empty();
        }
        return Optional.of(expireAt);
    }

    @Transactional
    @Override
    public boolean joinMember() {
        int userId = StpUtil.getLoginIdAsInt(); // 获取用户
        Member member = this.getOne(this.lambdaQuery()
                .eq(Member::getUserId, userId)
                .getWrapper());
        LocalDateTime now = LocalDateTime.now();
        if (member != null) { // 检查是否存在会员信息
            LocalDateTime expireAt = member.getExpireAt();
            if (now.isAfter(expireAt)) {
                // 过期添加一个月
                member.setExpireAt(now.plusMonths(1));
            } else {
                // 续期一个月
                member.setExpireAt(expireAt.plusMonths(1));
            }
            return this.updateById(member);
        }
        // 未开通过会员
        Member newMember = new Member();
        newMember.setUserId(userId);
        newMember.setExpireAt(now.plusMonths(1));
        return this.save(newMember);
    }
}
