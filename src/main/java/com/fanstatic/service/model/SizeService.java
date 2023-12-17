package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fanstatic.dto.model.size.SizeRequestDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.size.SizeDTO;
import com.fanstatic.model.Size;
import com.fanstatic.repository.SizeRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SizeService {
    private final ModelMapper modelMapper;
    private final SizeRepository sizeRepository;
    private final SystemService systemService;

    public ResponseDTO create(SizeRequestDTO sizeRequestDTO) {

        List<FieldError> errors = new ArrayList<>();
        if (sizeRepository.findByCodeAndActiveIsTrue(sizeRequestDTO.getCode()).isPresent()) {
            errors.add(new FieldError("sizeRequestDTO", "code", "Code đã tồn tại"));
        }

        if (sizeRepository.findByNameAndActiveIsTrue(sizeRequestDTO.getName()).isPresent()) {
            errors.add(new FieldError("sizeRequestDTO", "name", "Size đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        Size size = modelMapper.map(sizeRequestDTO, Size.class);

        size.setActive(DataConst.ACTIVE_TRUE);
        size.setCreateAt(new Date());
        size.setCreateBy(systemService.getUserLogin());

        Size sizeSaved = sizeRepository.saveAndFlush(size);

        if (sizeSaved != null) {

            systemService.writeSystemLog(sizeSaved.getId(), sizeSaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);

        }
        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);

    }

    public ResponseDTO update(SizeRequestDTO sizeRequestDTO) {

        Size size = sizeRepository.findByIdAndActiveIsTrue(sizeRequestDTO.getId()).orElse(null);

        if (size == null) {
            return ResponseUtils.fail(401, "Size không tồn tại", null);
        }

        List<FieldError> errors = new ArrayList<>();
        if (sizeRepository.findByCodeAndActiveIsTrueAndIdNot(sizeRequestDTO.getCode(), sizeRequestDTO.getId())
                .isPresent()) {
            errors.add(new FieldError("sizeRequestDTO", "code", "Code đã tồn tại"));
        }

        if (sizeRepository.findByNameAndActiveIsTrueAndIdNot(sizeRequestDTO.getName(), sizeRequestDTO.getId())
                .isPresent()) {
            errors.add(new FieldError("sizeRequestDTO", "name", "Size đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        modelMapper.map(sizeRequestDTO, size);

        size.setUpdateAt(new Date());
        size.setUpdateBy(systemService.getUserLogin());

        Size sizeSaved = sizeRepository.save(size);

        if (sizeSaved != null) {
            systemService.writeSystemLog(sizeSaved.getId(), sizeSaved.getName(), null);

            return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

        }
        return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);

    }

    public ResponseDTO delete(int id) {

        Size size = sizeRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (size == null) {
            return ResponseUtils.fail(401, "Size không tồn tại", null);
        }

        size.setActive(DataConst.ACTIVE_FALSE);
        size.setDeleteAt(new Date());
        size.setDeleteBy(systemService.getUserLogin());

        Size sizeSaved = sizeRepository.save(size);

        if (sizeSaved != null) {
            systemService.writeSystemLog(size.getId(), size.getName(), null);
            return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);

        }
        return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);

    }

    public ResponseDTO restore(int id) {

        Size size = sizeRepository.findByIdAndActiveIsFalse(id).orElse(null);

        if (size == null) {
            return ResponseUtils.fail(401, "Size không tồn tại", null);
        }

        size.setActive(DataConst.ACTIVE_TRUE);
        size.setUpdateAt(new Date());
        size.setUpdateBy(systemService.getUserLogin());

        Size sizeSaved = sizeRepository.save(size);

        if (sizeSaved != null) {
            systemService.writeSystemLog(sizeSaved.getId(), sizeSaved.getName(), null);

            return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);

        }
        return ResponseUtils.fail(500, MessageConst.RESTORE_FAIL, null);

    }

    public ResponseDTO detail(int id) {

        Size size = sizeRepository.findById(id).orElse(null);

        if (size == null) {
            return ResponseUtils.fail(401, "Size không tồn tại", null);
        }

        SizeDTO sizeDTO = modelMapper.map(size, SizeDTO.class);

        return ResponseUtils.success(200, "Chi tiết size", sizeDTO);

    }

    public ResponseDTO show(int active) {
        List<Size> sizes = new ArrayList<>();

        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                sizes = sizeRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_TRUE:
                sizes = sizeRepository.findAllByActiveIsTrue().orElse(sizes);
                break;
            case RequestParamConst.ACTIVE_FALSE:
                sizes = sizeRepository.findAllByActiveIsFalse().orElse(sizes);
                break;
            default:
                sizes = sizeRepository.findAll();
                break;
        }
        List<ResponseDataDTO> sizeDTOS = new ArrayList<>();

        for (Size size : sizes) {
            SizeDTO sizeDTO = new SizeDTO();
            modelMapper.map(size, sizeDTO);

            sizeDTOS.add(sizeDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(sizeDTOS);
        return ResponseUtils.success(200, "Danh sách size", reponseListDataDTO);
    }

    public SizeDTO getDTOById(int id) {
        Size size = sizeRepository.findById(id).orElse(null);
        if (size != null) {
            return modelMapper.map(size, SizeDTO.class);
        } else {
            return null;
        }
    }
}
