package com.fanstatic.controller.manage;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.warehouseInventory.WarehouseInventoryRequestDTO;
import com.fanstatic.dto.model.warehouseInventory.WarehouseInventoryRequestDeleteDTO;
import com.fanstatic.service.model.WarehouseInventoryService;
import com.fanstatic.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/manage/warehouseinventory")
@AllArgsConstructor
public class WarehouseInventoryController {
    private final WarehouseInventoryService warehouseInventoryService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid WarehouseInventoryRequestDTO data) {
        ResponseDTO responseDTO = warehouseInventoryService.create(data);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") int id, @RequestBody WarehouseInventoryRequestDeleteDTO warehouseInventoryRequestDeleteDTO) {
        ResponseDTO responseDTO = warehouseInventoryService.delete(id, warehouseInventoryRequestDeleteDTO);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/restore/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restore(@PathVariable("id") int id) {
        ResponseDTO responseDTO = warehouseInventoryService.restore(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO responseDTO = warehouseInventoryService.show(active);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> detail(@PathVariable("id") int id) {
        ResponseDTO responseDTO = warehouseInventoryService.detail(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }
}
