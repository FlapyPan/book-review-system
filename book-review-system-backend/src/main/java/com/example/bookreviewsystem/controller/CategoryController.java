package com.example.bookreviewsystem.controller;


import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.util.SaResult;
import com.example.bookreviewsystem.entity.Category;
import com.example.bookreviewsystem.service.ICategoryService;
import com.example.bookreviewsystem.vo.CategoryAddRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 分类相关接口
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private ICategoryService categoryService;

    /**
     * 查询所有分类信息
     */
    @SaIgnore
    @GetMapping("")
    public SaResult list() {
        return SaResult.data(categoryService.list());
    }

    /**
     * 添加分类
     */
    @SaCheckRole("admin")
    @PostMapping("")
    public SaResult add(@RequestBody @Validated CategoryAddRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        return categoryService.save(category) ? SaResult.ok() : SaResult.error("添加失败");
    }

    /**
     * 通过id删除
     *
     * @param id 实体id
     */
    @DeleteMapping("/{id}")
    @SaCheckRole("admin")
    public SaResult delete(@PathVariable Integer id) {
        return categoryService.removeById(id) ? SaResult.ok() : SaResult.error("删除失败");
    }
}
