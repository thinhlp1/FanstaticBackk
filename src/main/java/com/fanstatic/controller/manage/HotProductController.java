package com.fanstatic.controller.manage;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.flavor.FlavorRequestDTO;
import com.fanstatic.dto.model.hotproduct.HotProductRequestDTO;
import com.fanstatic.service.model.HotProductSerivce;
import com.fanstatic.util.ResponseUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/manage/hotproduct")
@AllArgsConstructor
public class HotProductController {
    private final HotProductSerivce hotProductSerivce;

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show() {
        ResponseDTO responseDTO = hotProductSerivce.show();
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid List<HotProductRequestDTO> hotProductRequestDTOs) {
        ResponseDTO responseDTO = hotProductSerivce.addHotProduct(hotProductRequestDTOs);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@RequestBody @Valid List<Integer> hotProductRemoveIds) {
        ResponseDTO responseDTO = hotProductSerivce.removeHotProduct(hotProductRemoveIds);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }
}
