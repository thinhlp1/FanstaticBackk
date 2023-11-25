package com.fanstatic.service.model;

import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.warehouseDeliverSolution.WarehouseDeliverSolutionDTO;
import com.fanstatic.model.WarehouseDeliverSolution;
import com.fanstatic.repository.WarehouseDeliverSolutionRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseDeliverSolutionService {
    private final WarehouseDeliverSolutionRepository warehouseDeliverSolutionRepository;

    private final ModelMapper modelMapper;

    private final SystemService systemService;

    public ResponseDTO show(int active) {//truyền tham số active vào để show list voucher dựa vào trạng thái
        //Tạo 1 list voucher mới chưa có dữ liệu
        List<WarehouseDeliverSolution> warehouseDeliverSolutionList = new ArrayList<>();

        //Dựa vào biến active mà sẽ cho hiển thị list voucher mong muốn
        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                warehouseDeliverSolutionList = warehouseDeliverSolutionRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_FALSE:
                warehouseDeliverSolutionList = warehouseDeliverSolutionRepository.findAllByActiveIsFalse().orElse(warehouseDeliverSolutionList);
                break;
            case RequestParamConst.ACTIVE_TRUE:
                warehouseDeliverSolutionList = warehouseDeliverSolutionRepository.findAllByActiveIsTrue().orElse(warehouseDeliverSolutionList);
                break;
            default:
                warehouseDeliverSolutionList = warehouseDeliverSolutionRepository.findAll();
                break;
        }
//        Bắt đầu từ đoạn này chủ yếu để cấu hình cho json trả về theo dạng nào
        //Tạo 1 list voucher dto chưa có dữ liệu
        List<ResponseDataDTO> warehouseDeliverSolutionDTOList = new ArrayList<>();

        //Lập qua vòng for này để map dữ liệu voucher vào voucherDTO
        for (WarehouseDeliverSolution warehouseDeliverSolution : warehouseDeliverSolutionList) {
            WarehouseDeliverSolutionDTO warehouseDeliverSolutionDTO = new WarehouseDeliverSolutionDTO();
            modelMapper.map(warehouseDeliverSolution, warehouseDeliverSolutionDTO);
            warehouseDeliverSolutionDTOList.add(warehouseDeliverSolutionDTO);
        }

        //Tạo 1 đối tượng respones list data mới
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();

        /*Trong đối tượng response list data sẽ có thuộc tính "datas"
        -> nên ta set cho nó bằng list voucherDtos vừa có lúc nãy
         */
        reponseListDataDTO.setDatas(warehouseDeliverSolutionDTOList);

        /*
        Trong đối tượng response util chứa method success.
        Tại sao lại truyền responseListDataDTO vào?
         */
        return ResponseUtils.success(200, "Danh sách giải pháp xuất kho", reponseListDataDTO);
    }
}
