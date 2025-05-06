package com.manage_blog.api.entity;

import com.manage_blog.api.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "blogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private BlogCategory category;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @Column(name = "slugs")
    private String slugs;

    @Column(name = "title")
    private String title;

    @Column(name = "tumbnail", length = 500, nullable = true)
    private String tumbnail;

    @Column(name = "article", length = 1000)
    private String article;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    @Column(name= "created_at")
    private String createdAt;

    @Column(name= "updated_at")
    private String updatedAt;

    @Column(name= "deleted_at")
    private String deletedAt;
}