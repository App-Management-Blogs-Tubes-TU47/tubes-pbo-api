package com.manage_blog.api.service.impl;

import com.manage_blog.api.entity.Blog;
import com.manage_blog.api.entity.Comment;
import com.manage_blog.api.entity.Users;
import com.manage_blog.api.model.*;
import com.manage_blog.api.repository.BlogRepository;
import com.manage_blog.api.repository.CommentRepository;
import com.manage_blog.api.repository.UserRepository;
import com.manage_blog.api.service.CommentService;
import com.manage_blog.api.service.StorageService;
import com.manage_blog.api.utils.NullAwareBeanUtils;
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
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private StorageService storageService;

    @Override
    @Transactional
    public ListResponse<List<CommentResponse>> getCommentList(String blogSlug) {
        List<Comment> comments = commentRepository.selectCommentByBlog(blogSlug);
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> {
                    CommentResponse response = new CommentResponse(comment);

                    response.setUserName(comment.getUser().getName());
                    if(comment.getUser().getProfile() != null)
                        response.setUserProfile(storageService.getFileUrl(comment.getUser().getProfile()));

                    response.setBlogTitle(comment.getBlog().getTitle());
                    response.setBlogSlug(comment.getBlog().getSlugs());
                    return response;
                })
                .toList();

        return new ListResponse<>(commentResponses, null);
    }

    @Override
    @Transactional
    public CommentResponse createComment(CommentRequest commentRequest) {
        Users user = userRepository.findByUsername(commentRequest.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Blog blog = blogRepository.findBlogBySlugs(commentRequest.getBlogSlug())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

        Comment comment = Comment.builder()
                .comment(commentRequest.getComment())
                .user(user)
                .blog(blog)
                .build();

        commentRepository.save(comment);

        CommentResponse response = new CommentResponse(comment);
        response.setUser(new UserResponse(user));
        response.setBlog(new BlogResponse(blog));

        return response;
    }

    @Override
    @Transactional
    public CommentResponse updateComment(String commentId, CommentRequest commentRequest) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        NullAwareBeanUtils.copyNonNullProperties(commentRequest, comment);


        Users user = userRepository.findByUsername(commentRequest.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Blog blog = blogRepository.findBlogBySlugs(commentRequest.getBlogSlug())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

        commentRepository.save(comment);

        CommentResponse response = new CommentResponse(comment);
        response.setUser(new UserResponse(user));
        response.setBlog(new BlogResponse(blog));

        return response;
    }

    @Override
    @Transactional
    public void deleteComment(String commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        comment.setDeletedAt(LocalDateTime.now());
        commentRepository.save(comment);

    }
}
