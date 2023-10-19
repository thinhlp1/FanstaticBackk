package com.fanstatic.controller.manage;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.role.RoleRequestDTO;
import com.fanstatic.dto.model.user.UserRequestDTO;
import com.fanstatic.service.model.RolePermissionService;
import com.fanstatic.service.model.RoleService;
import com.fanstatic.util.ResponseUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/manage/role")
@AllArgsConstructor
public class RoleController {
    private final RolePermissionService rolePermissionService;
    private final RoleService roleService;

    // @GetMapping("/show/role-permission")
    // @ResponseBody
    // public ResponseEntity<ReponseDTO> getRolePermission() {
    // ReponseDTO reponseDTO = rolePermissionService.getRolePermisson();
    // return ReponseUtils.returnReponsetClient(reponseDTO);
    // }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid RoleRequestDTO roleRequestDTO) {
        ResponseDTO reponseDTO = roleService.create(roleRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid RoleRequestDTO roleRequestDTO) {
        ResponseDTO reponseDTO = roleService.update(roleRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = roleService.delete(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PutMapping("/restore/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> restore(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = roleService.restore(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show(@RequestParam(name = "active") int active) {
        ResponseDTO reponseDTO = roleService.show(active);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/role-permission/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> getRolePermission(@PathVariable("id") int id) {
        ResponseDTO reponseDTO = rolePermissionService.getRolePermisson(id);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/system-feature-permission")
    @ResponseBody
    public ResponseEntity<ResponseDTO> getFeaturePermission() {
        ResponseDTO reponseDTO = rolePermissionService.getFeaturePermission();
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }
}
