package com.fanstatic.controller.manage;

import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.size.SizeRequestDTO;
import com.fanstatic.dto.model.table.TableRequestDTO;
import com.fanstatic.dto.model.table.TableTypeRequestDTO;
import com.fanstatic.service.model.TableService;
import com.fanstatic.service.model.TableTypeService;
import com.fanstatic.util.ResponseUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/manage/table")
@AllArgsConstructor
public class TableController {

    private final TableTypeService tableTypeService;
    private final TableService tableService;

    @PostMapping("/create/table-type")
    @ResponseBody
    public ResponseEntity<ResponseDTO> createTableType(@RequestPart @Valid TableTypeRequestDTO data,
            @RequestPart MultipartFile image) {
        data.setImage(image);
        ResponseDTO reponseDTO = tableTypeService.create(data);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/table-type/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid TableTypeRequestDTO tableTypeRequestDTO,
            @PathVariable("id") int id) {
        ResponseDTO reponseDTO = tableTypeService.update(tableTypeRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update/image/table-type/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateImage(@RequestPart MultipartFile image,
            @PathVariable("id") Integer id) {
        ResponseDTO reponseDTO = tableTypeService.updateImage(id, image);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @DeleteMapping("/delete/table-type/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = tableTypeService.delete(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/create/check-exits")
    @ResponseBody
    public ResponseEntity<ResponseDTO> check(@RequestParam(name = "number") int number) {
        ResponseDTO reponseDTO = tableService.checkExits(number);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> createTable(@RequestBody @Valid TableRequestDTO tableRequestDTO) {
        ResponseDTO reponseDTO = tableService.save(tableRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PostMapping("/create/layout")
    @ResponseBody
    public ResponseEntity<ResponseDTO> saveLayout(@RequestPart MultipartFile tableLayout) {
        ResponseDTO reponseDTO = tableService.saveLayout(tableLayout);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }


    // @GetMapping("/show/detail/{id}")
    // @ResponseBody
    // public ResponseEntity<ResponseDTO> detail(@PathVariable("id") int id) {
    // ResponseDTO reponseDTO = sizeService.detail(id);
    // return ResponseUtils.returnReponsetoClient(reponseDTO);
    // }
}
