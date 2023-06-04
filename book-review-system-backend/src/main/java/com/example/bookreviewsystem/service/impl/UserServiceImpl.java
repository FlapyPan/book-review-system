package com.example.bookreviewsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookreviewsystem.entity.User;
import com.example.bookreviewsystem.mapper.UserMapper;
import com.example.bookreviewsystem.service.IUserService;
import com.example.bookreviewsystem.vo.ChangePasswordRequest;
import com.example.bookreviewsystem.vo.ForgetPasswordRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-04-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    public static final String EMAIL_CACHE_PREFIX = "PASSWORD_RESET_";

    @Value("${spring.mail.username}")
    private String mailFromAddress;
    @Resource
    private JavaMailSender mailSender;

    @Resource
    private TimedCache<String, String> emailCache;


    @Override
    public Optional<String> login(String username, String password) {
        Optional<User> user = Optional.ofNullable(getOne(lambdaQuery()
                .eq(User::getUsername, username)
                .eq(User::getPassword, password)
                .getWrapper())
        );
        return user.map(u -> {
            StpUtil.login(u.getId());
            return Optional.of(StpUtil.getTokenValue());
        }).orElseGet(Optional::empty);
    }

    @Override
    public List<User> searchList(String search) {
        return this.list(this.lambdaQuery()
                .like(User::getUsername, search)
                .or()
                .like(User::getEmail, search)
                .or()
                .like(User::getPhone, search)
                .getWrapper()
        );
    }

    @Override
    public boolean changePassword(ChangePasswordRequest request) {
        User user = this.getById(StpUtil.getLoginIdAsInt());
        if (!StrUtil.equals(user.getPassword(), request.getOldPassword())) return false;
        user.setPassword(request.getNewPassword());
        return this.updateById(user);
    }

    @Override
    public boolean forgetPassword(ForgetPasswordRequest request) {
        String email = request.getEmail();
        String cacheKey = EMAIL_CACHE_PREFIX + email;
        if (!emailCache.containsKey(cacheKey)) {
            // 验证码不存在
            return false;
        }
        String captcha = emailCache.get(cacheKey);
        // 校验验证码
        if (!StrUtil.equalsIgnoreCase(captcha, request.getCaptcha())) {
            return false;
        }
        User user = this.getOne(this.lambdaQuery()
                .eq(User::getPhone, request.getPhone())
                .eq(User::getUsername, request.getUsername())
                .eq(User::getEmail, email)
                .getWrapper());
        if (user == null) {
            return false;
        }
        user.setPassword(request.getPassword());
        this.updateById(user);
        return true;
    }

    @Override
    public void sendForgetCaptcha(String email) {
        String cacheKey = EMAIL_CACHE_PREFIX + email;
        String captcha;
        if (emailCache.containsKey(cacheKey)) {
            // 如果缓存中存在验证码，不需要重新生成
            captcha = emailCache.get(cacheKey);
        } else {
            // 生成新验证码
            captcha = RandomUtil.randomString(email, 6);
        }
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置发送方
        message.setFrom(mailFromAddress);
        // 设定接收方
        message.setTo(email);
        // 设置邮件主题
        message.setSubject("书评系统：密码重置验证码");
        // 放入缓存
        emailCache.put(cacheKey, captcha);
        message.setText(captcha);
        // 发送
        mailSender.send(message);
    }

}
