package com.example.bookreviewsystem.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String avatar;

    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private String phone;

    /**
     * 用户权限使用字符串来存储，逗号分隔：user,admin
     */
    @JsonIgnore
    private String roles;

    /**
     * 获取权限列表
     */
    public List<String> getRoleList() {
        return StrUtil.splitTrim(this.roles, ",");
    }

    /**
     * 存储权限列表
     */
    public void setRoleList(List<String> roles) {
        this.roles = StrUtil.join(",", roles);
    }

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
