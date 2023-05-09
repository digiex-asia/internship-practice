package com.demo.config.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.demo.common.enums.UserRole;
import com.demo.entities.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author DigiEx Group
 */
public class AuthUser implements UserDetails {

    @Getter
    private final String id;
    private final String username;
    private final String password;
    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String email;
    private final boolean enabled;
    @Getter
    private UserRole role;



    public AuthUser(User user) {
        this.id = user.getId();
        this.username = user.getEmail();
        this.password = "";
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.enabled = true;

    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
