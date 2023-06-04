package com.example.bookreviewsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookreviewsystem.entity.Member;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 会员服务接口
 */
public interface IMemberService extends IService<Member> {

    /**
     * 检查会员是否过期
     */
    Optional<LocalDateTime> check();

    /**
     * 模拟加入一个月的会员
     */
    boolean joinMember();
}
