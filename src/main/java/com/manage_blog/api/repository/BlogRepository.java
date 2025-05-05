package com.manage_blog.api.repository;

import com.manage_blog.api.entity.Blog;
import com.manage_blog.api.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, String>, JpaSpecificationExecutor<Blog> {

    @Query("SELECT u FROM Blog u WHERE " +
            "u.deletedAt IS NULL AND (" +
            ":search IS NULL OR :search = '' OR " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.slugs) LIKE LOWER(CONCAT('%', :search, '%')) )")
    Page<Blog> findBySearch(@Param("search") String search, PageRequest pageRequest);

    @Query("SELECT b FROM Blog b WHERE b.slugs = :slugs")
    Optional<Blog> findBlogBySlugs(@Param("slugs") String slugs);

}


