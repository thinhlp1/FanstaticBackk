package com.fanstatic.controller.manage;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.extraportion.ExtraPortionRequestDTO;
import com.fanstatic.service.model.ExtraPortionService;
import com.fanstatic.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/manage/extraportion")
@AllArgsConstructor
public class ExtraPortionController {
    private final ExtraPortionService extraPortionService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestPart @Valid ExtraPortionRequestDTO data, @RequestPart MultipartFile imageFile) {
        data.setImageFile(imageFile);
        ResponseDTO responseDTO = extraPortionService.create(data);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid ExtraPortionRequestDTO extraPortionRequestDTO, @PathVariable("id") int id) {
        ResponseDTO responseDTO = extraPortionService.update(extraPortionRequestDTO);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/update/image/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateImage(@RequestPart MultipartFile imageFile,
                                                   @PathVariable("id") Integer id) {
        ResponseDTO responseDTO = extraPortionService.updateImage(id, imageFile);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") int id) {
        ResponseDTO responseDTO = extraPortionService.delete(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/restore/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restore(@PathVariable("id") int id) {
        ResponseDTO responseDTO = extraPortionService.restore(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO responseDTO = extraPortionService.show(active);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> detail(@PathVariable("id") int id) {
        ResponseDTO responseDTO = extraPortionService.detail(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }
}
