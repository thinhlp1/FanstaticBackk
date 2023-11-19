package com.fanstatic.dto.system;

import java.math.BigInteger;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubscribteNotiRequestDTO {

    @NotBlank
    private String token;

    @NotBlank
    private String browser;
}
