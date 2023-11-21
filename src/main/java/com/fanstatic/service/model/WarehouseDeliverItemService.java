package com.fanstatic.service.model;

import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.WarehouseDeliverItem.WarehouseDeliverItemRequestDTO;
import com.fanstatic.model.Flavor;
import com.fanstatic.model.WarehouseDeliver;
import com.fanstatic.model.WarehouseDeliverItem;
import com.fanstatic.repository.FlavorRepository;
import com.fanstatic.repository.WarehouseDeliverItemRepository;
import com.fanstatic.repository.WarehouseDeliverRepository;
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
public class WarehouseDeliverItemService {
    private final SystemService systemService;

    private final ModelMapper modelMapper;
    private final FlavorRepository flavorRepository;
    private final WarehouseDeliverRepository warehouseDeliverRepository;
    private final WarehouseDeliverItemRepository warehouseDeliverItemRepository;

    public ResponseDTO create(WarehouseDeliverItemRequestDTO warehouseDeliverItemRequestDTO) {
        /*
        Tạo 1 đối tượng supplier dựa trên supplierId được truyền vào warehouseReceiveRequestDTO
         */
        Flavor flavor = flavorRepository.findByIdAndActiveIsTrue(warehouseDeliverItemRequestDTO.getFlavorId()).orElse(null);
        WarehouseDeliver warehouseDeliver = warehouseDeliverRepository.findByIdAndActiveIsTrue(warehouseDeliverItemRequestDTO.getWarehouseDeliverItemId()).orElse(null);

        List<FieldError> errors = new ArrayList<>();
        //Bắt lỗi danh mục không tồn tại -> cái này không câần in ra cho user, dev coi được rồi
        if (flavor == null) {
            errors.add(new FieldError("warehouseDeliverItemRequestDTO", "flavorId", "Nguyên liệu không tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        WarehouseDeliverItem warehouseDeliverItem = modelMapper.map(warehouseDeliverItemRequestDTO, WarehouseDeliverItem.class);
        warehouseDeliverItem.setWarehouseDeliver(warehouseDeliver);
        warehouseDeliverItem.setFlavor(flavor);
        warehouseDeliverItem.setCreateAt(new Date());
        warehouseDeliverItem.setCreateBy(systemService.getUserLogin());
        //save and flush dung cho việc save vào database và lấy dữ liệu lên ngay lập tức để xử lý
        WarehouseDeliverItem warehouseDeliverItemSaved = warehouseDeliverItemRepository.saveAndFlush(warehouseDeliverItem);

        if (warehouseDeliverItemSaved != null) {
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
}
