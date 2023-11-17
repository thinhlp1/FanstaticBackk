package com.fanstatic.controller.manage;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.saleEventProduct.SaleProductRequestDTO;
import com.fanstatic.service.model.SaleProductService;
import com.fanstatic.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/manage/saleproduct")
@AllArgsConstructor
public class SaleProductController {
    private final SaleProductService saleProductService;

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO reponseDTO = saleProductService.show(active);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }
    // @PostMapping("/create")
    // @ResponseBody
    // public ResponseEntity<ResponseDTO> create(@RequestBody @Valid SaleProductRequestDTO saleProductRequestDTO) {
    //     ResponseDTO reponseDTO = saleProductService.create(saleProductRequestDTO);
    //     return ResponseUtils.returnReponsetoClient(reponseDTO);
    // }

    // @PutMapping("/update/{id}")
    // @ResponseBody
    // public ResponseEntity<ResponseDTO> update(@RequestBody @Valid SaleProductRequestDTO saleProductRequestDTO, @PathVariable("id") int id) {
    //     ResponseDTO reponseDTO = saleProductService.update(saleProductRequestDTO);
    //     return ResponseUtils.returnReponsetoClient(reponseDTO);
    // }

    // @DeleteMapping("/delete/{id}")
    // @ResponseBody
    // public ResponseEntity<ResponseDTO> delete(@PathVariable("id") int id) {
    //     ResponseDTO reponseDTO = saleProductService.delete(id);
    //     return ResponseUtils.returnReponsetoClient(reponseDTO);
    // }

    // @PutMapping("/restore/{id}")
    // @ResponseBody
    // public ResponseEntity<ResponseDTO> restore(@PathVariable("id") int id) {
    //     ResponseDTO reponseDTO = saleProductService.restore(id);
    //     return ResponseUtils.returnReponsetoClient(reponseDTO);
    // }
  
  

    //  @GetMapping("/show/detail/{id}")
    // @ResponseBody
    // public ResponseEntity<ResponseDTO> detail(@PathVariable("id") int id) {
    //     ResponseDTO reponseDTO = saleProductService.detail(id);
    //     return ResponseUtils.returnReponsetoClient(reponseDTO);
    // }
}
