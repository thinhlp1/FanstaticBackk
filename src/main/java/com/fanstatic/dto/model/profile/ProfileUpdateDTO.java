package com.fanstatic.dto.model.profile;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileUpdateDTO {
      private int id;

    private String name;

    private String email;

    private Date dateOfBirth;

    private String placeOrigin;

     private String numberPhone;
     
}
