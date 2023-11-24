package com.fanstatic.service.model;

import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.WarehouseDeliverReason.WarehouseDeliverReasonDTO;
import com.fanstatic.model.WarehouseDeliverReason;
import com.fanstatic.repository.WarehouseDeliverReasonRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseDeliverReasonService {
    private final WarehouseDeliverReasonRepository warehouseDeliverReasonRepository;

    private final ModelMapper modelMapper;

    private final SystemService systemService;

    public ResponseDTO show(int active) {//truyền tham số active vào để show list voucher dựa vào trạng thái
        //Tạo 1 list voucher mới chưa có dữ liệu
        List<WarehouseDeliverReason> warehouseDeliverReasonList = new ArrayList<>();

        //Dựa vào biến active mà sẽ cho hiển thị list voucher mong muốn
        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                warehouseDeliverReasonList = warehouseDeliverReasonRepository.findAllByOrderByCreateAtDesc();
                break;
            case RequestParamConst.ACTIVE_FALSE:
                warehouseDeliverReasonList = warehouseDeliverReasonRepository.findAllByActiveIsFalseOrderByCreateAtDesc().orElse(warehouseDeliverReasonList);
                break;
            case RequestParamConst.ACTIVE_TRUE:
                warehouseDeliverReasonList = warehouseDeliverReasonRepository.findAllByActiveIsTrueOrderByCreateAtDesc().orElse(warehouseDeliverReasonList);
                break;
            default:
                warehouseDeliverReasonList = warehouseDeliverReasonRepository.findAllByOrderByCreateAtDesc();
                break;
        }
//        Bắt đầu từ đoạn này chủ yếu để cấu hình cho json trả về theo dạng nào
        //Tạo 1 list voucher dto chưa có dữ liệu
        List<ResponseDataDTO> warehouseDeliverReasonDTOList = new ArrayList<>();

        //Lập qua vòng for này để map dữ liệu voucher vào voucherDTO
        for (WarehouseDeliverReason warehouseDeliverReason : warehouseDeliverReasonList) {
            WarehouseDeliverReasonDTO warehouseDeliverReasonDTO = new WarehouseDeliverReasonDTO();
            modelMapper.map(warehouseDeliverReason, warehouseDeliverReasonDTO);
            warehouseDeliverReasonDTOList.add(warehouseDeliverReasonDTO);
        }

        //Tạo 1 đối tượng respones list data mới
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();

        /*Trong đối tượng response list data sẽ có thuộc tính "datas"
        -> nên ta set cho nó bằng list voucherDtos vừa có lúc nãy
         */
        reponseListDataDTO.setDatas(warehouseDeliverReasonDTOList);

        /*
        Trong đối tượng response util chứa method success.
        Tại sao lại truyền responseListDataDTO vào?
         */
        return ResponseUtils.success(200, "Danh sách lý do xuất kho", reponseListDataDTO);
    }
}
