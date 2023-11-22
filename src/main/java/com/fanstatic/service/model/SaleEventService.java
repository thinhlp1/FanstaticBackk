package com.fanstatic.service.model;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.saleevent.SaleEventRequestDTO;
import com.fanstatic.model.SaleEvent;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.saleevent.SaleEventDTO;
import com.fanstatic.repository.SaleEventRepository;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleEventService {
    private final ModelMapper modelMapper;
    private final SaleEventRepository saleEventRepository;
    private final SystemService systemService;

    public ResponseDTO create(SaleEventRequestDTO saleEventRequestDTO) {


        SaleEvent saleevent = modelMapper.map(saleEventRequestDTO, SaleEvent.class);

        saleevent.setActive(DataConst.ACTIVE_TRUE);
        saleevent.setCreateAt(new Date());
        saleevent.setCreateBy(systemService.getUserLogin());

        SaleEvent saleeventSaved = saleEventRepository.saveAndFlush(saleevent);

        systemService.writeSystemLog(saleeventSaved.getId(), saleeventSaved.getPercent() + "", null);
        return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);

    }

    public ResponseDTO update(SaleEventRequestDTO saleEventRequestDTO) {

        SaleEvent saleevent = saleEventRepository.findByIdAndActiveIsTrue(saleEventRequestDTO.getId()).orElse(null);

        if (saleevent == null) {
            return ResponseUtils.fail(401, "Sự kiện không tồn tại", null);
        }

        modelMapper.map(saleEventRequestDTO, saleevent);

        saleevent.setUpdateAt(new Date());
        saleevent.setUpdateBy(systemService.getUserLogin());
        saleevent.setActive(DataConst.ACTIVE_TRUE);
        SaleEvent saleeventSaved = saleEventRepository.save(saleevent);

        systemService.writeSystemLog(saleeventSaved.getId(), saleeventSaved.getPercent() + "", null);

        return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

    }

    public ResponseDTO delete(int id) {

        SaleEvent saleevent = saleEventRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (saleevent == null) {
            return ResponseUtils.fail(401, "Sự kiện không tồn tại", null);
        }

        saleevent.setActive(DataConst.ACTIVE_FALSE);
        saleevent.setDeleteAt(new Date());
        saleevent.setDeleteBy(systemService.getUserLogin());

        SaleEvent saleeventSaved = saleEventRepository.save(saleevent);

        systemService.writeSystemLog(saleevent.getId(), saleevent.getId() + "", null);
        return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);

    }

    public ResponseDTO restore(int id) {

        SaleEvent saleevent = saleEventRepository.findByIdAndActiveIsFalse(id).orElse(null);

        if (saleevent == null) {
            return ResponseUtils.fail(401, "Sự kiện không tồn tại", null);
        }

        saleevent.setActive(DataConst.ACTIVE_TRUE);
        saleevent.setUpdateAt(new Date());
        saleevent.setUpdateBy(systemService.getUserLogin());

        SaleEvent saleeventSaved = saleEventRepository.save(saleevent);

        systemService.writeSystemLog(saleeventSaved.getId(), saleeventSaved.getPercent() + "", null);

        return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);

    }

    public ResponseDTO detail(int id) {

        SaleEvent saleevent = saleEventRepository.findById(id).orElse(null);

        if (saleevent == null) {
            return ResponseUtils.fail(401, "SaleEvent không tồn tại", null);
        }

        SaleEventDTO saleeventDTO = modelMapper.map(saleevent, SaleEventDTO.class);

        return ResponseUtils.success(200, "Chi tiết sự kiện.", saleeventDTO);

    }

    public ResponseDTO show(int active) {
        List<SaleEvent> saleevents = new ArrayList<>();

        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                saleevents = saleEventRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_FALSE:
                saleevents = saleEventRepository.findAllByActiveIsFalse().orElse(saleevents);
                break;
            case RequestParamConst.ACTIVE_TRUE:   
                 saleevents = saleEventRepository.findAllByActiveIsTrue().orElse(saleevents);
                break;
            default:
                saleevents = saleEventRepository.findAll();
                break;
        }
        List<ResponseDataDTO> saleeventDTOS = new ArrayList<>();

        for (SaleEvent saleevent : saleevents) {
            SaleEventDTO saleeventDTO = new SaleEventDTO();
            modelMapper.map(saleevent, saleeventDTO);

            saleeventDTOS.add(saleeventDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(saleeventDTOS);
        return ResponseUtils.success(200, "Danh sách sự kiện.", reponseListDataDTO);
    }

}
