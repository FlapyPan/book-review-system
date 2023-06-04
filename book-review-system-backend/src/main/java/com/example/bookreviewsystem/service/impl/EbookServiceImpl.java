package com.example.bookreviewsystem.service.impl;

import cn.hutool.core.lang.id.NanoId;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookreviewsystem.entity.Ebook;
import com.example.bookreviewsystem.mapper.EbookMapper;
import com.example.bookreviewsystem.service.IEbookService;
import com.example.bookreviewsystem.vo.EBookSaveRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * 电子书服务实现类
 */
@Service
public class EbookServiceImpl extends ServiceImpl<EbookMapper, Ebook> implements IEbookService {

    public static final Path EBOOK_SAVE_PATH = Path.of("ebook");
    public static final String EBOOK_SAVE_PREFIX = "book_";

    @PostConstruct
    public void init() throws IOException {
        // 检查文件夹是否存在
        if (Files.notExists(EBOOK_SAVE_PATH)) {
            Files.createDirectories(EBOOK_SAVE_PATH);
        }
    }

    @Override
    @Transactional
    public boolean save(EBookSaveRequest saveRequest) throws IOException {
        Integer bookId = saveRequest.getBookId();
        MultipartFile file = saveRequest.getFile();
        if (file == null) return false;
        // 获取扩展名
        String originalFilename = file.getOriginalFilename();
        String ext = "";
        if (originalFilename != null) {
            ext = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        // book_id_nanoid.xxx
        String filename = EBOOK_SAVE_PREFIX + bookId + "_" + NanoId.randomNanoId() + ext;
        Path path = EBOOK_SAVE_PATH.resolve(filename);
        file.transferTo(path);
        Ebook ebook = saveRequest.toEntity();
        ebook.setFilepath(path.toString());
        ebook.setFilename(filename);
        return this.save(ebook);
    }

    @Override
    public List<Ebook> getByBookId(Integer bookId) {
        return this.list(this.lambdaQuery()
                .eq(Ebook::getBookId, bookId)
                .orderByDesc(Ebook::getUpdatedAt)
                .getWrapper());
    }
}
