package com.fanstatic.controller.manage;

import com.fanstatic.dto.model.size.SizeRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.shift.ShiftRequestDTO;

import com.fanstatic.service.model.SizeService;
import com.fanstatic.util.ResponseUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/manage/size")
@AllArgsConstructor
public class SizeController {
    private final SizeService sizeService;

    // @GetMapping("/show/size-permission")
    // @ResponseBody
    // public ResponseEntity<ReponseDTO> getSizePermission() {
    // ReponseDTO reponseDTO = sizePermissionService.getSizePermisson();
    // return ReponseUtils.returnReponsetClient(reponseDTO);
    // }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid SizeRequestDTO sizeRequestDTO) {
        ResponseDTO reponseDTO = sizeService.create(sizeRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid SizeRequestDTO sizeRequestDTO, @PathVariable("id") int id) {
        ResponseDTO reponseDTO = sizeService.update(sizeRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = sizeService.delete(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/restore/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restore(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = sizeService.restore(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO reponseDTO = sizeService.show(active);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> detail(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = sizeService.detail(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }
}
