package com.fanstatic.controller.manage;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.PutExchange;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.combo.ComboProductDetailRequestDTO;
import com.fanstatic.dto.model.combo.ComboProductRequestDTO;
import com.fanstatic.dto.model.product.ProductRequestDTO;
import com.fanstatic.service.model.ComboProductService;
import com.fanstatic.util.ResponseUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/manage/comboproduct")
@AllArgsConstructor
public class ComboProductController {
    private final ComboProductService comboProductService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestPart @Valid ComboProductRequestDTO data,
            @RequestPart MultipartFile images, @RequestPart MultipartFile description) {
        data.setImage(images);
        data.setDescription(description);
        ResponseDTO reponseDTO = comboProductService.create(data);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid ComboProductRequestDTO data) {
        ResponseDTO reponseDTO = comboProductService.update(data);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/image/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateImage(@PathVariable("id") Integer id, @RequestPart MultipartFile image) {
        ResponseDTO reponseDTO = comboProductService.updateImage(id, image);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/description/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateDescription(@PathVariable("id") Integer id,
            @RequestPart MultipartFile description) {
        ResponseDTO reponseDTO = comboProductService.updateDescription(id, description);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/update-combo-detail")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateDetail(@RequestBody @Valid ComboProductDetailRequestDTO comboProductRequestDTO) {
        ResponseDTO reponseDTO = comboProductService.updateComboProductDetails(comboProductRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/add-combo-detail")
    @ResponseBody
    public ResponseEntity<ResponseDTO> addDetail(@RequestBody @Valid ComboProductDetailRequestDTO comboProductRequestDTO) {
        ResponseDTO reponseDTO = comboProductService.addComboProductDetails(comboProductRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/remove-combo-detail")
    @ResponseBody
    public ResponseEntity<ResponseDTO> removeDetail(@RequestBody @Valid ComboProductDetailRequestDTO comboProductRequestDTO) {
        ResponseDTO reponseDTO = comboProductService.removeComboProductDetails(comboProductRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") Integer id) {
        ResponseDTO reponseDTO = comboProductService.delete(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/restore/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restore(@PathVariable("id") Integer id) {
        ResponseDTO reponseDTO = comboProductService.restore(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> detail(@PathVariable("id") Integer id) {
        ResponseDTO reponseDTO = comboProductService.detail(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO reponseDTO = comboProductService.show(active);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }
}
