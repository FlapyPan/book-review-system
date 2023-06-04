package com.example.bookreviewsystem.controller;


import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.StrUtil;
import com.example.bookreviewsystem.service.IBookService;
import com.example.bookreviewsystem.vo.BookSaveRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 书籍相关接口
 */
@RestController
@RequestMapping("/book")
public class BookController {
    @Resource
    IBookService service; // 注入服务实现类

    /**
     * 通过id查询书籍
     *
     * @param id 实体id
     */
    @GetMapping("/{id}")
    @SaIgnore
    public SaResult get(@PathVariable Integer id) {
        return SaResult.data(service.getById(id));
    }

    /**
     * 新增或修改实体，
     * 有id字段就是修改，没有就是新增
     *
     * @param saveRequest 保存请求
     */
    @SaCheckRole("admin")
    @PostMapping("")
    public SaResult save(@RequestBody @Validated BookSaveRequest saveRequest) {
        Integer id = saveRequest.getId();
        boolean b;
        if (id != null && id > 0) { // 修改
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
     * 模糊查询
     *
     * @param search 查询关键字
     */
    @GetMapping("/list")
    @SaIgnore
    public SaResult list(@RequestParam(required = false) String search) {
        // 如果查询字段是空字符串
        if (StrUtil.isBlankIfStr(search)) return SaResult.data(service.list());
        return SaResult.data(service.searchList(search));
    }

    /**
     * 分类模糊查询
     */
    @GetMapping("/category/{categoryId}")
    @SaIgnore
    public SaResult categoryList(@RequestParam(required = false) String search, @PathVariable Integer categoryId) {
        return SaResult.data(service.categoryList(categoryId, search));
    }

    /**
     * 查询最新书籍
     */
    @GetMapping("/latest")
    @SaIgnore
    public SaResult latestTen() {
        return SaResult.data(service.latestBook());
    }
}
