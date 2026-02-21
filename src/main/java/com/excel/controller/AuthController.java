package com.excel.controller;


import com.excel.dto.LoginRequest;
import com.excel.dto.LoginResponse;
import com.excel.dto.SignUpResponse;
import com.excel.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/sign")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody LoginRequest signUp){
        return ResponseEntity.ok(authService.signUp(signUp));
    }

}
