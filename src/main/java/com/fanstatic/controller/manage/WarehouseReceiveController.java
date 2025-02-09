package com.fanstatic.controller.manage;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.warehouseReceive.WarehouseReceiveRequestDTO;
import com.fanstatic.dto.model.warehouseReceive.WarehouseReceiveRequestDeleteDTO;
import com.fanstatic.service.model.WarehouseReceiveService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/api/manage/warehousereceive")
@AllArgsConstructor
public class WarehouseReceiveController {
    private final WarehouseReceiveService warehouseReceiveService;

    private final SystemService systemService;
    /*
        systemService
        userService -> user = systemService.getUserLogin
     */

    @GetMapping("/create/getUser")
    public ResponseEntity<ResponseDTO> getUser() {
        ResponseDTO responseDTO = warehouseReceiveService.getUser(systemService.getUserLogin().getId());
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestPart @Valid WarehouseReceiveRequestDTO data, @RequestPart Optional<MultipartFile> imageFile) {
        data.setImageFile(imageFile);
        ResponseDTO responseDTO = warehouseReceiveService.create(data);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    //    @PutMapping("/update/{id}")
//    @ResponseBody
//    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid WarehouseReceiveRequestDTO data, @PathVariable("id") int id) {
//        ResponseDTO responseDTO = warehouseReceiveService.update(data);
//        return ResponseUtils.returnReponsetoClient(responseDTO);
//    }
//
//    @PutMapping("/update/image/{id}")
//    @ResponseBody
//    public ResponseEntity<ResponseDTO> updateImage(@RequestPart MultipartFile imageFile,
//                                                   @PathVariable("id") Integer id) {
//        ResponseDTO responseDTO = warehouseReceiveService.updateImage(id, imageFile);
//        return ResponseUtils.returnReponsetoClient(responseDTO);
//    }
//
    @PutMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") int id, @RequestBody WarehouseReceiveRequestDeleteDTO warehouseReceiveRequestDeleteDTO) {
        ResponseDTO responseDTO = warehouseReceiveService.delete(id, warehouseReceiveRequestDeleteDTO);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/restore/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restore(@PathVariable("id") int id) {
        ResponseDTO responseDTO = warehouseReceiveService.restore(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO responseDTO = warehouseReceiveService.show(active);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> detail(@PathVariable("id") int id) {
        ResponseDTO responseDTO = warehouseReceiveService.detail(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }
}
