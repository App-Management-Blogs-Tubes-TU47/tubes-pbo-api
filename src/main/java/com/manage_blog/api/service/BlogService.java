package com.manage_blog.api.service;

import com.manage_blog.api.model.BlogRequest;
import com.manage_blog.api.model.BlogResponse;
import com.manage_blog.api.model.ListResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface BlogService {
    ListResponse<List<BlogResponse>> getBlogList(
            int page, int size, String search
    );

    BlogResponse getBlogBySlugs(String slugs);

    BlogResponse createBlog(BlogRequest BlogRequest) throws IOException;

    BlogResponse updateBlog(String slugs, BlogRequest BlogRequest) throws IOException;

    void deleteBlog(String slugs);

}
