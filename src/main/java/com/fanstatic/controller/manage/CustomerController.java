package com.fanstatic.controller.manage;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.customer.CustomerRequestDTO;
import com.fanstatic.service.model.CustomerService;
import com.fanstatic.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/manage/customer")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;


    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestPart @Valid CustomerRequestDTO data,
                                              @RequestPart MultipartFile image) {
        data.setImage(image);
        ResponseDTO reponseDTO = customerService.create(data);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid CustomerRequestDTO data,
                                              @PathVariable("id") Integer id) {
        ResponseDTO reponseDTO = customerService.update(data);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/image/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateImage(@RequestPart MultipartFile image,
                                                   @PathVariable("id") Integer id) {
        ResponseDTO reponseDTO = customerService.updateImage(id, image);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") Integer id) {
        ResponseDTO reponseDTO = customerService.delete(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }


    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO reponseDTO = customerService.show(active);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> details(@PathVariable("id") Integer id) {
        ResponseDTO reponseDTO = customerService.detail(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }
}
