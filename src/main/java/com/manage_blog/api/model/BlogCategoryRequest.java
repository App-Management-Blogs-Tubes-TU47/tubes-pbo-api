package com.manage_blog.api.model;

import com.manage_blog.api.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogCategoryRequest {

    private String name;


}
