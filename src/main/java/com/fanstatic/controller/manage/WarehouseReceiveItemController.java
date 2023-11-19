package com.fanstatic.controller.manage;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.warehouseReceiveItem.WarehouseReceiveItemRequestDTO;
import com.fanstatic.service.model.WarehouseReceiveItemService;
import com.fanstatic.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/manage/warehousereceiveitem")
@AllArgsConstructor
public class WarehouseReceiveItemController {
    private final WarehouseReceiveItemService warehouseReceiveItemService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid WarehouseReceiveItemRequestDTO data) {
        ResponseDTO responseDTO = warehouseReceiveItemService.create(data);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }
}
