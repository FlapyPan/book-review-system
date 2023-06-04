package com.example.bookreviewsystem.vo;

import com.example.bookreviewsystem.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 用户保存请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserSaveRequest extends BaseSaveRequest<User> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String avatar;

    private String username;

    private String email;

    private String phone;

    private List<String> roleList;

    @Override
    public User toEntity() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        user.setId(null);
        return user;
    }
}
