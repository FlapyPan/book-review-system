package com.example.bookreviewsystem.vo;

import com.example.bookreviewsystem.entity.Ebook;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EBookSaveRequest extends BaseSaveRequest<Ebook> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    @Positive
    private Integer bookId;

    private MultipartFile file;

    @Override
    public Ebook toEntity() {
        Ebook ebook = new Ebook();
        BeanUtils.copyProperties(this, ebook);
        return ebook;
    }
}
