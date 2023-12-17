package com.fanstatic.dto.firebase;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadInfoDTO extends ResponseDataDTO{
    private String imageUrl;
    private String imageName;
}
