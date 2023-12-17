package com.fanstatic.controller.manage;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.flavorcategory.FlavorCategoryRequestDTO;
import com.fanstatic.service.model.FlavorCategoryService;
import com.fanstatic.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/manage/flavorcategory")
@AllArgsConstructor
public class FlavorCategoryController {
    private final FlavorCategoryService flavorCategoryService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid FlavorCategoryRequestDTO data) {
        ResponseDTO responseDTO = flavorCategoryService.create(data);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid FlavorCategoryRequestDTO data, @PathVariable("id") int id) {
        ResponseDTO responseDTO = flavorCategoryService.update(data);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") int id) {
        ResponseDTO responseDTO = flavorCategoryService.delete(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/restore/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restore(@PathVariable("id") int id) {
        ResponseDTO responseDTO = flavorCategoryService.restore(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> detail(@PathVariable("id") int id) {
        ResponseDTO responseDTO = flavorCategoryService.detail(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO responseDTO = flavorCategoryService.show(active);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

}
