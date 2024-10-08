package com.example.userservice.service;


import com.example.userservice.domain.User;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.repository.UserRepo;
import com.example.userservice.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {


    @Autowired
    private UserRepo userRepo;


    public User save(User user) {
        return userRepo.save(user);
    }


    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public List<UserDTO> getAllUsers () {
        return userRepo.findAll().stream().map(this::doDto).collect(Collectors.toList());
    }


    public Optional<User> findByEmail (String email) {
        return userRepo.findUserByEmail(email);
    }


    public Optional<User> findByUsername(String name) {
        return userRepo.findByName(name);
    }

    public User update(Long id, UserPrincipal userPrincipal) {
        User user = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        if (Objects.equals(id, userPrincipal.getId())) {
            userRepo.save(user);
        }
       return user;
    }

    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }
    public boolean isPhoneNumberTaken(String phoneNumber){
        return userRepo.findByPhone(phoneNumber).isPresent();
    }

    public boolean doesUsernameExists(String username){
        return userRepo.findUserByEmail(username).isPresent();
    }



    public UserDTO doDto (User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        dto.setEnabled(user.getEnabled());
        dto.setPicture(user.getPicture());
        dto.setLocale(user.getLocale());
        dto.setAuthority(user.getAuthority());
        return dto;

    }
}
