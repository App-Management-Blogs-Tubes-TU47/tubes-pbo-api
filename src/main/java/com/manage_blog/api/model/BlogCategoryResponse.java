package com.manage_blog.api.model;

import com.manage_blog.api.entity.BlogCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogCategoryResponse {
    private UUID id;
    private String name;
    private String slugs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BlogCategoryResponse(BlogCategory blogCategory) {
        this.id = blogCategory.getId();
        this.name = blogCategory.getName();
        this.slugs = blogCategory.getSlugs();
        this.createdAt = blogCategory.getCreatedAt();
        this.updatedAt = blogCategory.getUpdatedAt();
    }
}
