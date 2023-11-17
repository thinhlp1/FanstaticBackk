package com.fanstatic.controller.manage;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.size.SizeRequestDTO;
import com.fanstatic.dto.model.supplier.SupplierRequestDTO;
import com.fanstatic.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import com.fanstatic.service.model.SupplierService;

@Controller
@RequestMapping("/api/manage/supplier")
@AllArgsConstructor
public class SupplierController {
      private final SupplierService supplierService;
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid SupplierRequestDTO supplierRequestDTO) {
        ResponseDTO reponseDTO = supplierService.create(supplierRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid SupplierRequestDTO supplierRequestDTO, @PathVariable("id") int id) {
        ResponseDTO reponseDTO = supplierService.update(supplierRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = supplierService.delete(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/restore/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restore(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = supplierService.restore(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }
      @GetMapping("/show")
      @ResponseBody
      public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
          ResponseDTO reponseDTO = supplierService.show(active);
          return ResponseUtils.returnReponsetoClient(reponseDTO);
      }
  
}
