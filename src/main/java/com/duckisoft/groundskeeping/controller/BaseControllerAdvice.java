package com.duckisoft.groundskeeping.controller;

import com.duckisoft.groundskeeping.dto.error.ErrorResponse;
import com.duckisoft.groundskeeping.exception.EmailExistsException;
import com.duckisoft.groundskeeping.exception.InvalidCredentialsException;
import com.duckisoft.groundskeeping.exception.JwtException;
import com.duckisoft.groundskeeping.exception.UserDisabledException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class BaseControllerAdvice {
    @ExceptionHandler({ Exception.class })
    public final ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
        if (ex instanceof EmailExistsException) {
            return returnExceptionResponse(HttpStatus.CONFLICT, ex.getMessage());
        }
        if (ex instanceof JwtException) {
            return returnExceptionResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
        if (ex instanceof UserDisabledException || ex instanceof InvalidCredentialsException) {
            return returnExceptionResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
        return returnExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> returnExceptionResponse(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus.value()).body(new ErrorResponse(message));
    }
}
