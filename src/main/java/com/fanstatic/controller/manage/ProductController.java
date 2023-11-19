package com.fanstatic.controller.manage;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.product.ProductRequestDTO;
import com.fanstatic.dto.model.product.ProductVarientRequestDTO;
import com.fanstatic.service.model.ProductService;
import com.fanstatic.service.model.ProductVarientService;
import com.fanstatic.util.ResponseUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/manage/product")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductVarientService productVarientService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestPart @Valid ProductRequestDTO data,
            @RequestPart List<MultipartFile> images, @RequestPart MultipartFile description) {
        data.setImageFiles(images);
        data.setDescriptionFile(description);
        ResponseDTO reponseDTO = productService.create(data);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@RequestPart @Valid ProductRequestDTO data,
            @PathVariable("id") Integer id) {
        ResponseDTO reponseDTO = productService.update(data);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/varient/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateVarient(@RequestPart @Valid ProductVarientRequestDTO data,
            @PathVariable("id") Integer id) {
        ResponseDTO reponseDTO = productVarientService.updateProductVarient(data);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/image/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateImage(@RequestPart List<Integer> removeImages,
            @RequestPart List<MultipartFile> newImages,

            @PathVariable("id") Integer id) {
        ResponseDTO reponseDTO = productService.updateImage(id, newImages, removeImages);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/description/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateDescription(@RequestPart MultipartFile description,
            @PathVariable("id") Integer id) {
        ResponseDTO reponseDTO = productService.updateDescriptionProduct(id, description);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = productService.delete(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @DeleteMapping("/delete/varient/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> deleteVarient(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = productVarientService.deleteProductVarient(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/restore/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restore(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = productService.restore(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/restore/varient/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restoreVarient(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = productVarientService.restoreProductVarient(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO reponseDTO = productService.show(active);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> details(@PathVariable("id") Integer id) {
        ResponseDTO reponseDTO = productService.detail(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

}
