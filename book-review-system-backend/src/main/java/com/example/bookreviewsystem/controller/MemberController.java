package com.example.bookreviewsystem.controller;


import cn.dev33.satoken.util.SaResult;
import com.example.bookreviewsystem.service.IMemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 会员相关接口
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private IMemberService service;

    /**
     * 检查会员状态
     */
    @GetMapping("/check")
    public SaResult check() {
        return service.check().map(SaResult::data).orElseGet(() -> SaResult.error("会员过期"));
    }

    /**
     * 模拟开通会员
     */
    @GetMapping("/join")
    public SaResult join() {
        return service.joinMember() ? SaResult.ok() : SaResult.error("开通失败");
    }

}
