package com.fanstatic.controller.publics;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.service.model.UserVoucherService;
import com.fanstatic.service.model.VoucherService;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/u")
public class HomeController {
    private final VoucherService voucherService;

    @GetMapping("/vouchers")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showVouchers() {
        ResponseDTO reponseDTO = voucherService.getListVoucher();
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }
}
