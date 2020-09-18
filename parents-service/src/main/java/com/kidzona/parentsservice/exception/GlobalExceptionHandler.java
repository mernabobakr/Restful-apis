package com.kidzona.parentsservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> argumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        ArrayList<ErrorMessage> listOfMessages = new ArrayList<>();
        for (ObjectError errorMessage : ex.getBindingResult().getAllErrors()) {
            listOfMessages.add(new ErrorMessage(errorMessage.getDefaultMessage()));
        }
        return new ResponseEntity<>(listOfMessages.get(0), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<?> userAlreadyRegistered(UserAlreadyRegisteredException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenAuthenticationException.class)
    public ResponseEntity<?> handleTokenAuthenticationException(TokenAuthenticationException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ParentNotFoundException.class)
    public ResponseEntity<?> parentNotFound(ParentNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(KidNotFoundException.class)
    public ResponseEntity<?> kidNotFound(KidNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> genericException(Exception ex, WebRequest req) {
        return new ResponseEntity<>(new ErrorMessage("Unknown Error Happened"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
