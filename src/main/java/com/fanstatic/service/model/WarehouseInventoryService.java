package com.fanstatic.service.model;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.flavor.FlavorDTO;
import com.fanstatic.dto.model.flavorcategory.FlavorCategoryDTO;
import com.fanstatic.dto.model.warehouseInventory.WarehouseInventoryDTO;
import com.fanstatic.dto.model.warehouseInventory.WarehouseInventoryRequestDTO;
import com.fanstatic.dto.model.warehouseInventory.WarehouseInventoryRequestDeleteDTO;
import com.fanstatic.dto.model.warehouseInventoryItem.WarehouseInventoryItemDTO;
import com.fanstatic.dto.model.warehouseInventoryItem.WarehouseInventoryItemRequestDTO;
import com.fanstatic.model.FlavorCategory;
import com.fanstatic.model.WarehouseInventory;
import com.fanstatic.model.WarehouseInventoryItem;
import com.fanstatic.repository.FlavorCategoryRepository;
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
public class WarehouseInventoryService {

    private final WarehouseInventoryRepository warehouseInventoryRepository;

    private final WarehouseInventoryItemService warehouseInventoryItemService;
    private final FlavorCategoryRepository flavorCategoryRepository;

    private final ModelMapper modelMapper;

    private final SystemService systemService;

