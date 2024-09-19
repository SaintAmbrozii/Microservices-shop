package com.example.productservice.exceptionhandler;

import com.example.productservice.exception.ProductPurchaseRequestException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ProductPurchaseRequestException.class)
    public ResponseEntity<String> handler(ProductPurchaseRequestException exp) {
        return ResponseEntity.badRequest()
                .body(exp.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handler(EntityNotFoundException exp) {
        return ResponseEntity.badRequest()
                .body(exp.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handler(MethodArgumentNotValidException exp) {
        var errors = new HashMap<String, String>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var fileName = ((FieldError) error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.put(fileName, errorMessage);
                });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errors));
    }
}
