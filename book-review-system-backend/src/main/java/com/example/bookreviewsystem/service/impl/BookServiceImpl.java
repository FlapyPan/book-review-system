package com.example.bookreviewsystem.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookreviewsystem.entity.Book;
import com.example.bookreviewsystem.mapper.BookMapper;
import com.example.bookreviewsystem.service.IBookService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 书籍服务实现类
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

    @Override
    public List<Book> searchList(String search) {
        return this.list(
                this.lambdaQuery()
                        .like(Book::getBookName, search) // 书籍名
                        .or()
                        .like(Book::getAuthor, search) // 作者
                        .or()
                        .like(Book::getIsbn, search) // ISBN
                        .getWrapper()
        );
    }

    @Override
    public List<Book> latestBook() {
        return this.list(this.lambdaQuery()
                .orderByDesc(Book::getCreatedAt) // 创建时间降序
                .getWrapper()
                .last("LIMIT 40")); // 只要40个
    }

    @Override
    public List<Book> categoryList(Integer categoryId, String search) {
        if (StrUtil.isBlankIfStr(search)) return this.list(
                this.lambdaQuery()
                        .eq(Book::getCategoryId, categoryId)
                        .getWrapper()
        );
        return this.list(
                this.lambdaQuery()
                        .eq(Book::getCategoryId, categoryId)
                        .like(Book::getBookName, search) // 书籍名
                        .or()
                        .like(Book::getAuthor, search) // 作者
                        .or()
                        .like(Book::getIsbn, search) // ISBN
                        .getWrapper()
        );
    }
}
