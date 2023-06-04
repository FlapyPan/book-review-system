package com.example.bookreviewsystem.service.impl;

import com.example.bookreviewsystem.entity.Category;
import com.example.bookreviewsystem.mapper.CategoryMapper;
import com.example.bookreviewsystem.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2023-04-16
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}
