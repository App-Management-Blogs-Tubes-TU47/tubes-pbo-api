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
public class BlogResponse {

    private UUID id;
    private UUID category_id;
    private UUID user_id;
    private String slugs;
    private String title;
    private String tumbnail;
    private String article;
    private String status;
    private String createdAt;
    private String updatedAt;

    public BlogCategoryResponse(BlogCategory category) {
        this.id = category.getId();
        this.category_id = category.getCategory_id();
        this.user_id = category.getUser_id();
        this.slugs = category.getSlugs();
        this.title = category.getTitle();
        this.tumbnail = category.getTumbnail();
        this.article = category.getArticle();
        this.status = category.getStatus();
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
    }
}