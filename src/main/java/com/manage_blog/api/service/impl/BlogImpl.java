package com.manage_blog.api.service.impl;

import com.manage_blog.api.entity.Blog;
import com.manage_blog.api.entity.Users;
import com.manage_blog.api.model.*;
import com.manage_blog.api.repository.BlogRepository;
import com.manage_blog.api.service.BlogService;
import com.manage_blog.api.service.CreateSlugService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository BlogRepository;

    @Autowired
    private CreateSlugService createSlugService;

    @Override
    public ListResponse<List<BlogResponse>> getBlogList(int page, int size, String search) {

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Blog> blogPage= BlogRepository.findBySearch(search, pageRequest);

        List<BlogResponse> blogResponse = blogPage.getContent().stream()
                .map(blog -> new BlogResponse(blog))
                .toList();

        // Prepare pagination response
        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setPage(page);
        paginationResponse.setSize(size);
        paginationResponse.setTotalRecords((int) blogPage.getTotalElements());
        paginationResponse.setTotalPages(blogPage.getTotalPages());

        return new ListResponse<>(blogResponse, paginationResponse);
    }

    @Override
    public BlogResponse getBlogBySlugs(String slugs) {
        Blog Blog = BlogRepository.findBlogBySlugs(slugs)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        return new BlogResponse(Blog);
    }

    @Override
    public BlogResponse createBlog(BlogRequest BlogRequest) {
        Blog Blog = new Blog();
        Blog.setName(BlogRequest.getName());
        Blog.setSlugs(createSlugService.createSlug(BlogRequest.getName()));
        Blog.setCreatedAt(String.valueOf(System.currentTimeMillis()));

        BlogRepository.save(Blog);

        return new BlogResponse(Blog);
    }

    @Override
    public BlogResponse updateBlog(String slugs, BlogRequest BlogRequest) {
        Blog Blog = BlogRepository.findBlogBySlugs(slugs)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        Blog.setName(BlogRequest.getName());
        Blog.setSlugs(createSlugService.createSlug(BlogRequest.getName()));
        Blog.setUpdatedAt(String.valueOf(System.currentTimeMillis()));

        BlogRepository.save(Blog);

        return new BlogResponse(Blog);
    }

    @Override
    public void deleteBlog(String slugs) {
        Blog Blog = BlogRepository.findBlogBySlugs(slugs)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        Blog.setDeletedAt(String.valueOf(System.currentTimeMillis()));
        BlogRepository.save(Blog);
    }
}
