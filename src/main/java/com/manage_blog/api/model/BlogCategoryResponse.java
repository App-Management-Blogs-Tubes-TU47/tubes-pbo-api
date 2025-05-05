package com.manage_blog.api.model;

import com.manage_blog.api.entity.BlogCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogCategoryResponse {

    private UUID id;
    private String name;
    private String slugs;
    private String createdAt;
    private String updatedAt;

    public BlogCategoryResponse(BlogCategory category) {
        this.id = category.getId();
        this.name = category.getName();
        this.slugs = category.getSlugs();
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
    }
}
