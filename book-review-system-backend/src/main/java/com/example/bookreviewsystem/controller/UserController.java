package com.example.bookreviewsystem.controller;


import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.StrUtil;
import com.example.bookreviewsystem.entity.User;
import com.example.bookreviewsystem.service.IUserService;
import com.example.bookreviewsystem.vo.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 用户相关接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    IUserService userService;

    /**
     * 登录接口
     */
    @SaIgnore
    @PostMapping("login")
    public SaResult login(@Validated @RequestBody LoginRequest loginRequest) {
        Optional<String> token = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return token.map(SaResult::data).orElseGet(() -> SaResult.error("账户或密码错误"));
    }

    /**
     * 注册接口
     */
    @SaIgnore
    @PostMapping("/register")
    public SaResult register(@Validated @RequestBody UserRegisterRequest registerRequest) {
        User user = registerRequest.toEntity();
        return userService.save(user) ? SaResult.ok() : SaResult.error();
    }

    /**
     * 修改密码接口
     */
    @PostMapping("/change-password")
    public SaResult changePassword(@Validated @RequestBody ChangePasswordRequest request) {
        return userService.changePassword(request) ? SaResult.ok() : SaResult.error();
    }

    /**
     * 获取自己的信息
     */
    @GetMapping("/me")
    public SaResult me() {
        return SaResult.data(userService.getById(StpUtil.getLoginIdAsInt()));
    }

    /**
     * 通过id查询一个用户
     *
     * @param id 实体id
     */
    @GetMapping("/{id}")
    public SaResult get(@PathVariable Integer id) {
        return SaResult.data(userService.getById(id));
    }

    /**
     * 新增或修改用户，
     * 有id字段就是修改，没有就是新增
     *
     * @param saveRequest 保存请求
     */
    @PostMapping("")
    public SaResult save(@RequestBody @Validated UserSaveRequest saveRequest) {
        Integer id = saveRequest.getId();
        boolean b;
        if (id != null && id > 0) { // 修改
            b = userService.updateById(saveRequest.updateEntity(userService.getById(id)));
        } else { // 保存
            b = userService.save(saveRequest.toEntity());
        }
        return b ? SaResult.ok() : SaResult.error("保存失败");
    }

    /**
     * 通过id删除用户
     *
     * @param id 实体id
     */
    @DeleteMapping("/{id}")
    @SaCheckRole("admin")
    public SaResult delete(@PathVariable Integer id) {
        return userService.removeById(id) ? SaResult.ok() : SaResult.error("删除失败");
    }

    /**
     * 模糊查询书籍列表
     *
     * @param search 查询关键字
     */
    @GetMapping("/list")
    public SaResult list(String search) {
        if (StrUtil.isBlankIfStr(search)) return SaResult.data(userService.list());
        return SaResult.data(userService.searchList(search));
    }

    /**
     * 发送忘记密码电子邮件
     */
    @SaIgnore
    @GetMapping("/forget/captcha")
    public SaResult forgetCaptcha(String email) {
        if (StrUtil.isBlankIfStr(email)) return SaResult.error("请输入邮箱地址");
        userService.sendForgetCaptcha(email);
        return SaResult.ok();
    }

    /**
     * 忘记密码
     */
    @SaIgnore
    @PostMapping("/forget")
    public SaResult forget(@RequestBody @Validated ForgetPasswordRequest request) {
        return userService.forgetPassword(request) ? SaResult.ok() : SaResult.error("验证码或其他信息错误");
    }
}
