package com.example.bookreviewsystem.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 基本保存请求抽象类
 *
 * @param <Entity> 对应的实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public abstract class BaseSaveRequest<Entity> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 将保存请求转换为对应的实体类
     */
    public abstract Entity toEntity();

    /**
     * 将保存请求转换为对应的实体类
     */
    public Entity updateEntity(Entity entity) {
        BeanUtils.copyProperties(this, entity);
        return entity;
    }
}
