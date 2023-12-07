package com.fanstatic.service.model;

import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.flavor.FlavorDTO;
import com.fanstatic.dto.model.warehouseReceiveItem.WarehouseReceiveItemDTO;
import com.fanstatic.dto.model.warehouseReceiveItem.WarehouseReceiveItemRequestDTO;
import com.fanstatic.model.Flavor;
import com.fanstatic.model.WarehouseReceive;
import com.fanstatic.model.WarehouseReceiveItem;
import com.fanstatic.repository.FlavorRepository;
import com.fanstatic.repository.WarehouseReceiveItemRepository;
import com.fanstatic.repository.WarehouseReceiveRepository;
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
public class WarehouseReceiveItemService {
    private final SystemService systemService;

    private final ModelMapper modelMapper;
    private final FlavorRepository flavorRepository;
    private final WarehouseReceiveItemRepository warehouseReceiveItemRepository;
    private final WarehouseReceiveRepository warehouseReceiveRepository;

    public ResponseDTO create(WarehouseReceiveItemRequestDTO warehouseReceiveItemRequestDTO) {
        /*
        Tạo 1 đối tượng supplier dựa trên supplierId được truyền vào warehouseReceiveRequestDTO
         */
        Flavor flavor = flavorRepository.findByIdAndActiveIsTrue(warehouseReceiveItemRequestDTO.getFlavorId()).orElse(null);
        WarehouseReceive warehouseReceive = warehouseReceiveRepository.findByIdAndActiveIsTrue(warehouseReceiveItemRequestDTO.getWarehouseReceiveItemId()).orElse(null);

        List<FieldError> errors = new ArrayList<>();
        //Bắt lỗi danh mục không tồn tại -> cái này không câần in ra cho user, dev coi được rồi
        if (flavor == null) {
            errors.add(new FieldError("warehouseReceiveItemRequestDTO", "flavorId", "Nguyên liệu không tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        /*
        modelMapper.map(warehouseReceiveRequestDTO, warehouseReceive);
        warehouseReceive.setSupplier(supplier);
        warehouseReceive.setActive(DataConst.ACTIVE_TRUE);
        warehouseReceive.setUpdateAt(new Date());
        warehouseReceive.setUpdateBy(systemService.getUserLogin());
        WarehouseReceive warehouseReceiveSaved = warehouseReceiveRepository.save(warehouseReceive);
        if(warehouseReceiveSaved != null){
            for(WarehouseReceiveItem warehouseReceiveItem : listWarehouseReceiveItem){
                warehouseReceiveItem.setWarehouseReceiveId(warehouseReceiveSaved.getId());
                warehouseReceiveItem.create(warehouseReceiveItem);
            }
        }

        if (warehouseReceiveSaved != null) {
            systemService.writeSystemLog(warehouseReceiveSaved.getId(), warehouseReceiveSaved.getDescription(), null);
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
            return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);
        }
         */

        WarehouseReceiveItem warehouseReceiveItem = modelMapper.map(warehouseReceiveItemRequestDTO, WarehouseReceiveItem.class);
        warehouseReceiveItem.setWarehouseReceive(warehouseReceive);
        warehouseReceiveItem.setFlavor(flavor);
        warehouseReceiveItem.setCreateAt(new Date());
        warehouseReceiveItem.setCreateBy(systemService.getUserLogin());
        //save and flush dung cho việc save vào database và lấy dữ liệu lên ngay lập tức để xử lý
        WarehouseReceiveItem warehouseReceiveItemSaved = warehouseReceiveItemRepository.saveAndFlush(warehouseReceiveItem);

        if (warehouseReceiveItemSaved != null) {
            //Lưu thông tin vào lịch sử hệ thống
//            systemService.writeSystemLog(warehouseReceiveItemSaved.getId(), warehouseReceiveItemSaved.getWarehouseReceive().getId(), null);
            /*
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
             */
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
        }
        //Mã 500 là lỗi internal server
        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
    }

    public ResponseDTO show() {//truyền tham số active vào để show list voucher dựa vào trạng thái
        //Tạo 1 list voucher mới chưa có dữ liệu
        List<WarehouseReceiveItem> warehouseReceiveItems = warehouseReceiveItemRepository.findAll();


//        Bắt đầu từ đoạn này chủ yếu để cấu hình cho json trả về theo dạng nào
        List<ResponseDataDTO> warehouseReceiveItemsDTOs = new ArrayList<>();

        // Lập qua vòng for này để map dữ liệu voucher vào voucherDTO
        for (WarehouseReceiveItem warehouseReceiveItem : warehouseReceiveItems) {
            WarehouseReceiveItemDTO warehouseReceiveItemDTO = new WarehouseReceiveItemDTO();
            FlavorDTO flavorDTO = modelMapper.map(warehouseReceiveItem.getFlavor(), FlavorDTO.class);
            warehouseReceiveItemDTO.setFlavorDTO(flavorDTO);
            modelMapper.map(warehouseReceiveItem, warehouseReceiveItemDTO);
            warehouseReceiveItemsDTOs.add(warehouseReceiveItemDTO);
        }
        //Tạo 1 đối tượng respones list data mới
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();

        /*Trong đối tượng response list data sẽ có thuộc tính "datas"
        -> nên ta set cho nó bằng list voucherDtos vừa có lúc nãy
         */
        reponseListDataDTO.setDatas(warehouseReceiveItemsDTOs);

        /*
        Trong đối tượng response util chứa method success.
        Tại sao lại truyền responseListDataDTO vào?
         */
        return ResponseUtils.success(200, "Danh sách nguyên liệu trong kho", reponseListDataDTO);
    }

    public ResponseDTO showByFlavorId(int flavorId) {//truyền tham số active vào để show list voucher dựa vào trạng thái
        //Tạo 1 list voucher mới chưa có dữ liệu
        List<WarehouseReceiveItem> warehouseReceiveItems = warehouseReceiveItemRepository.findAllByFlavorId(flavorId).orElse(null);


//        Bắt đầu từ đoạn này chủ yếu để cấu hình cho json trả về theo dạng nào
        List<ResponseDataDTO> warehouseReceiveItemsDTOs = new ArrayList<>();

        // Lập qua vòng for này để map dữ liệu voucher vào voucherDTO
        for (WarehouseReceiveItem warehouseReceiveItem : warehouseReceiveItems) {
            WarehouseReceiveItemDTO warehouseReceiveItemDTO = new WarehouseReceiveItemDTO();
            FlavorDTO flavorDTO = modelMapper.map(warehouseReceiveItem.getFlavor(), FlavorDTO.class);
            warehouseReceiveItemDTO.setFlavorDTO(flavorDTO);
            modelMapper.map(warehouseReceiveItem, warehouseReceiveItemDTO);
            warehouseReceiveItemsDTOs.add(warehouseReceiveItemDTO);
        }
        //Tạo 1 đối tượng respones list data mới
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();

        /*Trong đối tượng response list data sẽ có thuộc tính "datas"
        -> nên ta set cho nó bằng list voucherDtos vừa có lúc nãy
         */
        reponseListDataDTO.setDatas(warehouseReceiveItemsDTOs);

        /*
        Trong đối tượng response util chứa method success.
        Tại sao lại truyền responseListDataDTO vào?
         */
        return ResponseUtils.success(200, "Danh sách nguyên liệu trong kho", reponseListDataDTO);
    }

    public int countByFlavorId(int flavorId) {
        int counts = warehouseReceiveItemRepository.countByFlavorId(flavorId);
        return counts;
    }
}
