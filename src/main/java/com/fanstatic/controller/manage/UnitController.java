package com.fanstatic.controller.manage;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.unit.UnitRequestDTO;
import com.fanstatic.service.model.UnitService;
import com.fanstatic.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/manage/unit")
@AllArgsConstructor
public class UnitController {
    private final UnitService unitService;

    // @GetMapping("/show/size-permission")
    // @ResponseBody
    // public ResponseEntity<ReponseDTO> getSizePermission() {
    // ReponseDTO reponseDTO = sizePermissionService.getSizePermisson();
    // return ReponseUtils.returnReponsetClient(reponseDTO);
    // }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid UnitRequestDTO unitRequestDTO) {
        ResponseDTO reponseDTO = unitService.create(unitRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid UnitRequestDTO unitRequestDTO, @PathVariable("id") String id) {
        ResponseDTO reponseDTO = unitService.update(unitRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") String id) {
        ResponseDTO reponseDTO = unitService.delete(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/restore/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restore(@PathVariable("id") String id) {
        ResponseDTO reponseDTO = unitService.restore(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO reponseDTO = unitService.show(active);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> detail(@PathVariable("id") String id) {
        ResponseDTO reponseDTO = unitService.detail(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }
}
