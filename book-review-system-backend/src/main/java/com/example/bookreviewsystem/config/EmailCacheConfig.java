package com.example.bookreviewsystem.config;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码发送缓存配置
 */
@Configuration
public class EmailCacheConfig {
    @Bean
    public TimedCache<String, String> emailCache() {
        // 5 分钟过期
        TimedCache<String, String> cache = CacheUtil.newTimedCache(300000);
        // 每 50 毫秒清理过期缓存
        cache.schedulePrune(100);
        return cache;
    }
}
