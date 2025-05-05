package com.manage_blog.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "blog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @category_id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID category_id;

    @user_id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID user_id;

    @Column(name = "slugs")
    private String slugs;

    @Column(name = "title")
    private String title;


    @Column(name = "tumbnail")
    private String tumbnail;

    @Column(name = "article")
    private String article;

    @Column(name = "status")
    private String status;

    @Column(name= "created_at")
    private String createdAt;

    @Column(name= "updated_at")
    private String updatedAt;

    @Column(name= "deleted_at")
    private String deletedAt;
}
v