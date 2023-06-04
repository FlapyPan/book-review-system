package com.example.bookreviewsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookreviewsystem.entity.Ebook;
import com.example.bookreviewsystem.vo.EBookSaveRequest;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author author
 * @since 2023-04-11
 */
public interface IEbookService extends IService<Ebook> {

    /**
     * 上传
     */
    boolean save(EBookSaveRequest saveRequest) throws IOException;

    /**
     * 根据boot id 获取电子书文件列表
     */
    List<Ebook> getByBookId(Integer bookId);
}
