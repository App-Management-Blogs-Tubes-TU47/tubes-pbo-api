package com.manage_blog.api.entity;

import com.manage_blog.api.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Table(name = "blogs")
@Data()
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at IS NULL")
public class Blog extends BaseEntity {
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

    @Column(name = "article", length = 50000)
    private String article;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;
}