package com.example.bookreviewsystem.vo;

import com.example.bookreviewsystem.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户保存请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ForgetPasswordRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String captcha;

    @NotBlank
    private String phone;

    @NotBlank
    private String password;

    public User toEntity() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        user.setId(0);
        user.setRoles("user");
        return user;
    }
}
