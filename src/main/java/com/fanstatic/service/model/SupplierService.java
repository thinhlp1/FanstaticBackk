package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.supplier.SupplierDTO;
import com.fanstatic.dto.model.supplier.SupplierRequestDTO;
import com.fanstatic.model.Supplier;
import com.fanstatic.repository.SupplierRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final ModelMapper modelMapper;
    private final SupplierRepository supplierRepository;
    private final SystemService systemService;
      public ResponseDTO create(SupplierRequestDTO supplierRequestDTO) {

        List<FieldError> errors = new ArrayList<>();
        if (supplierRepository.findByNameAndActiveIsTrue(supplierRequestDTO.getName()).isPresent()) {
            errors.add(new FieldError("supplierRequestDTO", "name", "Tên nhà cung cấp đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        Supplier supplier = modelMapper.map(supplierRequestDTO, Supplier.class);

        supplier.setActive(DataConst.ACTIVE_TRUE);
        supplier.setCreateAt(new Date());
        supplier.setCreateBy(systemService.getUserLogin());

        Supplier supplierSaved = supplierRepository.saveAndFlush(supplier);

        if (supplierSaved != null) {

            systemService.writeSystemLog(supplierSaved.getId(), supplierSaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);

        }
        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);

    }

    public ResponseDTO update(SupplierRequestDTO supplierRequestDTO) {

        Supplier supplier = supplierRepository.findByIdAndActiveIsTrue(supplierRequestDTO.getId()).orElse(null);

        if (supplier == null) {
            return ResponseUtils.fail(401, "Supplier không tồn tại", null);
        }

        List<FieldError> errors = new ArrayList<>();
        if (supplierRepository.findByNameAndActiveIsTrueAndIdNot(supplierRequestDTO.getName(), supplierRequestDTO.getId())
                .isPresent()) {
            errors.add(new FieldError("supplierRequestDTO", "supplier", "supplier đã tồn tại"));
        }

     

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        modelMapper.map(supplierRequestDTO, supplier);

        supplier.setUpdateAt(new Date());
        supplier.setUpdateBy(systemService.getUserLogin());

        Supplier supplierSaved = supplierRepository.save(supplier);

        if (supplierSaved != null) {
            systemService.writeSystemLog(supplierSaved.getId(), supplierSaved.getName(), null);

            return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

        }
        return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);

    }

    public ResponseDTO delete(int id) {

        Supplier supplier = supplierRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (supplier == null) {
            return ResponseUtils.fail(401, "Size không tồn tại", null);
        }

        supplier.setActive(DataConst.ACTIVE_FALSE);
        supplier.setDeleteAt(new Date());
        supplier.setDeleteBy(systemService.getUserLogin());

        Supplier supplierSaved = supplierRepository.save(supplier);

        if (supplierSaved != null) {
            systemService.writeSystemLog(supplier.getId(), supplier.getName(), null);
            return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);

        }
        return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);

    }

    public ResponseDTO restore(int id) {

        Supplier supplier = supplierRepository.findByIdAndActiveIsFalse(id).orElse(null);

        if (supplier == null) {
            return ResponseUtils.fail(401, "Supplier không tồn tại", null);
        }

        supplier.setActive(DataConst.ACTIVE_TRUE);
        supplier.setUpdateAt(new Date());
        supplier.setUpdateBy(systemService.getUserLogin());

        Supplier supplierSaved = supplierRepository.save(supplier);

        if (supplierSaved != null) {
            systemService.writeSystemLog(supplierSaved.getId(), supplierSaved.getName(), null);

            return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);

        }
        return ResponseUtils.fail(500, MessageConst.RESTORE_FAIL, null);

    }

    public ResponseDTO detail(int id) {

        Supplier supplier = supplierRepository.findById(id).orElse(null);

        if (supplier == null) {
            return ResponseUtils.fail(401, "Size không tồn tại", null);
        }

        SupplierDTO supplierDTO = modelMapper.map(supplier, SupplierDTO.class);

        return ResponseUtils.success(200, "Chi tiết suplier", supplierDTO);

    }
    public ResponseDTO show(int active) {
        List<Supplier> suppliers = new ArrayList<>();

        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                suppliers = supplierRepository.findAllByOrderByCreateAtDesc();
                break;
            case RequestParamConst.ACTIVE_TRUE:
                suppliers = supplierRepository.findAllByActiveIsTrueOrderByCreateAtDesc().orElse(suppliers);
                break;
            case RequestParamConst.ACTIVE_FALSE:
                suppliers = supplierRepository.findAllByActiveIsFalseOrderByCreateAtDesc().orElse(suppliers);
                break;
            default:
                suppliers = supplierRepository.findAllByOrderByCreateAtDesc();
                break;
        }
        List<ResponseDataDTO> supplierDTOS = new ArrayList<>();

        for (Supplier supplier : suppliers) {
            SupplierDTO supplierDTO = new SupplierDTO();
            modelMapper.map(supplier, supplierDTO);

            supplierDTOS.add(supplierDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(supplierDTOS);
        return ResponseUtils.success(200, "Danh sách supplier", reponseListDataDTO);
    }

}
