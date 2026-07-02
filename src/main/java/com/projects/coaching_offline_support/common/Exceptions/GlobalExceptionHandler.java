package com.projects.coaching_offline_support.common.Exceptions;

import com.projects.coaching_offline_support.common.dtos.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistsException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT)
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<ErrorResponse> handleMethodArugmentNotValidException(MethodArgumentNotValidException ex){
       List<String> errors = ex.getBindingResult()
               .getAllErrors()
               .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
               .collect(Collectors.toList());

       ErrorResponse errorResponse = ErrorResponse.builder()
               .status(HttpStatus.BAD_REQUEST)
               .message("Invalid validation failed")
               .subErrors(errors)
               .timeStamp(LocalDateTime.now())
               .build();
       return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
}
