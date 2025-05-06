package com.manage_blog.api.repository;

import com.manage_blog.api.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String>, JpaSpecificationExecutor<Comment> {

    @Query("SELECT c FROM Comment c WHERE " +
                    "c.blog.slugs = :blogs")
    List<Comment> selectCommentByBlog(@Param("blogs") String blogs);

}
