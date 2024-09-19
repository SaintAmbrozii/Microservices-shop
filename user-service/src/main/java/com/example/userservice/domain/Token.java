package com.example.userservice.domain;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String token;

    private boolean expired = false;

    private boolean revoked = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;




}
