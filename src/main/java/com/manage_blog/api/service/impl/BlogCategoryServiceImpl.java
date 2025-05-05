package com.manage_blog.api.service.impl;

import com.manage_blog.api.entity.BlogCategory;
import com.manage_blog.api.entity.Users;
import com.manage_blog.api.model.*;
import com.manage_blog.api.repository.BlogCategoryRepository;
import com.manage_blog.api.service.BlogCategoryService;
import com.manage_blog.api.service.CreateSlugService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BlogCategoryServiceImpl implements BlogCategoryService {

    @Autowired
    private BlogCategoryRepository blogCategoryRepository;

    @Autowired
    private CreateSlugService createSlugService;

    @Override
    public ListResponse<List<BlogCategoryResponse>> getCategoryList(int page, int size, String search) {

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<BlogCategory> categoryPage= blogCategoryRepository.findBySearch(search, pageRequest);

        List<BlogCategoryResponse> categoryResponse = categoryPage.getContent().stream()
                .map(category -> new BlogCategoryResponse(category))
                .toList();

        // Prepare pagination response
        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setPage(page);
        paginationResponse.setSize(size);
        paginationResponse.setTotalRecords((int) categoryPage.getTotalElements());
        paginationResponse.setTotalPages(categoryPage.getTotalPages());

        return new ListResponse<>(categoryResponse, paginationResponse);
    }

    @Override
    public BlogCategoryResponse getCategoryBySlugs(String slugs) {
        return null;
    }

    @Override
    public BlogCategoryResponse createCategory(BlogCategoryRequest blogCategoryRequest) {
        return null;
    }

    @Override
    public BlogCategoryResponse updateCategory(String slugs, BlogCategoryRequest blogCategoryRequest) {
        return null;
    }

    @Override
    public void deleteCategory(String slugs) {

    }
}
