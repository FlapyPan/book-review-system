package com.example.bookreviewsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookreviewsystem.entity.Book;

import java.util.List;

/**
 * 书籍服务类
 */
public interface IBookService extends IService<Book> {
    /**
     * 模糊查询
     *
     * @param search 查询关键字
     * @return 查询结果列表
     */
    List<Book> searchList(String search);

    /**
     * 最新上传的书籍列表
     *
     * @return 查询结果列表
     */
    List<Book> latestBook();

    /**
     * 分类模糊查询
     */
    List<Book> categoryList(Integer categoryId, String search);
}
