package com.fanstatic.controller.manage;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.warehouseDeliver.WarehouseDeliverRequestDTO;
import com.fanstatic.dto.model.warehouseDeliver.WarehouseDeliverRequestDeleteDTO;
import com.fanstatic.service.model.WarehouseDeliverService;
import com.fanstatic.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/manage/warehousedeliver")
@AllArgsConstructor
public class WarehouseDeliverController {
    private final WarehouseDeliverService warehouseDeliverService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid WarehouseDeliverRequestDTO data) {
        ResponseDTO responseDTO = warehouseDeliverService.create(data);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") int id, @RequestBody WarehouseDeliverRequestDeleteDTO warehouseDeliverRequestDeleteDTO) {
        ResponseDTO responseDTO = warehouseDeliverService.delete(id, warehouseDeliverRequestDeleteDTO);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/restore/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restore(@PathVariable("id") int id) {
        ResponseDTO responseDTO = warehouseDeliverService.restore(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO responseDTO = warehouseDeliverService.show(active);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> detail(@PathVariable("id") int id) {
        ResponseDTO responseDTO = warehouseDeliverService.detail(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show/reason")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showReason(@RequestParam(name = "active") int active) {
        ResponseDTO responseDTO = warehouseDeliverService.showReason(active);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show/solution")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showSolution(@RequestParam(name = "active") int active) {
        ResponseDTO responseDTO = warehouseDeliverService.showSolution(active);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }
}
