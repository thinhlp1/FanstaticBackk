package com.fanstatic.controller.manage;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.warehouseReceiveItem.WarehouseReceiveItemRequestDTO;
import com.fanstatic.service.model.WarehouseReceiveItemService;
import com.fanstatic.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show() {
        ResponseDTO responseDTO = warehouseReceiveItemService.show();
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show/byflavorid")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showByFlavor(@RequestParam(name = "flavorId") int flavorId) {
        ResponseDTO responseDTO = warehouseReceiveItemService.showByFlavorId(flavorId);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show/countbyflavorid")
    @ResponseBody
    public int countByFlavorId(@RequestParam(name = "flavorId") int flavorId) {
        int flavorQuantity = warehouseReceiveItemService.countByFlavorId(flavorId);
        return flavorQuantity;
    }
}
