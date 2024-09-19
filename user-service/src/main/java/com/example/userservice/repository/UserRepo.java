package com.example.userservice.repository;


import com.example.userservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.Optional;


public interface UserRepo extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {


    Optional<User> findUserByEmail(String email);


    Optional<User> findByName(String name);
    Optional<User> findByPhone(String number);





}
