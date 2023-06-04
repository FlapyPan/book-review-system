package com.example.bookreviewsystem.config;

import cn.dev33.satoken.stp.StpInterface;
import com.example.bookreviewsystem.entity.User;
import com.example.bookreviewsystem.service.IUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * Sa-Token的自定义权限验证扩展
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private IUserService userService;

    /**
     * 返回一个账号所拥有的权限码集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本系统只使用角色鉴权，权限列表为空即可
        return List.of();
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 查询对应用户
        User user = userService.getById((Serializable) loginId);
        // 返回权限列表
        return user.getRoleList();
    }

}
