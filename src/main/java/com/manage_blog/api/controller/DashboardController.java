package com.manage_blog.api.controller;

import com.manage_blog.api.model.DashboardResponse;
import com.manage_blog.api.model.WebResponse;
import com.manage_blog.api.service.DashboardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public WebResponse<DashboardResponse> getDashboardData(
            @RequestHeader("Authorization") String token
    ) {
        try {
            String newtoken = token.substring(7);

            DashboardResponse dashboardResponse = dashboardService.getDashboardData(newtoken);
            return WebResponse.<DashboardResponse>builder()
                    .status(200)
                    .message("Success")
                    .data(dashboardResponse)
                    .build();
        } catch (Exception e) {
            return WebResponse.<DashboardResponse>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }

}
