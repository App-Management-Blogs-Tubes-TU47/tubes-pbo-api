package com.manage_blog.api.controller;

import com.manage_blog.api.enums.StatusEnum;
import com.manage_blog.api.model.BlogRequest;
import com.manage_blog.api.model.BlogResponse;
import com.manage_blog.api.model.ListResponse;
import com.manage_blog.api.model.WebResponse;
import com.manage_blog.api.service.BlogService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/blogs")
public class BlogController {
    private BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

//    =========================
//    Get All Blogs
//    @Params page, size, search
//    =========================
    @GetMapping
    public WebResponse<ListResponse<List<BlogResponse>>> getAllBlogs(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam (value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam (value = "search", required = false) String search,
            @RequestParam (value = "category", required = false) String category,
            @RequestParam (value = "author", required = false) String author,
            @RequestParam(value = "status", required = false) StatusEnum status
    ) {
        try {
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
        } catch (Exception e) {
            return WebResponse.<ListResponse<List<BlogResponse>>>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }

//    =========================
//    Get Blog By Slugs
//    @Params slugs
//    =========================
    @GetMapping("/{slugs}")
    public WebResponse<BlogResponse> getBlogBySlugs(
            @PathVariable("slugs") String slugs
    ) {
        try {
            BlogResponse blogResponse = blogService.getBlogBySlugs(slugs);
            return WebResponse.<BlogResponse>builder()
                    .status(200)
                    .message("Success")
                    .data(blogResponse)
                    .build();
        } catch (Exception e) {
            return WebResponse.<BlogResponse>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }

//    =========================
//    Create Blog
//    @Body title, tumbnail, article, status, author, category
//    =========================
     @PostMapping(
             consumes = MediaType.MULTIPART_FORM_DATA_VALUE
     )
     public WebResponse<BlogResponse> createBlog(
             @ModelAttribute BlogRequest blogRequest
     ) throws IOException {
         BlogResponse blogResponse = blogService.createBlog(blogRequest);
         return WebResponse.<BlogResponse>builder()
                 .status(200)
                 .message("Success")
                 .data(blogResponse)
                 .build();
     }

//    =========================
//    Update Blog
//    @Params slugs
//    @Body title, tumbnail, article, status, author, category
//    =========================
     @PatchMapping(value = "/{slugs}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     public WebResponse<BlogResponse> updateBlog(
             @PathVariable("slugs") String slugs,
             @ModelAttribute BlogRequest blogRequest
     ) throws IOException {
         BlogResponse blogResponse = blogService.updateBlog(slugs, blogRequest);
         return WebResponse.<BlogResponse>builder()
                 .status(200)
                 .message("Success")
                 .data(blogResponse)
                 .build();
     }

//    =========================
//    Delete Blog
//    @Params slugs
//    =========================
     @DeleteMapping("/{slugs}")
     public WebResponse<String> deleteBlog(
             @PathVariable("slugs") String slugs
     ) {
        try {
            blogService.deleteBlog(slugs);
            return WebResponse.<String>builder()
                    .status(200)
                    .message("Success")
                    .data("Blog deleted successfully")
                    .build();
        } catch (Exception e) {
            return WebResponse.<String>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
     }


}
