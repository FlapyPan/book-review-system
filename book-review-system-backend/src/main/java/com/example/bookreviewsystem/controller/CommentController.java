package com.example.bookreviewsystem.controller;


import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.bookreviewsystem.service.ICommentService;
import com.example.bookreviewsystem.vo.CommentSaveRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 书评相关接口
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private ICommentService service;


    /**
     * 通过id查询
     *
     * @param id 实体id
     */
    @GetMapping("/{id}")
    public SaResult get(@PathVariable Integer id) {
        return SaResult.data(service.getById(id));
    }

    /**
     * 新增或修改实体，
     * 有id字段就是修改，没有就是新增
     *
     * @param saveRequest 保存请求
     */
    @PostMapping("")
    public SaResult save(@RequestBody @Validated CommentSaveRequest saveRequest) {
        Integer id = saveRequest.getId();
        boolean b;
        if (id != null && id > 0) { // 修改
            StpUtil.checkRole("admin");
            b = service.updateById(saveRequest.updateEntity(service.getById(id)));
        } else { // 保存
            b = service.save(saveRequest.toEntity());
        }
        return b ? SaResult.ok() : SaResult.error("保存失败");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     */
    @DeleteMapping("/{id}")
    @SaCheckRole("admin")
    public SaResult delete(@PathVariable Integer id) {
        return service.removeById(id) ? SaResult.ok() : SaResult.error("删除失败");
    }

    /**
     * 获取列表
     */
    @GetMapping("/list")
    @SaCheckRole("admin")
    public SaResult list() {
        return SaResult.data(service.listDetail());
    }

    /**
     * 查询自己的书评列表
     */
    @GetMapping("/list/me")
    public SaResult listSelf() {
        return SaResult.data(service.listByUserId(StpUtil.getLoginIdAsInt()));
    }

    /**
     * 根据用户id获取列表
     */
    @GetMapping("/list/user/{id}")
    @SaIgnore
    public SaResult listByUserId(@PathVariable Integer id) {
        return SaResult.data(service.listByUserId(id, 1));
    }

    /**
     * 根据书籍id获取列表
     */
    @GetMapping("/list/book/{id}")
    @SaIgnore
    public SaResult listByBookId(@PathVariable Integer id) {
        return SaResult.data(service.listByBookId(id));
    }

}
