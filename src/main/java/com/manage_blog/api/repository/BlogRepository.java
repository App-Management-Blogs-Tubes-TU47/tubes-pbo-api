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
            "LOWER(u.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.category.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.category.slugs) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.user.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.slugs) LIKE LOWER(CONCAT('%', :search, '%')) )" +
            " AND (:category IS NULL OR :category = '' OR LOWER(u.category.slugs) = LOWER(:category))" +
            " AND (:author IS NULL OR :author = '' OR LOWER(u.user.username) = LOWER(:author))"
    )
    Page<Blog> findBySearch(@Param("search") String search, PageRequest pageRequest,
                            @Param("category") String category, @Param("author") String author);

    @Query("""
            SELECT b AS blog, COUNT(c) AS commentCount
            FROM Blog b
            LEFT JOIN Comment c ON c.blog = b
            WHERE b.deletedAt IS NULL
              AND (:search IS NULL OR :search = '' 
                   OR LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) 
                   OR LOWER(b.slugs) LIKE LOWER(CONCAT('%', :search, '%')))
            GROUP BY b """)
    Page<Blog> findBySearchWithCommentCount(@Param("search") String search, PageRequest pageable);


    @Query("SELECT b FROM Blog b WHERE b.slugs = :slugs"
    + " AND b.deletedAt IS NULL")
    Optional<Blog> findBlogBySlugs(@Param("slugs") String slugs);

}


