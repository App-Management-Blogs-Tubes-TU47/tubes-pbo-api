package com.manage_blog.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogRequesst {

    private String slugs;
    private String title;
    private String tumbnail;
    private String article;
    private String status; 

}
