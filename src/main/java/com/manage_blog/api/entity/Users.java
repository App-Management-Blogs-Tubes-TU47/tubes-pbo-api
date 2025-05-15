package com.manage_blog.api.entity;

import com.manage_blog.api.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at IS NULL")
public class Users extends BaseEntity implements UserDetails  {
    @Column(name= "name")
    private String name;

    @Column(name= "username", unique = true)
    private String username;

    @Column(name= "email", unique = true)
    private String email;

    @Column(name= "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name= "role")
    private RoleEnum role;

    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Blog> blogs;

    @Column(name = "profile", length = 500, nullable = true)
    private String profile;

    // implementasi UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
