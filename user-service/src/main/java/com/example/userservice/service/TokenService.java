package com.example.userservice.service;


import com.example.userservice.domain.Token;
import com.example.userservice.domain.User;
import com.example.userservice.repository.TokenRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepo tokenRepo;

    public TokenService(TokenRepo tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    public Optional<Token> findByToken(String token) {
        return tokenRepo.findByToken(token);
    }

    public void saveToken(User user, String jwt){
        Token token = Token.builder()
                .token(jwt)
                .user(user)
                .build();

        tokenRepo.save(token);
    }

    public void revokeAllUserToken(User user){
        List<Token> allValidTokenByUser
                = tokenRepo.findAllValidTokenByUser(user.getId());

        if(allValidTokenByUser.isEmpty()) return;

        allValidTokenByUser.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepo.saveAll(allValidTokenByUser);
    }

    public void deleteToken(Long id) {
        tokenRepo.deleteById(id);
    }
}
