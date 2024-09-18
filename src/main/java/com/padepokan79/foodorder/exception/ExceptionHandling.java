package com.padepokan79.foodorder.exception;

import javax.naming.AuthenticationException;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.padepokan79.foodorder.dto.error.ErrorDto;
import com.padepokan79.foodorder.dto.error.ErrorsDto;
import com.padepokan79.foodorder.exception.classes.CartException;
import com.padepokan79.foodorder.exception.classes.DataNotFoundException;
import com.padepokan79.foodorder.exception.classes.ValidationException;
import com.padepokan79.foodorder.utils.MessageUtils;
import com.padepokan79.foodorder.utils.ViolationsMapper;

@RestControllerAdvice
public class ExceptionHandling {
    
    private final MessageUtils messageUtils;

    public ExceptionHandling(final MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleAllException(Exception ex) {
        return new ErrorDto(messageUtils.getMessage("application.error.internal"), 
            HttpStatus.INTERNAL_SERVER_ERROR.toString(), 
            HttpStatus.INTERNAL_SERVER_ERROR.value()); 
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.badRequest()
            .body(
                new ErrorDto(
                    ex.getMessage(), 
                    HttpStatus.BAD_REQUEST.toString(), 
                    HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorsDto> handleEmptyFields(ValidationException ex) {
        System.out.println("empty fields  123123");
        return ResponseEntity.badRequest()
            .body(new ErrorsDto(ex.getFieldErrors()));
    }

    @ExceptionHandler(AuthenticationException.class) 
    public ResponseEntity<ErrorsDto> handleEmptyFields(AuthenticationException ex) {
        System.out.println("empty fields  123123");
        return ResponseEntity.badRequest()
            .body(new ErrorsDto(ViolationsMapper.map("username", ex.getMessage())));
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleDataNotFoundException(DataNotFoundException ex) {
        return new ErrorDto(
            ex.getMessage(), 
            HttpStatus.NOT_FOUND.toString(), 
            HttpStatus.NOT_FOUND.value());
  
    }

    @ExceptionHandler(CartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleCartException(CartException ex) {
        return new ErrorDto(ex.getMessage(), HttpStatus.BAD_REQUEST.toString(), HttpStatus.BAD_REQUEST.value());
    } 
}
