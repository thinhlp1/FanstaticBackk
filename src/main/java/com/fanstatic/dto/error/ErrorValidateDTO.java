package com.fanstatic.dto.error;

import java.util.Map;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorValidateDTO extends ResponseDataDTO{
    Map<String, String> errors;
}
