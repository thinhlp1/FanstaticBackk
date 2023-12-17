package com.fanstatic.service.model;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.shift.ShiftDTO;
import com.fanstatic.model.Shift;
import com.fanstatic.dto.model.shift.ShiftRequestDTO;
import com.fanstatic.repository.ShiftRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.DateUtils;
import com.fanstatic.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftService {
    private final ModelMapper modelMapper;
    private final ShiftRepository shiftRepository;
    private final SystemService systemService;

    public ResponseDTO create(ShiftRequestDTO shiftRequestDTO) {

        List<FieldError> errors = new ArrayList<>();
        
        if (shiftRepository.findByCodeAndActiveIsTrue(shiftRequestDTO.getCode()).isPresent()) {
            errors.add(new FieldError("shiftRequestDTO", "code", "Code đã tồn tại"));
        }

        if (shiftRepository.findByShiftAndActiveIsTrue(shiftRequestDTO.getShift()).isPresent()) {
            errors.add(new FieldError("shiftRequestDTO", "shift", "Ca làm đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        Shift shift = modelMapper.map(shiftRequestDTO, Shift.class);

        shift.setActive(DataConst.ACTIVE_TRUE);
        shift.setCreateAt(new Date());
        shift.setCreateBy(systemService.getUserLogin());
        LocalTime start = LocalTime.parse(shiftRequestDTO.getStartAt());
        LocalTime end = LocalTime.parse(shiftRequestDTO.getEndAt());
        shift.setStartAt(Time.valueOf(start));
        shift.setEndAt(Time.valueOf(end));

        Shift shiftSaved = shiftRepository.save(shift);

        Shift shiftSave2 = shiftRepository.findByCodeAndActiveIsTrue(shiftSaved.getCode()).orElse(null);

        systemService.writeSystemLog(shiftSave2.getId(), shiftSave2.getCode(), null);
        return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);

    }

    public ResponseDTO update(ShiftRequestDTO shiftRequestDTO) {

        Shift shift = shiftRepository.findByIdAndActiveIsTrue(shiftRequestDTO.getId()).orElse(null);

        if (shift == null) {
            return ResponseUtils.fail(401, "Ca làm không tồn tại", null);
        }

        List<FieldError> errors = new ArrayList<>();
        if (shiftRepository.findByCodeAndActiveIsTrueAndIdNot(shiftRequestDTO.getCode(), shiftRequestDTO.getId())
                .isPresent()) {
            errors.add(new FieldError("shiftRequestDTO", "code", "Code đã tồn tại"));
        }

        if (shiftRepository.findByShiftAndActiveIsTrueAndIdNot(shiftRequestDTO.getShift(), shiftRequestDTO.getId())
                .isPresent()) {
            errors.add(new FieldError("shiftRequestDTO", "name", "Ca làm đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        modelMapper.map(shiftRequestDTO, shift);
        shift.setShift(shiftRequestDTO.getShift());
        shift.setUpdateAt(new Date());
        shift.setUpdateBy(systemService.getUserLogin());
        LocalTime start = LocalTime.parse(shiftRequestDTO.getStartAt());
        LocalTime end = LocalTime.parse(shiftRequestDTO.getEndAt());
        shift.setStartAt(Time.valueOf(start));
        shift.setEndAt(Time.valueOf(end));
      
        Shift shiftSaved = shiftRepository.save(shift);

        systemService.writeSystemLog(shiftSaved.getId(), shiftSaved.getShift(), null);

        return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

    }

    public ResponseDTO delete(int id) {

        Shift shift = shiftRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (shift == null) {
            return ResponseUtils.fail(401, "Shift không tồn tại", null);
        }

        shift.setActive(DataConst.ACTIVE_FALSE);
        shift.setDeleteAt(new Date());
        shift.setDeleteBy(systemService.getUserLogin());

        Shift shiftSaved = shiftRepository.save(shift);

        systemService.writeSystemLog(shift.getId(), shift.getShift(), null);
        return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);

    }

    public ResponseDTO restore(int id) {

        Shift shift = shiftRepository.findByIdAndActiveIsFalse(id).orElse(null);

        if (shift == null) {
            return ResponseUtils.fail(401, "Shift không tồn tại", null);
        }

        shift.setActive(DataConst.ACTIVE_TRUE);
        shift.setUpdateAt(new Date());
        shift.setUpdateBy(systemService.getUserLogin());

        Shift shiftSaved = shiftRepository.save(shift);

        systemService.writeSystemLog(shiftSaved.getId(), shiftSaved.getShift(), null);

        return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);

    }

    public ResponseDTO detail(int id) {

        Shift shift = shiftRepository.findById(id).orElse(null);

        if (shift == null) {
            return ResponseUtils.fail(401, "Ca làm không tồn tại", null);
        }

        ShiftDTO shiftDTO = modelMapper.map(shift, ShiftDTO.class);

        return ResponseUtils.success(200, "Chi tiết ca làm", shiftDTO);
    }

    public ResponseDTO show(int active) {
        List<Shift> shifts = new ArrayList<>();

        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                shifts = shiftRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_FALSE:
                shifts = shiftRepository.findAllByActiveIsTrue().orElse(shifts);
                break;
            case RequestParamConst.ACTIVE_TRUE:
                shifts = shiftRepository.findAllByActiveIsFalse().orElse(shifts);
                break;
            default:
                shifts = shiftRepository.findAll();
                break;
        }
        List<ResponseDataDTO> shiftDTOS = new ArrayList<>();

        for (Shift shift : shifts) {
            ShiftDTO shiftDTO = new ShiftDTO();
            modelMapper.map(shift, shiftDTO);

            shiftDTOS.add(shiftDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(shiftDTOS);
        return ResponseUtils.success(200, "Danh sách shift", reponseListDataDTO);
    }

    public ShiftDTO getDTOById(int id) {
        Shift shift = shiftRepository.findById(id).orElse(null);
        if (shift != null) {
            return modelMapper.map(shift, ShiftDTO.class);
        } else {
            return null;
        }
    }
}
