package com.fanstatic.service.model;

import java.util.Date;

import org.apache.tomcat.util.http.ResponseUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.shitfhandover.EndShiftRequestDTO;
import com.fanstatic.dto.model.shitfhandover.ShiftHandoverDTO;
import com.fanstatic.dto.model.shitfhandover.StartShiftRequestDTO;
import com.fanstatic.dto.model.user.UserCompactDTO;
import com.fanstatic.model.ShiftHandover;
import com.fanstatic.model.User;
import com.fanstatic.repository.ShiftHandoverRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.DateUtils;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShiftHandoverService {
    private final ShiftHandoverRepository shiftHandoverRepository;
    private final SystemService systemService;
    private final ModelMapper modelMapper;

    public boolean checkUserStartShift(User user) {
        ShiftHandover shiftHandover = shiftHandoverRepository.findLatestShiftHandover(user,
                DateUtils.getDateBefore(24)).orElse(null);
        if (shiftHandover == null) {
            return false;
        }

        if (shiftHandover.getEndShiftTime() != null) {
            return false;
        }
        return true;
    }

    public boolean checkUserEndShift(User user) {
        ShiftHandover shiftHandover = shiftHandoverRepository.findLatestShiftHandover(user,
                DateUtils.getDateBefore(24)).orElse(null);
        if (shiftHandover == null) {
            return false;
        }
        if (shiftHandover.getEndShiftTime() != null) {
            return true;
        }
        return false;
    }

    public ResponseDTO endShift(EndShiftRequestDTO endShiftRequestDTO) {

        // check to day is other shift but not end
        ShiftHandover shiftHandover = shiftHandoverRepository.findLatestShiftHandover(systemService.getUserLogin(),
                DateUtils.getDateBefore(24)).orElse(null);
        if (shiftHandover.getEndShiftTime() != null) {
            return ResponseUtils.fail(500, "Ca làm đã kết thúc", null);
        }

        shiftHandover.setUpdateAt(new Date());
        shiftHandover.setUpdateBy(systemService.getUserLogin());
        shiftHandover.setEndShiftTime(new Date());
        shiftHandover.setNote(shiftHandover.getNote() + " - " + endShiftRequestDTO.getNote());
        shiftHandover.setEndShiftCash(endShiftRequestDTO.getEndShiftCash());

        // shiftHandover.setCashHandover(cashDiff);

        shiftHandoverRepository.saveAndFlush(shiftHandover);

        ShiftHandoverDTO shiftHandoverDTO = modelMapper.map(shiftHandover, ShiftHandoverDTO.class);
        shiftHandoverDTO.setEmployee(modelMapper.map(systemService.getUserLogin(), UserCompactDTO.class));

        return ResponseUtils.success(200, "Kết thúc ca thành công", shiftHandoverDTO);
    }

    public ResponseDTO startShift(StartShiftRequestDTO shiftRequestDTO) {

        // check to day is other shift but not end
        if (checkUserStartShift(systemService.getUserLogin())) {
            return ResponseUtils.fail(500, "Ca làm đã được bắt đầu", null);
        }

        ShiftHandover shiftHandover = new ShiftHandover();
        shiftHandover.setCreateAt(new Date());
        shiftHandover.setCreateBy(systemService.getUserLogin());
        shiftHandover.setStartShiftTime(new Date());
        shiftHandover.setNote(shiftRequestDTO.getNote());
        shiftHandover.setStartShiftCash(shiftRequestDTO.getStartShiftCash());

        shiftHandoverRepository.saveAndFlush(shiftHandover);

        ShiftHandoverDTO shiftHandoverDTO = modelMapper.map(shiftHandover, ShiftHandoverDTO.class);
        shiftHandoverDTO.setEmployee(modelMapper.map(systemService.getUserLogin(), UserCompactDTO.class));

        return ResponseUtils.success(200, "Bắt đầu ca thành công", shiftHandoverDTO);
    }
}
