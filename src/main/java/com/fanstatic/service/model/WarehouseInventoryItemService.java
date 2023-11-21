package com.fanstatic.service.model;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.warehouseInventoryItem.WarehouseInventoryItemRequestDTO;
import com.fanstatic.model.Flavor;
import com.fanstatic.model.WarehouseInventory;
import com.fanstatic.model.WarehouseInventoryItem;
import com.fanstatic.repository.FlavorRepository;
import com.fanstatic.repository.WarehouseInventoryItemRepository;
import com.fanstatic.repository.WarehouseInventoryRepository;
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
public class WarehouseInventoryItemService {

    private final WarehouseInventoryRepository warehouseInventoryRepository;

    private final ModelMapper modelMapper;

    private final SystemService systemService;
    private final FlavorRepository flavorRepository;
    private final WarehouseInventoryItemRepository warehouseInventoryItemRepository;

    public ResponseDTO create(WarehouseInventoryItemRequestDTO warehouseInventoryItemRequestDTO) {
        Flavor flavor = flavorRepository.findByIdAndActiveIsTrue(warehouseInventoryItemRequestDTO.getFlavorId()).orElse(null);
        WarehouseInventory warehouseInventory = warehouseInventoryRepository.findByIdAndActiveIsTrue(warehouseInventoryItemRequestDTO.getWarehouseInventoryId()).orElse(null);

        //Tạo 1 list để lưu trữ lỗi, khi thông báo lỗi thì truyền nó lên -> mục đích để thông báo lỗi 1 lượt
        List<FieldError> errors = new ArrayList<>();
        //Bắt lỗi không tồn tại
        if (flavor == null) {
            errors.add(new FieldError("warehouseInventoryItemRequestDTO", "flavorId", "Nguyên liệu không tồn tại"));
        }
        if (warehouseInventory == null) {
            errors.add(new FieldError("warehouseInventoryItemRequestDTO", "warehouseInventoryId", "Phiếu kiểm kê không tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        WarehouseInventoryItem warehouseInventoryItem = modelMapper.map(warehouseInventoryItemRequestDTO, WarehouseInventoryItem.class);
        warehouseInventoryItem.setFlavor(flavor);
        warehouseInventoryItem.setWarehouseInventory(warehouseInventory);
        warehouseInventoryItem.setActive(DataConst.ACTIVE_TRUE);
        warehouseInventoryItem.setCreateAt(new Date());
        warehouseInventoryItem.setCreateBy(systemService.getUserLogin());
        //save and flush dung cho việc save vào database và lấy dữ liệu lên ngay lập tức để xử lý
        WarehouseInventoryItem warehouseInventoryItemSaved = warehouseInventoryItemRepository.saveAndFlush(warehouseInventoryItem);

//        if (warehouseDeliverSaved != null && warehouseDeliverRequestDTO.getWarehouseDeliverItemRequestDTOList().size() != 0) {
//            for (WarehouseDeliverItemRequestDTO warehouseDeliverItemRequestDTOSaved : warehouseDeliverRequestDTO.getWarehouseDeliverItemRequestDTOList()) {
//                warehouseDeliverItemRequestDTOSaved.setWarehouseDeliverItemId(warehouseDeliverSaved.getId());
//                warehouseDeliverItemService.create(warehouseDeliverItemRequestDTOSaved);
//            }
//        }

        if (warehouseInventoryItemSaved != null) {
            //Lưu thông tin vào lịch sử hệ thống
//            systemService.writeSystemLog(warehouseInventoryItemSaved.getId(), warehouseInventoryItemSaved.getSolution(), null);
            /*
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
             */
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
        }
        //Mã 500 là lỗi internal server
        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
    }

}
