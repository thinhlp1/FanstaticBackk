package com.fanstatic.service.model;

import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.warehouse.warehouseRequestDto;
import com.fanstatic.model.Warehouse;
import com.fanstatic.repository.WarehouseRepository;
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
public class WarehouseService {

    private final ModelMapper modelMapper;

    private final SystemService systemService;
    private final WarehouseRepository warehouseRepository;

    public ResponseDTO create(warehouseRequestDto warehouseRequestDto) {


        //Tạo 1 list để lưu trữ lỗi, khi thông báo lỗi thì truyền nó lên -> mục đích để thông báo lỗi 1 lượt
        List<FieldError> errors = new ArrayList<>();


        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        Warehouse warehouse = modelMapper.map(warehouseRequestDto, Warehouse.class);

        warehouse.setCreateAt(new Date());
        warehouse.setCreateBy(systemService.getUserLogin());
        //save and flush dung cho việc save vào database và lấy dữ liệu lên ngay lập tức để xử lý
        Warehouse warehouseSaved = warehouseRepository.saveAndFlush(warehouse);

        if (warehouseSaved != null) {
            //Lưu thông tin vào lịch sử hệ thống
            systemService.writeSystemLog(warehouseSaved.getId(), warehouseSaved.getFlavorName(), null);
            /*
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
             */
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
        }
        //Mã 500 là lỗi internal server
        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
    }
}
