package com.example.userservice.controller;



import com.example.userservice.domain.AuthProvider;
import com.example.userservice.domain.Role;
import com.example.userservice.domain.Token;
import com.example.userservice.domain.User;
import com.example.userservice.payload.ApiResponse;
import com.example.userservice.payload.LoginRequest;
import com.example.userservice.payload.SignUpRequest;
import com.example.userservice.payload.TokenResponse;
import com.example.userservice.security.logout.LogoutService;
import com.example.userservice.security.jwt.TokenProvider;
import com.example.userservice.service.TokenService;
import com.example.userservice.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("api/users/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;
    private final LogoutService logoutService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService,
                          TokenProvider tokenProvider, PasswordEncoder encoder,
                          TokenService tokenService, LogoutService logoutService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.encoder = encoder;
        this.tokenService = tokenService;
        this.logoutService = logoutService;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        final User user = userService.findByEmail(loginRequest.getEmail()).orElseThrow();
        tokenService.revokeAllUserToken(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = tokenProvider.createToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);
        TokenResponse tokenResponse = TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
        tokenService.saveToken(user, refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody SignUpRequest signUpRequest) {
        Optional<User> userFromDB = userService.findByUsername(signUpRequest.getEmail());

        if (userFromDB.isPresent()) {
            throw new BadCredentialsException("Username is already exists");
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setName(signUpRequest.getName());
        user.setLastname(signUpRequest.getLastname());
        user.setPhone(signUpRequest.getPhone());
        user.setProvider(AuthProvider.LOCAL);
        user.setEnabled(1);
        user.setAuthority(Collections.singleton(Role.ROLE_USER));

        User userAfterSaving = userService.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(userAfterSaving.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully!"));
    }



    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> getRefreshToken(@RequestBody String token) {
        Optional<Token> inDb = tokenService.findByToken(token);
        Token currentToken = inDb.get();
        if (Date.from(Instant.now()).after(currentToken.getDuration())) {
            tokenService.deleteToken(currentToken.getId());
        }
        String refreshToken = currentToken.getToken();
        TokenResponse response = TokenResponse.builder().refreshToken(refreshToken).accessToken(null).build();
        return ResponseEntity.ok(response);
    }



}
