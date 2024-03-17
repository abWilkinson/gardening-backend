package com.duckisoft.gardening.controller;

import com.duckisoft.gardening.dto.error.ErrorResponse;
import com.duckisoft.gardening.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class BaseControllerAdvice {
    @ExceptionHandler({ Exception.class })
    public final ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
        log.error("Controller Advice error", ex);
        if (ex instanceof EmailExistsException) {
            return returnExceptionResponse(HttpStatus.CONFLICT, ex.getMessage());
        }
        if (ex instanceof JwtException) {
            return returnExceptionResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
        if (ex instanceof UserDisabledException || ex instanceof InvalidCredentialsException) {
            return returnExceptionResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
        if (ex instanceof DeviceException) {
            return returnExceptionResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        if (ex instanceof GenericExcepion) {
            return returnExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return returnExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, "There was a problem with the request. Please try again later.");
    }

    private ResponseEntity<ErrorResponse> returnExceptionResponse(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus.value()).body(new ErrorResponse(message));
    }
}
