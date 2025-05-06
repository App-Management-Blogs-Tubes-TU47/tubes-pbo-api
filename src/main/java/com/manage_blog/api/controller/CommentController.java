package com.manage_blog.api.controller;

import com.manage_blog.api.model.CommentRequest;
import com.manage_blog.api.model.CommentResponse;
import com.manage_blog.api.model.ListResponse;
import com.manage_blog.api.model.WebResponse;
import com.manage_blog.api.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

//    =========================
//    Get All Comments
//    @Params blogSlug
//    =========================
    @GetMapping("/{blogSlug}")
    public WebResponse<ListResponse<List<CommentResponse>>> getAllComments(
            @PathVariable("blogSlug") String blogSlug
    ) {
        try {
            ListResponse<List<CommentResponse>> listResponse = commentService.getCommentList(
                    blogSlug
            );
            return WebResponse.<ListResponse<List<CommentResponse>>>builder()
                    .status(200)
                    .message("Success")
                    .data(listResponse)
                    .build();
        } catch (Exception e) {
            return WebResponse.<ListResponse<List<CommentResponse>>>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }

    @PostMapping
    public WebResponse<CommentResponse> createComment(
            @RequestBody CommentRequest commentRequest
    ) {
        try {
            CommentResponse response = commentService.createComment(commentRequest);
            return WebResponse.<CommentResponse>builder()
                    .status(200)
                    .message("Success")
                    .data(response)
                    .build();
        } catch (Exception e) {
            return WebResponse.<CommentResponse>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }

    @PatchMapping("/{id}")
    public WebResponse<CommentResponse> updateComment(
            @PathVariable("id") String id,
            @RequestBody CommentRequest commentRequest
    ) {
        try {
            CommentResponse response = commentService.updateComment(id, commentRequest);
            return WebResponse.<CommentResponse>builder()
                    .status(200)
                    .message("Success")
                    .data(response)
                    .build();
        } catch (Exception e) {
            return WebResponse.<CommentResponse>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    public WebResponse<String> deleteComment(
            @PathVariable("id") String id
    ) {
        try {
            commentService.deleteComment(id);
            return WebResponse.<String>builder()
                    .status(200)
                    .message("Success")
                    .data("Comment with id " + id + " has been deleted")
                    .build();
        } catch (Exception e) {
            return WebResponse.<String>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }
}
