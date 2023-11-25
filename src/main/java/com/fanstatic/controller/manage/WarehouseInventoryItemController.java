package com.fanstatic.controller.manage;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.warehouseInventoryItem.WarehouseInventoryItemRequestDTO;
import com.fanstatic.service.model.WarehouseInventoryItemService;
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
@RequestMapping("/api/manage/warehouseinventoryitem")
@AllArgsConstructor
public class WarehouseInventoryItemController {
    private final WarehouseInventoryItemService warehouseInventoryItemService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid WarehouseInventoryItemRequestDTO data) {
        ResponseDTO responseDTO = warehouseInventoryItemService.create(data);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

}
