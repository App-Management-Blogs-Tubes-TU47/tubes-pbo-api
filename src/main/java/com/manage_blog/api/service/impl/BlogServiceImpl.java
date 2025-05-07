package com.manage_blog.api.service.impl;

import com.manage_blog.api.entity.*;
import com.manage_blog.api.enums.StatusEnum;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogCategoryRepository blogCategoryRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CreateSlugService createSlugService;

    @Autowired
    private StorageService storageService;

    @Override
    @Transactional
    public ListResponse<List<BlogResponse>> getBlogList(int page, int size, String search, String category, String author, StatusEnum status) {

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Blog> blogPage = blogRepository.findBySearch(search, pageRequest, category, author, status);

        List<BlogResponse> blogResponse = blogPage.getContent().stream()
                .map(blog -> {
                    long commentCount = commentRepository.countCommentByBlog(blog.getSlugs());
                    BlogResponse response = new BlogResponse(blog);
                    if (blog.getTumbnail() != null) response.setTumbnailUrl(storageService.getFileUrl(blog.getTumbnail()));
                    response.setCountComments(commentCount);
                    response.setAuthorName(blog.getUser().getName());
                    response.setAuthorUsername(blog.getUser().getUsername());
                    response.setCategoryName(blog.getCategory().getName());
                    response.setCategorySlugs(blog.getCategory().getSlugs());
                    return response;
                })
                .toList();

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .page(page)
                .size(size)
                .totalRecords((int) blogPage.getTotalElements())
                .totalPages(blogPage.getTotalPages())
                .build();

        return new ListResponse<>(blogResponse, paginationResponse);
    }

    @Override
    @Transactional
    public BlogResponse getBlogBySlugs(String slugs) {
        Blog blog = blogRepository.findBlogBySlugs(slugs)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

        BlogResponse response = new BlogResponse(blog);
        if (blog.getTumbnail() != null) response.setTumbnailUrl(storageService.getFileUrl(blog.getTumbnail()));

        Users user = blog.getUser();
        UserResponse userResponse = new UserResponse(user);
        if (user.getProfile() != null) {
            userResponse.setProfileUrl(storageService.getFileUrl(user.getProfile()));
        }
        response.setAuthor(userResponse);
        response.setCategory(new BlogCategoryResponse(blog.getCategory()));

        return response;
    }

    @Override
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

        if(blogRequest.getThumbnailFile() != null){
            tumbnailName = "blog/" + formattedDate + "/" + blogSlugs + "-tumbnail-" + blogRequest.getThumbnailFile().getOriginalFilename();
            storageService.uploadFile(tumbnailName,blogRequest.getThumbnailFile().getInputStream(), blogRequest.getThumbnailFile().getContentType());
        }

        Blog blog = Blog.builder()
                .title(blogRequest.getTitle())
                .slugs(blogSlugs)
                .tumbnail(tumbnailName)
                .article(blogRequest.getArticle())
                .status(blogRequest.getStatus())
                .user(user)
                .category(blogCategory)
                .build();

        Blog savedBlog = blogRepository.save(blog);

        BlogResponse response = new BlogResponse(savedBlog);
        if (savedBlog.getTumbnail() != null) response.setTumbnailUrl(storageService.getFileUrl(savedBlog.getTumbnail()));

        UserResponse userResponse = new UserResponse(user);
        if (user.getProfile() != null) {
            userResponse.setProfileUrl(storageService.getFileUrl(user.getProfile()));
        }
        response.setAuthor(userResponse);
        response.setCategory(new BlogCategoryResponse(savedBlog.getCategory()));

        return response;
    }

    @Override
    @Transactional
    public BlogResponse updateBlog(String slugs, BlogRequest blogRequest) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(formatter);

        Blog blog = blogRepository.findBlogBySlugs(slugs)
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

        if (blogRequest.getThumbnailFile() != null) {

            if (blog.getTumbnail() != null) {
                storageService.deleteFile(blog.getTumbnail());
            }

            String tumbnailName = "blog/" + formattedDate + "/" + slugs + "-tumbnail-" + blogRequest.getThumbnailFile().getOriginalFilename();
            storageService.uploadFile(tumbnailName, blogRequest.getThumbnailFile().getInputStream(), blogRequest.getThumbnailFile().getContentType());
            blog.setTumbnail(tumbnailName);
        }

        blogRepository.save(blog);

        BlogResponse response = new BlogResponse(blog);
        if (blog.getTumbnail() != null) response.setTumbnailUrl(storageService.getFileUrl(blog.getTumbnail()));

        Users user = blog.getUser();
        UserResponse userResponse = new UserResponse(user);
        if (user.getProfile() != null) {
            userResponse.setProfileUrl(storageService.getFileUrl(user.getProfile()));
        }
        response.setAuthor(userResponse);
        response.setCategory(new BlogCategoryResponse(blog.getCategory()));

        return response;
    }

    @Override
    @Transactional
    public void deleteBlog(String slugs) {
        Blog blog = blogRepository.findBlogBySlugs(slugs)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

        if (blog.getTumbnail() != null) {
            storageService.deleteFile(blog.getTumbnail());
        }
        blog.setDeletedAt(LocalDateTime.now());
        blogRepository.save(blog);
    }
}
