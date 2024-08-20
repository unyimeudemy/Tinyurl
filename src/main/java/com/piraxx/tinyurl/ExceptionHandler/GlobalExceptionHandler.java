package com.piraxx.tinyurl.ExceptionHandler;

import com.piraxx.tinyurl.domain.responses.ErrorResponseDto;
import com.piraxx.tinyurl.exceptions.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponseDto> handleInternalServerException(InternalServerErrorException ex){
        return new ResponseEntity<>(ErrorResponseDto.builder()
                .status_code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
