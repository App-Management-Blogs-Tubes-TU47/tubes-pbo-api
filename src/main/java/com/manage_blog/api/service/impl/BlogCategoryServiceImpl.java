package com.manage_blog.api.service.impl;

import com.manage_blog.api.entity.BlogCategory;
import com.manage_blog.api.model.*;
import com.manage_blog.api.repository.BlogCategoryRepository;
import com.manage_blog.api.service.BlogCategoryService;
import com.manage_blog.api.service.CreateSlugService;
import com.manage_blog.api.utils.NullAwareBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class BlogCategoryServiceImpl implements BlogCategoryService {

    @Autowired
    private BlogCategoryRepository blogCategoryRepository;

    @Autowired
    private CreateSlugService createSlugService;

    @Transactional
    public ListResponse<List<BlogCategoryResponse>> getCategoryList(int page, int size, String search) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<BlogCategory> categoryPage= blogCategoryRepository.findBySearch(search, pageRequest);

        List<BlogCategoryResponse> categoryResponse = categoryPage.getContent().stream()
                .map(BlogCategoryResponse::new)
                .toList();

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .page(page)
                .size(size)
                .totalRecords((int) categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .build();

        return new ListResponse<>(categoryResponse, paginationResponse);
    }

    @Transactional
    public BlogCategoryResponse getCategoryBySlugs(String slugs) {
        BlogCategory blogCategory = blogCategoryRepository.findBlogCategoryBySlugs(slugs)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog category not found"));

        return new BlogCategoryResponse(blogCategory);
    }

    @Transactional
    public BlogCategoryResponse createCategory(BlogCategoryRequest blogCategoryRequest) {

//        if (blogCategoryRepository.findExistingByName(blogCategoryRequest.getName())) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Blog category already exists");
//        }

        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setName(blogCategoryRequest.getName());
        blogCategory.setSlugs(
                createSlugService.createSlug(blogCategoryRequest.getName())
        );

        blogCategoryRepository.save(blogCategory);

        return new BlogCategoryResponse(blogCategory);
    }

    @Transactional
    public BlogCategoryResponse updateCategory(String slugs, BlogCategoryRequest blogCategoryRequest) {
        BlogCategory blogCategory = blogCategoryRepository.findBlogCategoryBySlugs(slugs)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Blog category not found"
                ));

        NullAwareBeanUtils.copyNonNullProperties(blogCategory, blogCategory);

//        if (blogCategoryRepository.findExistingByName(blogCategoryRequest.getName())) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Blog category already exists");
//        }

        blogCategory.setName(blogCategoryRequest.getName());
        blogCategory.setSlugs(
                createSlugService.createSlug(blogCategoryRequest.getName())
        );

        blogCategoryRepository.save(blogCategory);

        return new BlogCategoryResponse(blogCategory);
    }

    @Transactional
    public void deleteCategory(String slugs) {
        BlogCategory blogCategory = blogCategoryRepository.findBlogCategoryBySlugs(slugs)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Blog category not found"
                ));
        blogCategory.setDeletedAt(LocalDateTime.now());
        blogCategoryRepository.save(blogCategory);
    }
}
