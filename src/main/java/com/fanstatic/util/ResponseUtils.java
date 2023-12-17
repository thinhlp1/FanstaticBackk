package com.fanstatic.util;

import org.springframework.http.ResponseEntity;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

public class ResponseUtils {

    public static ResponseDTO success(int status, Object message, ResponseDataDTO data) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatusCode(status);
        responseDTO.setMessage(message);
        responseDTO.setTypeReponse(ResponseDTO.SUCCESS);
        responseDTO.setData(data);
        return responseDTO;
    }

    public static ResponseDTO success(ResponseDTO responseDTO, ResponseDataDTO data) {
        responseDTO.setData(data);
        return responseDTO;
    }

    public static ResponseDTO fail(int status, Object message, ResponseDataDTO data) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatusCode(status);
        responseDTO.setMessage(message);
        responseDTO.setTypeReponse(ResponseDTO.FAIL);

        responseDTO.setData(data);
        return responseDTO;
    }

    public static ResponseEntity<ResponseDTO> returnReponsetoClient(ResponseDTO responseDTO) {
        if (responseDTO.isSuccess()) {
            return ResponseEntity.ok().body((responseDTO));
        } else {
            return ResponseEntity.status(responseDTO.getStatusCode()).body(responseDTO);

        }
    }

    public static void setResponseDTOToHttpResponse(HttpServletResponse response, ResponseDTO responseDTO) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(responseDTO);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
