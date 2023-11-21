package com.fanstatic.controller.manage;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.WarehouseDeliverItem.WarehouseDeliverItemRequestDTO;
import com.fanstatic.service.model.WarehouseDeliverItemService;
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
@RequestMapping("/api/manage/warehousedeliveritem")
@AllArgsConstructor
public class WarehouseDeliverItemController {
    private final WarehouseDeliverItemService warehouseDeliverItemService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid WarehouseDeliverItemRequestDTO data) {
        ResponseDTO responseDTO = warehouseDeliverItemService.create(data);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }
}
