package com.manage_blog.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "blog_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "slugs")
    private String slugs;

    @Column(name= "created_at")
    private String createdAt;

    @Column(name= "updated_at")
    private String updatedAt;

    @Column(name= "deleted_at")
    private String deletedAt;
}
