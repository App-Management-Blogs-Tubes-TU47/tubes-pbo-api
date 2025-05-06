package com.manage_blog.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.manage_blog.api.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponse {
    private String id;
    private String comment;
    private BlogResponse blog;
    private String blogSlug;
    private String blogTitle;
    private UserResponse user;
    private String userName;
    private String userProfile;
    private String createdAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId().toString();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt().toString();
    }
}
