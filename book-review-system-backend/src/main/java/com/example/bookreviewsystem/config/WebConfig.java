package com.example.bookreviewsystem.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * mvc 配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 跨域请求路径
                .allowedOrigins("*") // 允许的跨域的域名，*为所有
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许跨域的请求方法
                .allowedHeaders("*") // 允许跨域的请求头
                .allowCredentials(false) // 前后端分离，无cookie
                .maxAge(3600); // options请求的有效期
    }

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 使用SaToken拦截器，将自动开启注解鉴权
        registry.addInterceptor(new SaInterceptor(h -> StpUtil.checkLogin()) {
                    /**
                     * 重写SaInterceptor，实现自己的逻辑
                     */
                    @Override
                    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                        // 放行OPTIONS跨域请求，防止跨域请求失败
                        if ("OPTIONS".equals(request.getMethod())) return true;
                        // 其他请求调用父类方法进行处理
                        return super.preHandle(request, response, handler);
                    }
                })
                .addPathPatterns("/**") // 拦截的路径
                .excludePathPatterns("/static/**"); // 放行静态资源
    }
}
