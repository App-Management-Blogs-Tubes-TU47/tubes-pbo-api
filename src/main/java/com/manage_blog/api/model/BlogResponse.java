package com.manage_blog.api.model;

import com.manage_blog.api.entity.Blog;
import com.manage_blog.api.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogResponse {
    private String id;
    private String title;
    private String slugs;
//    private String tumbnail;
    private String article;
    private StatusEnum status;
    private String tumbnailUrl;
    private UserResponse author;
    private BlogCategoryResponse category;
    private String createdAt;
    private String updatedAt;

    public BlogResponse(Blog blog) {
        this.id = blog.getId().toString();
        this.title = blog.getTitle();
        this.slugs = blog.getSlugs();
//        this.tumbnail = blog.getTumbnail();
        this.article = blog.getArticle();
        this.status = blog.getStatus();
        this.createdAt = blog.getCreatedAt();
        this.updatedAt = blog.getUpdatedAt();
    }
}
