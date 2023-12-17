package com.fanstatic.config.system;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactConfig extends ResponseDataDTO {
    private String numberPhone;
    private String facebook;
    private String zalo;
    private String email;

}