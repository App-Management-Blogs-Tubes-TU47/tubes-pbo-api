package com.manage_blog.api.repository;

import com.manage_blog.api.entity.Blog;
import com.manage_blog.api.entity.Users;
import com.manage_blog.api.enums.StatusEnum;
import com.manage_blog.api.model.DashboardBlogCountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;
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
            " AND (:status IS NULL OR :status = '' OR u.status = :status)" +
            " AND (:category IS NULL OR :category = '' OR LOWER(u.category.slugs) = LOWER(:category))" +
            " AND (:author IS NULL OR :author = '' OR LOWER(u.user.username) = LOWER(:author))"
    )
    Page<Blog> findBySearch(@Param("search") String search, PageRequest pageRequest,
                            @Param("category") String category, @Param("author") String author, @Param("status")StatusEnum status);

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

    @Query("""
        SELECT new com.manage_blog.api.model.DashboardBlogCountResponse(
             MONTH(b.createdAt) - 1, COUNT(b)
        )
        FROM Blog b
        WHERE year(b.createdAt) = year(CURRENT_DATE)
        GROUP BY MONTH(b.createdAt)
        ORDER BY MONTH(b.createdAt)
""")
    List<DashboardBlogCountResponse> listBlogCountByMonthInCurrentYear();

    @Query("""
        SELECT b FROM Blog b
        WHERE b.user.username = :username
        AND b.createdAt >= CURRENT_DATE
        ORDER BY b.createdAt DESC
""")
    List<Blog> listBlogLatestByCreatedAtDesc(@Param("username") String username);
}


