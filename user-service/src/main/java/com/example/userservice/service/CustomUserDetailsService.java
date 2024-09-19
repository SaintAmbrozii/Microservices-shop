package com.example.userservice.service;



import com.example.userservice.domain.User;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.repository.UserRepo;
import com.example.userservice.security.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private  UserRepo userRepo;



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return UserPrincipal.create(user);
    }
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new NotFoundException("Can't find user by ID"));

        return UserPrincipal.create(user);
    }
}
