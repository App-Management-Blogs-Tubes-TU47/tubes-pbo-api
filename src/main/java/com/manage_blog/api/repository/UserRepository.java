package com.manage_blog.api.repository;

import com.manage_blog.api.entity.Users;
import com.manage_blog.api.model.DashboardUserLeaderboardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String>, JpaSpecificationExecutor<Users> {

    @Query("SELECT u FROM Users u WHERE (" +
            ":search IS NULL OR :search = '' OR " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) )")
    Page<Users> findBySearch(@Param("search") String search, PageRequest pageRequest);

    @Query("SELECT u FROM Users u WHERE u.username = :username"
    + " AND u.deletedAt IS NULL")
    Optional<Users> findByUsername(@Param("username") String username);

    @Query(
        "SELECT u FROM Users u WHERE u.email = :email"
            + " OR u.username = :username"
    )
    Optional<Users> validateUnameEmailAlreadyExist(@Param("email") String email, @Param("username") String username );

    @Query("""
        SELECT new com.manage_blog.api.model.DashboardUserLeaderboardResponse(
            u.id, u.name, u.username, u.email, u.profile, u.createdAt, COUNT(b)
        )
        FROM Users u
        LEFT JOIN Blog b ON b.user = u AND year(b.createdAt) = year(CURRENT_DATE)
        WHERE year(b.createdAt) = year(CURRENT_DATE)
        GROUP BY u.id, u.name, u.username, u.email, u.profile, u.createdAt
        ORDER BY COUNT(b) DESC
    """)
    List<DashboardUserLeaderboardResponse> findUsersByCurrentYear();

    @Query("""
        SELECT u
        FROM Users u
        WHERE u.createdAt >= CURRENT_DATE
        ORDER BY u.createdAt DESC
    """)
    List<Users> findLatestUsersByCreatedAtDesc();

}
