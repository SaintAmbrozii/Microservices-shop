package com.example.basketservice.security;



import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityCheck {

    public static boolean isTheUserNotLoggedIn(String username){
        return   !SecurityContextHolder.getContext().getAuthentication().getName()
                .equals(username);
    }
    public static Long getUserIdFromSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) context.getAuthentication();
        return Long.parseLong(auth.getName());
    }




}