    public ResponseDTO create(WarehouseInventoryRequestDTO warehouseInventoryRequestDTO) {

        FlavorCategory flavorCategory = flavorCategoryRepository.findByIdAndActiveIsTrue(warehouseInventoryRequestDTO.getFlavorCategoryId()).orElse(null);

        //Tạo 1 list để lưu trữ lỗi, khi thông báo lỗi thì truyền nó lên -> mục đích để thông báo lỗi 1 lượt
        List<FieldError> errors = new ArrayList<>();
        //Bắt lỗi không tồn tại
        if (flavorCategory == null) {
            errors.add(new FieldError("warehouseInventoryRequestDTO", "flavorCategoryId", "Danh mục kho không tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        WarehouseInventory warehouseInventory = modelMapper.map(warehouseInventoryRequestDTO, WarehouseInventory.class);
        warehouseInventory.setFlavorCategory(flavorCategory);
        warehouseInventory.setActive(DataConst.ACTIVE_TRUE);
        warehouseInventory.setCreateAt(new Date());
        warehouseInventory.setCreateBy(systemService.getUserLogin());
        //save and flush dung cho việc save vào database và lấy dữ liệu lên ngay lập tức để xử lý
        WarehouseInventory warehouseInventorySaved = warehouseInventoryRepository.saveAndFlush(warehouseInventory);

        if (warehouseInventorySaved != null && warehouseInventoryRequestDTO.getWarehouseInventoryItemRequestDTOList().size() != 0) {
            for (WarehouseInventoryItemRequestDTO warehouseInventoryItemRequestDTOSaved : warehouseInventoryRequestDTO.getWarehouseInventoryItemRequestDTOList()) {
                warehouseInventoryItemRequestDTOSaved.setWarehouseInventoryId(warehouseInventorySaved.getId());
                warehouseInventoryItemService.create(warehouseInventoryItemRequestDTOSaved);
            }
        }

        if (warehouseInventorySaved != null) {
            //Lưu thông tin vào lịch sử hệ thống
            systemService.writeSystemLog(warehouseInventorySaved.getId(), warehouseInventorySaved.getCreateAt().toString(), null);
            /*
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
             */
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
        }
        //Mã 500 là lỗi internal server
        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
    }

    public ResponseDTO delete(int id, WarehouseInventoryRequestDeleteDTO warehouseInventoryRequestDeleteDTO) {

        WarehouseInventory warehouseInventory = warehouseInventoryRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (warehouseInventory == null) {
            return ResponseUtils.fail(401, "Phiếu kiểm kê kho không tồn tại", null);
        }

        /*
        XÓA MỀM -> nên sau khi đổi active thành false sẽ save lại đối tượng giống như update
         */
        warehouseInventory.setActive(DataConst.ACTIVE_FALSE);
        warehouseInventory.setDeleteAt(new Date());
        warehouseInventory.setCancelReason(warehouseInventoryRequestDeleteDTO.getCancelReason());
        warehouseInventory.setDeleteBy(systemService.getUserLogin());
        WarehouseInventory warehouseInventorySaved = warehouseInventoryRepository.save(warehouseInventory);

        if (warehouseInventorySaved != null) {
            systemService.writeSystemLog(warehouseInventorySaved.getId(), warehouseInventorySaved.getDescription(), null);
            return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);
    }

    public ResponseDTO restore(int id) {
        WarehouseInventory warehouseInventory = warehouseInventoryRepository.findByIdAndActiveIsFalse(id).orElse(null);

        if (warehouseInventory == null) {
            return ResponseUtils.fail(401, "Phiếu kiểm kê kho không tồn tại", null);
        }

        warehouseInventory.setActive(DataConst.ACTIVE_TRUE);
        warehouseInventory.setUpdateAt(new Date());
        warehouseInventory.setUpdateBy(systemService.getUserLogin());
        WarehouseInventory warehouseInventorySaved = warehouseInventoryRepository.save(warehouseInventory);

        if (warehouseInventorySaved != null) {
            systemService.writeSystemLog(warehouseInventorySaved.getId(), warehouseInventorySaved.getDescription(), null);
            return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.RESTORE_FAIL, null);
    }

    public ResponseDTO show(int active) {//truyền tham số active vào để show list voucher dựa vào trạng thái
        //Tạo 1 list voucher mới chưa có dữ liệu
        List<WarehouseInventory> warehouseInventoryList = new ArrayList<>();

        //Dựa vào biến active mà sẽ cho hiển thị list voucher mong muốn
        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                warehouseInventoryList = warehouseInventoryRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_FALSE:
                warehouseInventoryList = warehouseInventoryRepository.findAllByActiveIsFalse().orElse(warehouseInventoryList);
                break;
            case RequestParamConst.ACTIVE_TRUE:
                warehouseInventoryList = warehouseInventoryRepository.findAllByActiveIsTrue().orElse(warehouseInventoryList);
                break;
            default:
                warehouseInventoryList = warehouseInventoryRepository.findAll();
                break;
        }
//        Bắt đầu từ đoạn này chủ yếu để cấu hình cho json trả về theo dạng nào
        List<ResponseDataDTO> warehouseInventoryDTOList = new ArrayList<>();

        for (WarehouseInventory warehouseInventory : warehouseInventoryList) {
            WarehouseInventoryDTO warehouseInventoryDTO = new WarehouseInventoryDTO();
            FlavorCategoryDTO flavorCategoryDTO = modelMapper.map(warehouseInventory.getFlavorCategory(), FlavorCategoryDTO.class);
            warehouseInventoryDTO.setFlavorCategoryDTO(flavorCategoryDTO);
            List<WarehouseInventoryItemDTO> warehouseInventoryItemDTOList = new ArrayList<>();
            for (WarehouseInventoryItem warehouseInventoryItem : warehouseInventory.getWarehouseInventoryItemList()) {
                WarehouseInventoryItemDTO warehouseInventoryItemDTO = new WarehouseInventoryItemDTO();
                FlavorDTO flavorDTO = modelMapper.map(warehouseInventoryItem.getFlavor(), FlavorDTO.class);
                warehouseInventoryItemDTO.setFlavorDTO(flavorDTO);
                modelMapper.map(warehouseInventoryItem, warehouseInventoryItemDTO);
                warehouseInventoryItemDTOList.add(warehouseInventoryItemDTO);
            }
            warehouseInventoryDTO.setWarehouseInventoryItemDTOList(warehouseInventoryItemDTOList);
            modelMapper.map(warehouseInventory, warehouseInventoryDTO);
            warehouseInventoryDTOList.add(warehouseInventoryDTO);
        }

        //Tạo 1 đối tượng respones list data mới
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();

        /*Trong đối tượng response list data sẽ có thuộc tính "datas"
        -> nên ta set cho nó bằng list voucherDtos vừa có lúc nãy
         */
        reponseListDataDTO.setDatas(warehouseInventoryDTOList);

        /*
        Trong đối tượng response util chứa method success.
         */
        return ResponseUtils.success(200, "Danh sách kiểm kê kho", reponseListDataDTO);
    }

    public ResponseDTO detail(int id) {
        WarehouseInventory warehouseInventory = warehouseInventoryRepository.findById(id).orElse(null);
        WarehouseInventoryDTO warehouseInventoryDTO = modelMapper.map(warehouseInventory, WarehouseInventoryDTO.class);
        List<WarehouseInventoryItemDTO> warehouseInventoryItemDTOList = new ArrayList<>();
        for (WarehouseInventoryItem warehouseInventoryItem : warehouseInventory.getWarehouseInventoryItemList()) {
            WarehouseInventoryItemDTO warehouseInventoryItemDTO = new WarehouseInventoryItemDTO();
            FlavorDTO flavorDTO = modelMapper.map(warehouseInventoryItem.getFlavor(), FlavorDTO.class);
            warehouseInventoryItemDTO.setFlavorDTO(flavorDTO);
            modelMapper.map(warehouseInventoryItem, warehouseInventoryItemDTO);
            warehouseInventoryItemDTOList.add(warehouseInventoryItemDTO);
        }
        warehouseInventoryDTO.setWarehouseInventoryItemDTOList(warehouseInventoryItemDTOList);
        if (warehouseInventory.getFlavorCategory() != null) {
            FlavorCategoryDTO flavorCategoryDTO = modelMapper.map(warehouseInventory.getFlavorCategory(), FlavorCategoryDTO.class);
            warehouseInventoryDTO.setFlavorCategoryDTO(flavorCategoryDTO);
        }

        return ResponseUtils.success(200, "Chi tiết phiếu kiểm kê kho", warehouseInventoryDTO);
    }
}
