package com.manage_blog.api.model;

import com.manage_blog.api.entity.Users;
import com.manage_blog.api.enums.RoleEnum;
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
public class DashboardUserLeaderboardResponse {
    private UUID id;
    private String name;
    private String username;
    private String email;
    private String profileUrl;
    private LocalDateTime createdAt;
    private long blogs;

    // Constructor untuk digunakan di JPQL (pastikan tipe sesuai!)
    public DashboardUserLeaderboardResponse(UUID id, String name, String username, String email, String profileUrl, LocalDateTime createdAt, Long blogs) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.profileUrl = profileUrl;
        this.createdAt = createdAt;
        this.blogs = blogs != null ? blogs : 0L;
    }

    public DashboardUserLeaderboardResponse(DashboardUserLeaderboardResponse user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profileUrl = user.getProfileUrl();
        this.createdAt = user.getCreatedAt();
        this.blogs = user.getBlogs();
    }
}
