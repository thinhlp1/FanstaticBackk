package com.fanstatic.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO implements ResponseDTOIterface {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    protected int statusCode;

    protected String typeReponse;

    protected Object message;

    protected ResponseDataDTO data;

    public ResponseDTO (int statusCode, Object message){
        this.statusCode = statusCode;
        this.message = message;
    }

    @JsonIgnore
    public boolean isSuccess(){
        return typeReponse.equals(SUCCESS);
    }
}
