package com.excel.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorBody> handleUsernameNotFoundException(UsernameNotFoundException ex){
        ErrorBody errorBody = new ErrorBody("Username not found: "+ex.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorBody, errorBody.getHttpStatusCode());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorBody> handleAuthenticationException(AuthenticationException ex){
        ErrorBody errorBody = new ErrorBody("Authentication failed: "+ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(errorBody, errorBody.getHttpStatusCode());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorBody> handleJwtException(JwtException ex){
        ErrorBody errorBody = new ErrorBody("Invalid jwt token: "+ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(errorBody, errorBody.getHttpStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorBody> handleException(Exception ex){
        ErrorBody errorBody = new ErrorBody("An unexpected error occurred: "+ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorBody, errorBody.getHttpStatusCode());
    }

}
