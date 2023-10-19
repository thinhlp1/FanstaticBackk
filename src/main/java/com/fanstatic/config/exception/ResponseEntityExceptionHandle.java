package com.fanstatic.config.exception;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.error.ErrorValidateDTO;
import org.springframework.validation.FieldError;

@ControllerAdvice
public class ResponseEntityExceptionHandle extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(ValidationException ex) {

        ErrorValidateDTO errorValidateDTO = new ErrorValidateDTO();
        ResponseDTO reponseDTO = new ResponseDTO();

        Map<String, String> errors = ex.getErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        errorValidateDTO.setErrors(errors);
        reponseDTO.setData(errorValidateDTO);
        reponseDTO.setMessage("Thông tin không hợp lệ");
        reponseDTO.setTypeReponse("BAD_REQUEST");
        reponseDTO.setStatusCode(400);

        return ResponseEntity.status(400).body(reponseDTO);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorValidateDTO errorValidateDTO = new ErrorValidateDTO();
        ResponseDTO reponseDTO = new ResponseDTO();

        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        errorValidateDTO.setErrors(errors);
        reponseDTO.setData(errorValidateDTO);
        reponseDTO.setMessage("Thông tin không hợp lệ");
        reponseDTO.setTypeReponse("BAD_REQUEST");
        reponseDTO.setStatusCode(500);

        return ResponseEntity.status(500).body(reponseDTO);
    }
}