package com.manage_blog.api.service;

import org.springframework.stereotype.Service;

@Service
public class CreateSlugService {
    public String createSlug(String title) {
        String slug = title.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "-").toLowerCase();
        return slug;
    }

    public String createSlug(String title, String separator) {
        String slug = title.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", separator).toLowerCase();
        return slug;
    }
}
