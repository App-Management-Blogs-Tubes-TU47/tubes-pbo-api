package com.manage_blog.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf-> csrf.disable()) // disable CSRF for APIs (enable for web apps)
                .authorizeHttpRequests(auth -> auth
                                .anyRequest()
//                        .requestMatchers(
//                                "/api/public/**",
//                                "/api/v1/auth/**",
//                                "/v3/api-docs/**",
//                                "/swagger-ui/**",
//                                "/swagger-ui.html",
//                                "/swagger-resources/**"
//                        )
                        .permitAll() // allow public endpoints
//                        .anyRequest().authenticated() // secure all other endpoints
                )
                .httpBasic(
                        httpBasic -> httpBasic
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.sendError(401, "Unauthorized");
                                })
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
