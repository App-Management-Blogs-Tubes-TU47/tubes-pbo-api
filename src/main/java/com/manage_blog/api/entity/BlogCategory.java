package com.manage_blog.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Table(name = "blog_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at IS NULL")
public class BlogCategory extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "slugs")
    private String slugs;
}
