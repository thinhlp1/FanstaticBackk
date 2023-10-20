package com.fanstatic.controller.firebase;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.error.ErrorValidateDTO;
import com.fanstatic.dto.firebase.FirebaseResponse;
import com.fanstatic.service.firebase.FirebaseStorageService;
import com.fanstatic.util.ResponseUtils;

@RestController
public class FirebaseConfig {
    @Autowired
    FirebaseStorageService firebaseStorageService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseDTO> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = firebaseStorageService.uploadImage(file);
            FirebaseResponse firebaseResponse = new FirebaseResponse();
            firebaseResponse.setImageUrl(imageUrl);
            ResponseDTO responseDTO = ResponseUtils.success(200, "Upload successful", firebaseResponse);            
            return ResponseUtils.returnReponsetoClient(responseDTO);
        } catch (Exception e) {
            ErrorValidateDTO errorValidateDTO = new ErrorValidateDTO();
            Map<String, String> errors = new HashMap<>();
            errors.put("file", e.getMessage());
            errorValidateDTO.setErrors(errors);
            ResponseDTO responseDTO = ResponseUtils.fail(401, "Upload fail", errorValidateDTO);
            return ResponseUtils.returnReponsetoClient(responseDTO);
        }
    }

}
