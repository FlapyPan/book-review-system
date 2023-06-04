package com.example.bookreviewsystem.controller;


import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.util.SaResult;
import com.example.bookreviewsystem.service.IRankService;
import com.example.bookreviewsystem.vo.RankRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 书籍评分接口
 */
@RestController
@RequestMapping("/rank")
public class RankController {

    @Resource
    private IRankService rankService;

    /**
     * 保存评分
     */
    @PostMapping("")
    public SaResult rank(@RequestBody @Validated RankRequest request) {
        return rankService.rank(request) ? SaResult.ok() : SaResult.error("评分失败，你已经评分过了");
    }

    /**
     * 获取书籍的评分
     */
    @SaIgnore
    @GetMapping("/book/{bookId}")
    public SaResult getBookScore(@PathVariable Integer bookId) {
        return SaResult.data(rankService.bookScore(bookId));
    }
}
