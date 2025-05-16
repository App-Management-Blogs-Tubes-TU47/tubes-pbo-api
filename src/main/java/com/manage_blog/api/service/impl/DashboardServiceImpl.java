package com.manage_blog.api.service.impl;

import com.manage_blog.api.entity.Users;
import com.manage_blog.api.enums.RoleEnum;
import com.manage_blog.api.model.*;
import com.manage_blog.api.repository.BlogRepository;
import com.manage_blog.api.repository.UserRepository;
import com.manage_blog.api.service.DashboardService;
import com.manage_blog.api.service.StorageService;
import com.manage_blog.api.utils.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private StorageService storageService;

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);

    @Override
    @Transactional
    public DashboardResponse getDashboardData(String token) {

        logger.info("Testing Dashboard Service");

        String username = jwtUtils.getUsernameFromToken(token);

        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAdmin = users.getRole() == RoleEnum.ADMIN;
        String targetUsername = isAdmin ? null : users.getUsername();

        List<DashboardUserLeaderboardResponse> userResponse = userRepository.findUsersByCurrentYear().stream()
                .map(user -> {
                    DashboardUserLeaderboardResponse response = new DashboardUserLeaderboardResponse(user);
                    if(user.getProfileUrl() != null) response.setProfileUrl(storageService.getFileUrl(user.getProfileUrl()));
                    return response;
                })
                .toList();



//        List<UserResponse> userResponses = userRepository.findLatestUsersByCreatedAtDesc().stream()
//                .map(UserResponse::new)
//                .toList();
        List<BlogResponse> blogResponses = blogRepository.listBlogLatestByCreatedAtDesc(targetUsername).stream()
                .map(BlogResponse::new)
                .toList();


//        chart for blogs
        List<DashboardBlogCountResponse> blogCountResponse = blogRepository.listBlogCountByMonthInCurrentYear(targetUsername);
        Map<Integer, Long> countMap = blogCountResponse.stream()
                .collect(Collectors.toMap(DashboardBlogCountResponse::getTitle, DashboardBlogCountResponse::getCount));

        List<DashboardBlogCountResponse> completeList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            completeList.add(new DashboardBlogCountResponse(i, countMap.getOrDefault(i, 0L)));
        }

        return DashboardResponse.builder()
                .leaderboard(userResponse)
//                .users(userResponses)
                .countBlogs(completeList)
                .blogs(blogResponses)
                .build();
    }

}
