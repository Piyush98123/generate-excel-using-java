package com.excel.security;

import com.excel.dto.LoginRequest;
import com.excel.dto.LoginResponse;
import com.excel.dto.SignUpResponse;
import com.excel.entity.User;
import com.excel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtils authUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       AuthUtils authUtils,
                       UserRepository userRepository, PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.authUtils = authUtils;
        this.userRepository = userRepository;
        this.passwordEncoder=encoder;
    }


    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        User user = (User) authentication.getPrincipal();
        String token = authUtils.getAccessToken(user);
        return new LoginResponse(token, user.getId());
    }

    public SignUpResponse signUp(LoginRequest signUp) {
        User user = userRepository.findByUserName(signUp.getUsername()).orElse(null);
        if(user != null) throw new IllegalArgumentException("User exist");
        user = userRepository.save(User.builder().userName(signUp.getUsername()).password(passwordEncoder.encode(signUp.getPassword())).build());
        return new SignUpResponse(user.getId(), user.getUsername());

    }
}
