package com.fanstatic.controller.manage;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.saleevent.SaleEventRequestDTO;
import com.fanstatic.service.model.SaleEventService;
import com.fanstatic.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/manage/saleevent")
@AllArgsConstructor
public class SaleEventController {
    private final SaleEventService saleEventService;

    // @GetMapping("/show/size-permission")
    // @ResponseBody
    // public ResponseEntity<ReponseDTO> getSizePermission() {
    // ReponseDTO reponseDTO = sizePermissionService.getSizePermisson();
    // return ReponseUtils.returnReponsetClient(reponseDTO);
    // }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid SaleEventRequestDTO saleEventRequestDTO) {
        ResponseDTO reponseDTO = saleEventService.create(saleEventRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid SaleEventRequestDTO saleEventRequestDTO, @PathVariable("id") int id) {
        ResponseDTO reponseDTO = saleEventService.update(saleEventRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = saleEventService.delete(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/restore/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restore(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = saleEventService.restore(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO reponseDTO = saleEventService.show(active);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

     @GetMapping("/show/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> detail(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = saleEventService.detail(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }
}
