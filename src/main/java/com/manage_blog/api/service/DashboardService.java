package com.manage_blog.api.service;

import com.manage_blog.api.model.DashboardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {
    DashboardResponse getDashboardData(String token);
}
