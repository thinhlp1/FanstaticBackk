package com.fanstatic.controller.manage;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.shift.ShiftRequestDTO;
import com.fanstatic.service.model.ShiftService;
import com.fanstatic.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/manage/shift")
@AllArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;

    // @GetMapping("/show/size-permission")
    // @ResponseBody
    // public ResponseEntity<ReponseDTO> getSizePermission() {
    // ReponseDTO reponseDTO = sizePermissionService.getSizePermisson();
    // return ReponseUtils.returnReponsetClient(reponseDTO);
    // }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid ShiftRequestDTO shiftRequestDTO) {
        ResponseDTO reponseDTO = shiftService.create(shiftRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid ShiftRequestDTO shiftRequestDTO, @PathVariable("id") String id) {
        ResponseDTO reponseDTO = shiftService.update(shiftRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") String id) {
        ResponseDTO reponseDTO = shiftService.delete(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/restore/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restore(@PathVariable("id") String id) {
        ResponseDTO reponseDTO = shiftService.restore(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO reponseDTO = shiftService.show(active);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

     @GetMapping("/show/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> detail(@PathVariable("id") String id) {
        ResponseDTO reponseDTO = shiftService.detail(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }
}
