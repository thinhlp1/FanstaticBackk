package com.fanstatic.controller.manage;

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

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.category.CategoryRequestDTO;
import com.fanstatic.service.model.CategoryService;
import com.fanstatic.util.ResponseUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/manage/category")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestPart @Valid CategoryRequestDTO data,
            @RequestPart MultipartFile image) {
        data.setImageFile(image);
        ResponseDTO reponseDTO = categoryService.create(data);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid CategoryRequestDTO data) {
        ResponseDTO reponseDTO = categoryService.update(data);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/image/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateImage(@RequestPart MultipartFile image,
            @PathVariable("id") Integer id) {
        ResponseDTO reponseDTO = categoryService.updateImage(id, image);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = categoryService.delete(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/restore/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restore(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = categoryService.restore(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO reponseDTO = categoryService.show(active);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> details(@PathVariable("id") Integer id) {
        ResponseDTO reponseDTO = categoryService.detail(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

}
