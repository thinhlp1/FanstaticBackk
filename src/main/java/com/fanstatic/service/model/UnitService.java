package com.fanstatic.service.model;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.unit.UnitDTO;
import com.fanstatic.dto.model.unit.UnitRequestDTO;
import com.fanstatic.model.Unit;
import com.fanstatic.repository.UnitRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitService {
    private final ModelMapper modelMapper;
    private final UnitRepository unitRepository;
    private final SystemService systemService;

    public ResponseDTO create(UnitRequestDTO unitRequestDTO) {

        List<FieldError> errors = new ArrayList<>();
        if (unitRepository.findByNameAndActiveIsTrue(unitRequestDTO.getName()).isPresent()) {
            errors.add(new FieldError("unitRequestDTO", "name", "Tên đơn vị đã tồn tại"));
        }

        if (unitRepository.findBySignAndActiveIsTrue(unitRequestDTO.getSign()).isPresent()) {
            errors.add(new FieldError("unitRequestDTO", "sign", "Chữ kí đơn vị đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        Unit unit = modelMapper.map(unitRequestDTO, Unit.class);

        unit.setActive(DataConst.ACTIVE_TRUE);
        unit.setCreateAt(new Date());
        unit.setCreateBy(systemService.getUserLogin());

        Unit unitSaved = unitRepository.save(unit);

        Unit unitSaved2 = unitRepository.findByNameAndActiveIsTrue(unitSaved.getName()).orElse(null);

        systemService.writeSystemLog(unitSaved2.getId(), unitSaved2.getName(), null);
        return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);

    }

    public ResponseDTO update(UnitRequestDTO unitRequestDTO) {

        Unit unit = unitRepository.findByIdAndActiveIsTrue(unitRequestDTO.getId()).orElse(null);

        if (unit == null) {
            return ResponseUtils.fail(401, "Đơn vị không tồn tại", null);
        }

        List<FieldError> errors = new ArrayList<>();
        if (unitRepository.findByNameAndActiveIsTrueAndIdNot(unitRequestDTO.getName(), unitRequestDTO.getId())
                .isPresent()) {
            errors.add(new FieldError("unitRequestDTO", "code", "Tên đơn vị đã tồn tại"));
        }

        if (unitRepository.findBySignAndActiveIsTrueAndIdNot(unitRequestDTO.getSign(), unitRequestDTO.getId())
                .isPresent()) {
            errors.add(new FieldError("unitRequestDTO", "name", "Chữ kí đơn vị đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        modelMapper.map(unitRequestDTO, unit);

        unit.setUpdateAt(new Date());
        unit.setUpdateBy(systemService.getUserLogin());

        Unit unitSaved = unitRepository.save(unit);

        systemService.writeSystemLog(unitSaved.getId(), unitSaved.getName(), null);

        return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

    }

    public ResponseDTO delete(String id) {

        Unit unit = unitRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (unit == null) {
            return ResponseUtils.fail(401, "Đơn vị không tồn tại", null);
        }

        unit.setActive(DataConst.ACTIVE_FALSE);
        unit.setDeleteAt(new Date());
        unit.setDeleteBy(systemService.getUserLogin());

        Unit unitSaved = unitRepository.save(unit);

        systemService.writeSystemLog(unit.getId(), unit.getName(), null);
        return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);

    }

    public ResponseDTO restore(String id) {

        Unit unit = unitRepository.findByIdAndActiveIsFalse(id).orElse(null);

        if (unit == null) {
            return ResponseUtils.fail(401, "Đơn vị không tồn tại", null);
        }

        unit.setActive(DataConst.ACTIVE_TRUE);
        unit.setUpdateAt(new Date());
        unit.setUpdateBy(systemService.getUserLogin());

        Unit unitSaved = unitRepository.save(unit);

        systemService.writeSystemLog(unitSaved.getId(), unitSaved.getName(), null);

        return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);

    }

    public ResponseDTO detail(String id) {

        Unit unit = unitRepository.findById(id).orElse(null);

        if (unit == null) {
            return ResponseUtils.fail(401, "Đơn vị không tồn tại", null);
        }

        UnitDTO unitDTO = modelMapper.map(unit, UnitDTO.class);

        return ResponseUtils.success(200, "Chi tiết đơn vị", unitDTO);
    }

    public ResponseDTO show(int active) {
        List<Unit> units = new ArrayList<>();

        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                units = unitRepository.findAllByOrderByCreateAtDesc();
                break;
            case RequestParamConst.ACTIVE_TRUE:
                units = unitRepository.findAllByActiveIsTrueOrderByCreateAtDesc().orElse(units);
                break;
            case RequestParamConst.ACTIVE_FALSE:
                units = unitRepository.findAllByActiveIsFalseOrderByCreateAtDesc().orElse(units);
                break;
            default:
                units = unitRepository.findAllByOrderByCreateAtDesc();
                break;
        }
        List<ResponseDataDTO> unitDTOS = new ArrayList<>();

        for (Unit unit : units) {
            UnitDTO unitDTO = new UnitDTO();
            modelMapper.map(unit, unitDTO);

            unitDTOS.add(unitDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(unitDTOS);
        return ResponseUtils.success(200, "Danh sách unit", reponseListDataDTO);
    }

    public UnitDTO getDTOById(String id) {
        Unit unit = unitRepository.findById(id).orElse(null);
        if (unit != null) {
            return modelMapper.map(unit, UnitDTO.class);
        } else {
            return null;
        }
    }
}
