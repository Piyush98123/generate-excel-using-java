package com.excel.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;

@Data
public class ErrorBody {

    private LocalDateTime timestamp;
    private String error;
    private HttpStatus httpStatusCode;

    public ErrorBody() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorBody(String error, HttpStatus httpStatusCode) {
        this();
        this.error = error;
        this.httpStatusCode = httpStatusCode;
    }
}
