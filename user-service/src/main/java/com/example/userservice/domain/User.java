package com.example.userservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "users",schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email",unique = true)
    private String email;

    @Column(name = "phone",unique = true)
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "gender")
    private String gender;

    @Column(name = "locale")
    private String locale;

    @Column(name = "address")
    private String address;

    @Column(name = "auth_provider")
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "picture")
    private String picture;

    @Column(name = "enabled")
    private Integer enabled;


    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<GrantedAuthority> authority = new HashSet<>();


    @OneToMany(mappedBy = "user")
    private List<Token> tokenList;












}
