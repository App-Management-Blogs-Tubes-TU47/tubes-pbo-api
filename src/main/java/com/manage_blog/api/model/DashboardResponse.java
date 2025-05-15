package com.manage_blog.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardResponse {

    private List<BlogResponse> blogs;

    private List<DashboardBlogCountResponse> countBlogs;

    private List<UserResponse> users;

    private List<DashboardUserLeaderboardResponse> leaderboard;

    public DashboardResponse (DashboardResponse dashboardResponse) {
        this.blogs = dashboardResponse.getBlogs();
        this.countBlogs = dashboardResponse.getCountBlogs();
        this.users = dashboardResponse.getUsers();
        this.leaderboard = dashboardResponse.getLeaderboard();
    }

}
