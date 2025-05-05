package com.manage_blog.api.repository;

import com.manage_blog.api.entity.BlogCategory;
import com.manage_blog.api.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlogCategoryRepository extends JpaRepository<BlogCategory, String>, JpaSpecificationExecutor<BlogCategory> {

    @Query("SELECT u FROM BlogCategory u WHERE " +
            "u.deletedAt IS NULL AND (" +
            ":search IS NULL OR :search = '' OR " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.slugs) LIKE LOWER(CONCAT('%', :search, '%')) )")
    Page<BlogCategory> findBySearch(@Param("search") String search, PageRequest pageRequest);

    @Query("SELECT b FROM BlogCategory b WHERE b.slugs = :slugs")
    Optional<BlogCategory> findBlogCategoryBySlugs(@Param("slugs") String slugs);

}


