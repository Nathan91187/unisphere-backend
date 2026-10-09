package com.example.unisphere.exception;

import com.example.unisphere.dto.error.ErrorResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ClubNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClubNotFoundException(ClubNotFoundException ex){
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        status.value(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex){
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        status.value(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventNotFoundException(EventNotFoundException ex){
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        status.value(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }
    @ExceptionHandler(InvalidEventDateRangeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEventDateRangeException(
            InvalidEventDateRangeException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        status.value(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }
    @ExceptionHandler(InvalidEventTimeRangeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEventTimeRangeException(
            InvalidEventTimeRangeException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        status.value(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }
    @ExceptionHandler(ClubMembershipAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleClubMembershipAlreadyExistsException(
            ClubMembershipAlreadyExistsException ex
    ){
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        status.value(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex
    ){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<FieldError> errors = ex.getBindingResult()
                .getFieldErrors();
        Map<String,String > messages = errors.stream().collect(
                Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage()
                )
        );
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        status.value(),
                        "Validation failed",
                        LocalDateTime.now(),
                        messages
                ));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestBody(
            HttpMessageNotReadableException ex
    ){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        status.value(),
                        "Invalid request body",
                        LocalDateTime.now()
                ));
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedRequestMethod(
            HttpRequestMethodNotSupportedException ex
    ){
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        status.value(),
                        "HTTP method not allowed",
                        LocalDateTime.now()
                ));
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // for a non-existing category(ClubCategory...) inside our request param, specifically for the filter functionality
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        status.value(),
                        "Invalid request parameter",
                        LocalDateTime.now()
                ));
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestParameter(
            MissingServletRequestParameterException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Missing required request parameter",
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedError(Exception ex){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("Unexpected error occurred",ex);
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        status.value(),
                        "Unexpected error occurred",
                        LocalDateTime.now()
                ));
    }

}
