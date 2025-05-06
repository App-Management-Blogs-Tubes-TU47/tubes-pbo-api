package com.manage_blog.api.model;

import com.manage_blog.api.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogRequest {
    private String title;
    private String category;
    private String author;

    @Nullable
    private MultipartFile thumbnailFile;

    private String article;
    private StatusEnum status;
}
