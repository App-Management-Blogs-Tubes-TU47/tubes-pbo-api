package com.manage_blog.api.service.impl;

import com.manage_blog.api.entity.*;
import com.manage_blog.api.model.*;
import com.manage_blog.api.repository.*;
import com.manage_blog.api.service.*;
import com.manage_blog.api.utils.NullAwareBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository BlogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogCategoryRepository blogCategoryRepository;

    @Autowired
    private CreateSlugService createSlugService;

    @Autowired
    private StorageService storageService;

    @Transactional
    public ListResponse<List<BlogResponse>> getBlogList(int page, int size, String search) {

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Blog> blogPage= BlogRepository.findBySearch(search, pageRequest);

        List<BlogResponse> blogResponse = blogPage.getContent().stream()
                .map(blog -> {
                    BlogResponse response = new BlogResponse(blog);
                    response.setTumbnailUrl(storageService.getFileUrl(blog.getTumbnail()));
                    return response;
                })
                .toList();

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setPage(page);
        paginationResponse.setSize(size);
        paginationResponse.setTotalRecords((int) blogPage.getTotalElements());
        paginationResponse.setTotalPages(blogPage.getTotalPages());

        return new ListResponse<>(blogResponse, paginationResponse);
    }

    @Transactional
    public BlogResponse getBlogBySlugs(String slugs) {
        Blog blog = BlogRepository.findBlogBySlugs(slugs)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

        BlogResponse response = new BlogResponse(blog);
        response.setTumbnailUrl(storageService.getFileUrl(blog.getTumbnail()));

        return response;
    }

    @Transactional
    public BlogResponse createBlog(BlogRequest blogRequest) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(formatter);
        Users user = userRepository.findByUsername(blogRequest.getAuthor())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        BlogCategory blogCategory = blogCategoryRepository.findBlogCategoryBySlugs(blogRequest.getCategory())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog category not found"));

        String blogSlugs = createSlugService.createSlug(blogRequest.getTitle());

        String tumbnailName = null;

        if(blogRequest.getTumbnail() != null){
            tumbnailName = "blog/" + formattedDate + "/" + blogSlugs + "-tumbnail-" + blogRequest.getTumbnail().getOriginalFilename();
            storageService.uploadFile(tumbnailName,blogRequest.getTumbnail().getInputStream(), blogRequest.getTumbnail().getContentType());
        }

        Blog blog = Blog.builder()
                .title(blogRequest.getTitle())
                .slugs(blogSlugs)
                .tumbnail(tumbnailName)
                .article(blogRequest.getArticle())
                .status(blogRequest.getStatus())
                .user(user)
                .category(blogCategory)
                .createdAt(String.valueOf(System.currentTimeMillis()))
                .build();

        Blog savedBlog = BlogRepository.save(blog);


        BlogResponse response = new BlogResponse(savedBlog);
        response.setTumbnailUrl(storageService.getFileUrl(savedBlog.getTumbnail()));
        return response;
    }

    @Transactional
    public BlogResponse updateBlog(String slugs, BlogRequest blogRequest) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(formatter);
        Blog blog = BlogRepository.findBlogBySlugs(slugs)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        NullAwareBeanUtils.copyNonNullProperties(blogRequest, blog);

        if (blogRequest.getCategory() != null) {
            BlogCategory blogCategory = blogCategoryRepository.findBlogCategoryBySlugs(blogRequest.getCategory())
                    .orElseThrow(() -> new RuntimeException("Blog category not found"));
            blog.setCategory(blogCategory);
        }

        if (blogRequest.getAuthor() != null) {
            Users user = userRepository.findByUsername(blogRequest.getAuthor())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            blog.setUser(user);
        }

        if (blogRequest.getTumbnail() != null) {

            if (blog.getTumbnail() != null) {
                storageService.deleteFile(blog.getTumbnail());
            }

            String tumbnailName = "blog/" + formattedDate + "/" + slugs + "-tumbnail-" + blogRequest.getTumbnail().getOriginalFilename();
            storageService.uploadFile(tumbnailName, blogRequest.getTumbnail().getInputStream(), blogRequest.getTumbnail().getContentType());
            blog.setTumbnail(tumbnailName);
        }

        BlogRepository.save(blog);

        BlogResponse response = new BlogResponse(blog);
        response.setTumbnailUrl(storageService.getFileUrl(blog.getTumbnail()));

        return response;
    }

    @Transactional
    public void deleteBlog(String slugs) {
        Blog blog = BlogRepository.findBlogBySlugs(slugs)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

        blog.setDeletedAt(String.valueOf(System.currentTimeMillis()));
        BlogRepository.save(blog);
    }
}
