package com.fanstatic.config.exception;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.error.ErrorValidateDTO;
import com.fanstatic.util.ResponseUtils;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ResponseDTO> handleSQLException(SQLException e) {
        e.printStackTrace();
        return ResponseUtils.returnReponsetoClient(ResponseUtils.fail(500, "Có lỗi SQl xảy ra", null));
    }

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<ResponseDTO> handleException(Exception e) {
    //     e.printStackTrace();
    //     return ResponseUtils.returnReponsetoClient(ResponseUtils.fail(500, "Có lỗi hệ thống xảy ra", null));
    // }

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

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDTO> handleException(HttpRequestMethodNotSupportedException e) {
        e.printStackTrace();
        return ResponseUtils.returnReponsetoClient(ResponseUtils.fail(400, "Method không hợp lệ", null));
    }

}
