package com.fanstatic.controller.manage;


import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.voucher.VoucherRequestDTO;
import com.fanstatic.service.model.VoucherService;
import com.fanstatic.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/manage/voucher")
@AllArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;


    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO responseDTO = voucherService.show(active);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid VoucherRequestDTO voucherRequestDTO) {
        ResponseDTO responseDTO = voucherService.create(voucherRequestDTO);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid VoucherRequestDTO voucherRequestDTO, @PathVariable("id") int id) {
        ResponseDTO responseDTO = voucherService.update(voucherRequestDTO);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") int id) {
        ResponseDTO responseDTO = voucherService.delete(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/restore/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restore(@PathVariable("id") int id) {
        ResponseDTO responseDTO = voucherService.restore(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> detail(@PathVariable("id") int id) {
        ResponseDTO responseDTO = voucherService.detail(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }
}
