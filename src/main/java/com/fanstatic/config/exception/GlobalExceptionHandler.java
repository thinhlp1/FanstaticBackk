package com.fanstatic.config.exception;

import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.util.ResponseUtils;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ResponseDTO> handleSQLException(SQLException e) {
        e.printStackTrace();
        return ResponseUtils.returnReponsetoClient(ResponseUtils.fail(500, "Có lỗi SQl xảy ra", null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleException(Exception e) {
        e.printStackTrace();
        return ResponseUtils.returnReponsetoClient(ResponseUtils.fail(500, "Có lỗi hệ thống xảy ra", null));
    }

     @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDTO> handleException(HttpRequestMethodNotSupportedException e) {
        e.printStackTrace();
        return ResponseUtils.returnReponsetoClient(ResponseUtils.fail(400, "Method không hợp lệ", null));
    }


}
