package com.example.userservice.security;


import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
public class SecurityCheck {

    public static boolean isTheUserNotLoggedIn(String username){
        return   !SecurityContextHolder.getContext().getAuthentication().getName()
                .equals(username);
    }
    public static Long getUserIdFromSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) context.getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        return principal.getId();
    }

    public static UserPrincipal getJwtUserFromSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) context.getAuthentication();
        return (UserPrincipal) auth.getPrincipal();
    }


}
