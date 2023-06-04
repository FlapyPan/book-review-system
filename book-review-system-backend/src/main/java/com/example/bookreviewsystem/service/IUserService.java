package com.example.bookreviewsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookreviewsystem.entity.User;
import com.example.bookreviewsystem.vo.ChangePasswordRequest;
import com.example.bookreviewsystem.vo.ForgetPasswordRequest;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务接口
 */
public interface IUserService extends IService<User> {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    Optional<String> login(String username, String password);

    /**
     * 模糊搜索
     *
     * @param search 关键字
     */
    List<User> searchList(String search);

    /**
     * 修改密码
     */
    boolean changePassword(ChangePasswordRequest request);

    boolean forgetPassword(ForgetPasswordRequest request);

    void sendForgetCaptcha(String email);
}
