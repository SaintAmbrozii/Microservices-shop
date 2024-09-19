package com.example.userservice.security;



import com.example.userservice.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



import java.util.Collection;
import java.util.Map;

public class UserPrincipal implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private String address;
    private String name;
    private Integer enabled;
    private Collection<? extends GrantedAuthority> authorities;


    public UserPrincipal(Long id, String email, String password, String name, String address,
                         Integer enabled, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        Collection<GrantedAuthority> authorities = user.getAuthority();

        return new UserPrincipal(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getLastname(),
                user.getAddress(),
                user.getEnabled(),
                authorities
        );
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return enabled == 1;
    }



    public Long getId() {
        return id;
    }



    public String getFullAddress() {
        return address;
    }

    public String getPersonFullName() {
        return name;
    }


}
