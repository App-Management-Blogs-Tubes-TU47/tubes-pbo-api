package com.manage_blog.api.service;

import com.manage_blog.api.model.BlogCategoryRequest;
import com.manage_blog.api.model.BlogCategoryResponse;
import com.manage_blog.api.model.ListResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BlogCategoryService {
    ListResponse<List<BlogCategoryResponse>> getCategoryList(
            int page, int size, String search
    );

    BlogCategoryResponse getCategoryBySlugs(String slugs);

    BlogCategoryResponse createCategory(BlogCategoryRequest blogCategoryRequest);

    BlogCategoryResponse updateCategory(String slugs, BlogCategoryRequest blogCategoryRequest);

    void deleteCategory(String slugs);

}
