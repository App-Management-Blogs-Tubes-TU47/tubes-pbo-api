package com.manage_blog.api.service;

import com.manage_blog.api.model.CommentRequest;
import com.manage_blog.api.model.CommentResponse;
import com.manage_blog.api.model.ListResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {

    ListResponse<List<CommentResponse>> getCommentList(String blogSlug);

    CommentResponse createComment(CommentRequest commentRequest);

    CommentResponse updateComment(String commentId, CommentRequest commentRequest);

    void deleteComment(String commentId);

}
