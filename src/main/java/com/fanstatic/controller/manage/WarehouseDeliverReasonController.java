package com.fanstatic.controller.manage;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.service.model.WarehouseDeliverReasonService;
import com.fanstatic.util.ResponseUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/manage/warehousedeliverreason")
@AllArgsConstructor
public class WarehouseDeliverReasonController {
    private final WarehouseDeliverReasonService warehouseDeliverReasonService;

//    @PostMapping("/create")
//    @ResponseBody
//    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid WarehouseDeliverRequestDTO data) {
//        ResponseDTO responseDTO = warehouseDeliverService.create(data);
//        return ResponseUtils.returnReponsetoClient(responseDTO);
//    }

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO responseDTO = warehouseDeliverReasonService.show(active);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }
}
