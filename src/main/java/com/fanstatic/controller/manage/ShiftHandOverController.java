package com.fanstatic.controller.manage;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.auth.ConfirmPasswordDTO;
import com.fanstatic.dto.model.shitfhandover.EndShiftRequestDTO;
import com.fanstatic.dto.model.shitfhandover.StartShiftRequestDTO;
import com.fanstatic.repository.ShiftHandoverRepository;
import com.fanstatic.service.model.ShiftHandoverService;
import com.fanstatic.util.ResponseUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/manage/shifthandover")
@AllArgsConstructor
public class ShiftHandOverController {
    private final ShiftHandoverService shiftHandoverService;

    @PostMapping("/create/start-shift")
    @ResponseBody
    public ResponseEntity<ResponseDTO> start(
            @RequestBody @Valid StartShiftRequestDTO startShiftRequestDTO) {
        ResponseDTO reponseDTO = shiftHandoverService.startShift(startShiftRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PostMapping("/create/end-shift")
    @ResponseBody
    public ResponseEntity<ResponseDTO> end(
            @RequestBody @Valid EndShiftRequestDTO endShiftRequestDTO) {
        ResponseDTO reponseDTO = shiftHandoverService.endShift(endShiftRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }
}
