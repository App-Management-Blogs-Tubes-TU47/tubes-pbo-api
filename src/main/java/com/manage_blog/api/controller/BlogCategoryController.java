package com.manage_blog.api.controller;

import com.manage_blog.api.model.BlogCategoryRequest;
import com.manage_blog.api.model.BlogCategoryResponse;
import com.manage_blog.api.model.ListResponse;
import com.manage_blog.api.model.WebResponse;
import com.manage_blog.api.service.BlogCategoryService;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blog-categories")
public class BlogCategoryController {
    private BlogCategoryService blogCategoryService;

    public BlogCategoryController(BlogCategoryService blogCategoryService) {
        this.blogCategoryService = blogCategoryService;
    }

    // ==========================
    // Get All Blog Categories
    // @Params page, size, search
    // ==========================

    @GetMapping
    public WebResponse<ListResponse<List<BlogCategoryResponse>>> getAllBlogCategories(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "search", required = false) String search
    ) {
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 10;
        }
        ListResponse<List<BlogCategoryResponse>> listResponse = blogCategoryService.getCategoryList(
                page,
                size,
                search
        );
        return WebResponse.<ListResponse<List<BlogCategoryResponse>>>builder()
                .status(200)
                .message("Success")
                .data(listResponse)
                .build();
    }

    // ==========================
    // Get Blog Category By Slugs
    // @Params slugs
    // ==========================
    @GetMapping("/{slugs}")
    public WebResponse<BlogCategoryResponse> getBlogCategoryBySlugs(
            @PathVariable("slugs") String slugs
    ) {
            BlogCategoryResponse blogCategoryResponse = blogCategoryService.getCategoryBySlugs(slugs);
            return WebResponse.<BlogCategoryResponse>builder()
                    .status(200)
                    .message("Success")
                    .data(blogCategoryResponse)
                    .build();
    }

    // ==========================
    // Create Blog Category
    // @Body blogCategoryRequest
    // ==========================
    @PostMapping
    public WebResponse<BlogCategoryResponse> createBlogCategory(
            @RequestBody BlogCategoryRequest blogCategoryRequest
    ) {
            BlogCategoryResponse blogCategoryResponse = blogCategoryService.createCategory(blogCategoryRequest);
            return WebResponse.<BlogCategoryResponse>builder()
                    .status(201)
                    .message("Success")
                    .data(blogCategoryResponse)
                    .build();
    }


    // ==========================
    // Update Blog Category
    // @Params slugs
    // @Body blogCategoryRequest
    // ==========================
    @PatchMapping("/{slugs}")
    public WebResponse<BlogCategoryResponse> updateBlogCategory(
            @PathVariable("slugs") String slugs,
            @RequestBody BlogCategoryRequest blogCategoryRequest
    ) {
            BlogCategoryResponse blogCategoryResponse = blogCategoryService.updateCategory(slugs, blogCategoryRequest);
            return WebResponse.<BlogCategoryResponse>builder()
                    .status(200)
                    .message("Success")
                    .data(blogCategoryResponse)
                    .build();
    }

    // ==========================
    // Delete Blog Category
    // @Params slugs
    // ==========================
    @DeleteMapping("/{slugs}")
    public WebResponse<String> deleteBlogCategory(
            @PathVariable("slugs") String slugs
    ) {
            blogCategoryService.deleteCategory(slugs);
            return WebResponse.<String>builder()
                    .status(200)
                    .message("Success")
                    .data("Blog category with slugs " + slugs + " deleted successfully")
                    .build();
    }


}
