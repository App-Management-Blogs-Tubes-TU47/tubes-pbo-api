package com.manage_blog.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.manage_blog.api.entity.Blog;
import com.manage_blog.api.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogResponse {
    private String id;
    private String title;
    private String slugs;
    private String article;
    private StatusEnum status;
    private String tumbnailUrl;
    private UserResponse author;
    private String authorName;
    private String authorUsername;
    private BlogCategoryResponse category;
    private String categoryName;
    private String categorySlugs;
    private long CountComments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BlogResponse(Blog blog) {
        this.id = blog.getId().toString();
        this.title = blog.getTitle();
        this.slugs = blog.getSlugs();
        this.article = blog.getArticle();
        this.status = blog.getStatus();
        this.createdAt = blog.getCreatedAt();
        this.updatedAt = blog.getUpdatedAt();
    }
}
