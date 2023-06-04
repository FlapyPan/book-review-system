package com.example.bookreviewsystem.controller;


import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.util.SaResult;
import com.example.bookreviewsystem.entity.Ebook;
import com.example.bookreviewsystem.service.IEbookService;
import com.example.bookreviewsystem.vo.EBookSaveRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 电子书相关接口
 */
@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private IEbookService ebookService;

    @PostMapping("")
    @SaCheckRole("admin")
    public SaResult save(@ModelAttribute @Validated EBookSaveRequest saveRequest) throws IOException {
        return ebookService.save(saveRequest) ? SaResult.ok() : SaResult.error("上传失败");
    }

    @GetMapping("/book/{bookId}")
    public SaResult getByBookId(@PathVariable Integer bookId) {
        return SaResult.data(ebookService.getByBookId(bookId));
    }

    /**
     * 文件下载接口
     *
     * @param id 文件id
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer id) throws IOException {
        Ebook ebook = ebookService.getById(id);
        Path path = Path.of(ebook.getFilepath());
        if (Files.notExists(path)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + ebook.getFilename() + "\"");
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        byte[] bytes = Files.readAllBytes(path);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(Files.size(path))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(bytes));
    }

    @DeleteMapping("/{id}")
    @SaCheckRole("admin")
    public SaResult delete(@PathVariable Integer id) {
        return ebookService.removeById(id) ? SaResult.ok() : SaResult.error();
    }
}
