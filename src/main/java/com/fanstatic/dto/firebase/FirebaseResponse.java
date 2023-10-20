package com.fanstatic.dto.firebase;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FirebaseResponse extends ResponseDataDTO{
    private String imageUrl;
}
