package com.projects.coaching_offline_support.common.Exceptions;

import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.common.dtos.ErrorResponse;
import org.apache.coyote.BadRequestException;
import org.hibernate.grammars.hql.HqlParser;
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
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExistException(UserAlreadyExistsException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT)
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(ApiResponse.error(errorResponse),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(ApiResponse.error(errorResponse),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<ApiResponse<Void>> handleMethodArugmentNotValidException(MethodArgumentNotValidException ex){
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

       return new ResponseEntity<>(ApiResponse.error(errorResponse),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BatchTimingConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleBatchException(BatchTimingConflictException exception){

        List<String> errors = exception.getCoflicts().stream()
                .map( ex -> {
                    return ex.batchName() + " already exists for day " + ex.day() + " between " + ex.timing();
                }).toList();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT)
                .message(exception.getMessage())
                .subErrors(errors)
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(ApiResponse.error(errorResponse),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequestException(BadRequestException ex){

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(ApiResponse.error(errorResponse),HttpStatus.BAD_REQUEST);
    }
}
