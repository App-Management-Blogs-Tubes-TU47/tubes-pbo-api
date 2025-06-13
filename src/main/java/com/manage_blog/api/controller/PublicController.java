package com.manage_blog.api.controller;

import com.manage_blog.api.enums.StatusEnum;
import com.manage_blog.api.model.*;
import com.manage_blog.api.service.BlogCategoryService;
import com.manage_blog.api.service.BlogService;
import com.manage_blog.api.service.CommentService;
import com.manage_blog.api.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {
    private BlogCategoryService blogCategoryService;
    private BlogService blogService;
    private CommentService commentService;
    private UserService userService;

    public PublicController(
            BlogCategoryService blogCategoryService,
            BlogService blogService,
            CommentService commentService,
            UserService userService
    ) {
        this.blogCategoryService = blogCategoryService;
        this.blogService = blogService;
        this.commentService = commentService;
        this.userService = userService;
    }

    // ==========================
    // Get All Blog Categories
    // @Params page, size, search
    // ==========================
    @GetMapping("/blog-categories")
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


    //    =========================
    //    Get All Blogs
    //    @Params page, size, search
    //    =========================
    @GetMapping("/blogs")
    public WebResponse<ListResponse<List<BlogResponse>>> getAllBlogs(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam (value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam (value = "search", required = false) String search,
            @RequestParam (value = "category", required = false) String category,
            @RequestParam (value = "author", required = false) String author,
            @RequestParam(value = "status", required = false) StatusEnum status
    ) {
            if (page < 1){
                page = 1;
            }
            if (size < 1){
                size = 10;
            }
            ListResponse<List<BlogResponse>> listResponse = blogService.getBlogList(
                    page,
                    size,
                    search,
                    category,
                    author,
                    status
            );
            return WebResponse.<ListResponse<List<BlogResponse>>>builder()
                    .status(200)
                    .message("Success")
                    .data(listResponse)
                    .build();
    }


    //    =========================
//    Get Blog By Slugs
//    @Params slugs
//    =========================
    @GetMapping("/blogs/{slugs}")
    public WebResponse<BlogResponse> getBlogBySlugs(
            @PathVariable("slugs") String slugs
    ) {
            BlogResponse blogResponse = blogService.getBlogBySlugs(slugs);
            return WebResponse.<BlogResponse>builder()
                    .status(200)
                    .message("Success")
                    .data(blogResponse)
                    .build();
    }

    //    =========================
    //    Get All Comments
    //    @Params blogSlug
    //    =========================
    @GetMapping("comments/{blogSlug}")
    public WebResponse<ListResponse<List<CommentResponse>>> getAllComments(
            @PathVariable("blogSlug") String blogSlug
    ) {
            ListResponse<List<CommentResponse>> listResponse = commentService.getCommentList(
                    blogSlug
            );
            return WebResponse.<ListResponse<List<CommentResponse>>>builder()
                    .status(200)
                    .message("Success")
                    .data(listResponse)
                    .build();
    }

    //    =========================
    //    Get User By ID
    //    @Params id
    //    =========================
    @GetMapping("/author/{username}")
    public WebResponse<UserResponse> getUserByUsername(
            @PathVariable("username") String username
    ) {
            UserResponse userResponse = userService.getUserByUsername(username);
            return WebResponse.<UserResponse>builder()
                    .status(200)
                    .message("Success")
                    .data(userResponse)
                    .build();
    }
}
