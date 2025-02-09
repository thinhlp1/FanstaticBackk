package com.fanstatic.service.model;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.ImageConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.flavor.FlavorDTO;
import com.fanstatic.dto.model.supplier.SupplierDTO;
import com.fanstatic.dto.model.user.UserDTO;
import com.fanstatic.dto.model.warehouseReceive.WarehouseReceiveDTO;
import com.fanstatic.dto.model.warehouseReceive.WarehouseReceiveRequestDTO;
import com.fanstatic.dto.model.warehouseReceive.WarehouseReceiveRequestDeleteDTO;
import com.fanstatic.dto.model.warehouseReceiveItem.WarehouseReceiveItemDTO;
import com.fanstatic.dto.model.warehouseReceiveItem.WarehouseReceiveItemRequestDTO;
import com.fanstatic.model.*;
import com.fanstatic.repository.SupplierRepository;
import com.fanstatic.repository.UserRepository;
import com.fanstatic.repository.WarehouseReceiveRepository;
import com.fanstatic.service.system.FileService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseReceiveService {

    private final SupplierRepository supplierRepository;
    private final WarehouseReceiveRepository warehouseReceiveRepository;

    private final ModelMapper modelMapper;

    private final FileService fileService;

    private final WarehouseReceiveItemService warehouseReceiveItemService;

    private final WarehouseService warehouseService;

    private final SystemService systemService;
    private final UserRepository userRepository;

    public ResponseDTO create(WarehouseReceiveRequestDTO warehouseReceiveRequestDTO) {
        Supplier supplier = supplierRepository.findByIdAndActiveIsTrue(warehouseReceiveRequestDTO.getSupplierId()).orElse(null);

        //Tạo 1 list để lưu trữ lỗi, khi thông báo lỗi thì truyền nó lên -> mục đích để thông báo lỗi 1 lượt
        List<FieldError> errors = new ArrayList<>();
        //Bắt lỗi danh mục không tồn tại
        if (supplier == null) {
            errors.add(new FieldError("warehouseReceiveRequestDTO", "supplierId", "Nhà cung cấp không tồn tại"));
        }

        //Bắt lỗi extra portion Code đã tồn tại
        if (warehouseReceiveRepository.findByDescriptionAndActiveIsTrue(warehouseReceiveRequestDTO.getDescription()).isPresent()) {
            errors.add(new FieldError("warehouseReceiveRequestDTO", "description", "Diễn giải đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        WarehouseReceive warehouseReceive = modelMapper.map(warehouseReceiveRequestDTO, WarehouseReceive.class);
        MultipartFile imageOptional = warehouseReceiveRequestDTO.getImageFile().orElse(null);

        if (imageOptional != null) {
            File file = fileService.upload(imageOptional, ImageConst.EXTRA_PORTION_FOLDER);
            warehouseReceive.setImageFile(file);
            // Tiếp tục lưu hình ảnh vào Firebase và bảng file
            // save image to Firebase and file table
        }

        warehouseReceive.setSupplier(supplier);
        warehouseReceive.setCancelReason("");
        warehouseReceive.setActive(DataConst.ACTIVE_TRUE);
        warehouseReceive.setCreateAt(new Date());
        warehouseReceive.setCreateBy(systemService.getUserLogin());
        //save and flush dung cho việc save vào database và lấy dữ liệu lên ngay lập tức để xử lý
        WarehouseReceive warehouseReceiveSaved = warehouseReceiveRepository.saveAndFlush(warehouseReceive);
        /*
        Giả sử như đã có listWarehouseReceiveItem rồi
         */
        if (warehouseReceiveSaved != null && warehouseReceiveRequestDTO.getWarehouseReceiveItemRequestDTOList().size() != 0) {
            for (WarehouseReceiveItemRequestDTO warehouseReceiveItemRequestDTOSaved : warehouseReceiveRequestDTO.getWarehouseReceiveItemRequestDTOList()) {
                warehouseReceiveItemRequestDTOSaved.setWarehouseReceiveItemId(warehouseReceiveSaved.getId());
                warehouseReceiveItemService.create(warehouseReceiveItemRequestDTOSaved);
            }
        }
        if (warehouseReceiveSaved != null) {
            //Lưu thông tin vào lịch sử hệ thống
            systemService.writeSystemLog(warehouseReceiveSaved.getId(), warehouseReceiveSaved.getDescription(), null);
            /*
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
             */
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
        }
        //Mã 500 là lỗi internal server
        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
    }


    public ResponseDTO delete(int id, WarehouseReceiveRequestDeleteDTO warehouseReceiveRequestDeleteDTO) {

        WarehouseReceive warehouseReceive = warehouseReceiveRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (warehouseReceive == null) {
            return ResponseUtils.fail(401, "Phiếu nhập hàng không tồn tại", null);
        }

        /*
        XÓA MỀM -> nên sau khi đổi active thành false sẽ save lại đối tượng giống như update
         */
        warehouseReceive.setActive(DataConst.ACTIVE_FALSE);
        warehouseReceive.setDeleteAt(new Date());
        warehouseReceive.setCancelReason(warehouseReceiveRequestDeleteDTO.getCancelReason());
        warehouseReceive.setDeleteBy(systemService.getUserLogin());
        WarehouseReceive warehouseReceiveSaved = warehouseReceiveRepository.save(warehouseReceive);

        if (warehouseReceiveSaved != null) {
            systemService.writeSystemLog(warehouseReceiveSaved.getId(), warehouseReceiveSaved.getDescription(), null);
            return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);
    }

    public ResponseDTO restore(int id) {
        WarehouseReceive warehouseReceive = warehouseReceiveRepository.findByIdAndActiveIsFalse(id).orElse(null);

        if (warehouseReceive == null) {
            return ResponseUtils.fail(401, "Phiếu nhập hàng không tồn tại", null);
        }

        warehouseReceive.setActive(DataConst.ACTIVE_TRUE);
        warehouseReceive.setUpdateAt(new Date());
        warehouseReceive.setUpdateBy(systemService.getUserLogin());
        WarehouseReceive warehouseReceiveSaved = warehouseReceiveRepository.save(warehouseReceive);

        if (warehouseReceiveSaved != null) {
            systemService.writeSystemLog(warehouseReceiveSaved.getId(), warehouseReceiveSaved.getDescription(), null);
            return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.RESTORE_FAIL, null);
    }

    public ResponseDTO show(int active) {//truyền tham số active vào để show list voucher dựa vào trạng thái
        //Tạo 1 list voucher mới chưa có dữ liệu
        List<WarehouseReceive> warehouseReceives = new ArrayList<>();

        //Dựa vào biến active mà sẽ cho hiển thị list voucher mong muốn
        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                warehouseReceives = warehouseReceiveRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_FALSE:
                warehouseReceives = warehouseReceiveRepository.findAllByActiveIsFalse().orElse(warehouseReceives);
                break;
            case RequestParamConst.ACTIVE_TRUE:
                warehouseReceives = warehouseReceiveRepository.findAllByActiveIsTrue().orElse(warehouseReceives);
                break;
            default:
                warehouseReceives = warehouseReceiveRepository.findAll();
                break;
        }
//        Bắt đầu từ đoạn này chủ yếu để cấu hình cho json trả về theo dạng nào
        List<ResponseDataDTO> warehouseReceiveDTOs = new ArrayList<>();

        //Lập qua vòng for này để map dữ liệu warehouseReceive vào warehouseReceiveDTO
        for (WarehouseReceive warehouseReceive : warehouseReceives) {
            WarehouseReceiveDTO warehouseReceiveDTO = new WarehouseReceiveDTO();
            warehouseReceiveDTO.setImageFileUrl(warehouseReceive.getImageFile().getLink());
            SupplierDTO supplierDTO = modelMapper.map(warehouseReceive.getSupplier(), SupplierDTO.class);
            warehouseReceiveDTO.setSupplierDTO(supplierDTO);
            /*
            Trong warehouseReceiveDTO chứa warehouseReceiveItemDTO ->
             */
            List<WarehouseReceiveItemDTO> warehouseReceiveItemDTOList = new ArrayList<>();
            for (WarehouseReceiveItem warehouseReceiveItem : warehouseReceive.getWarehouseReceiveItemList()) {
                WarehouseReceiveItemDTO warehouseReceiveItemDTO = new WarehouseReceiveItemDTO();
                FlavorDTO flavorDTO = modelMapper.map(warehouseReceiveItem.getFlavor(), FlavorDTO.class);
                warehouseReceiveItemDTO.setFlavorDTO(flavorDTO);
                modelMapper.map(warehouseReceiveItem, warehouseReceiveItemDTO);
                warehouseReceiveItemDTOList.add(warehouseReceiveItemDTO);
            }
            warehouseReceiveDTO.setWarehouseReceiveItemDTOList(warehouseReceiveItemDTOList);
            modelMapper.map(warehouseReceive, warehouseReceiveDTO);
            warehouseReceiveDTOs.add(warehouseReceiveDTO);
        }

        //Tạo 1 đối tượng respones list data mới
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();

        /*Trong đối tượng response list data sẽ có thuộc tính "datas"
        -> nên ta set cho nó bằng list voucherDtos vừa có lúc nãy
         */
        reponseListDataDTO.setDatas(warehouseReceiveDTOs);

        /*
        Trong đối tượng response util chứa method success.
        Tại sao lại truyền responseListDataDTO vào?
         */
        return ResponseUtils.success(200, "Danh sách nhâp hàng", reponseListDataDTO);
    }

    public ResponseDTO detail(int id) {
        WarehouseReceive warehouseReceive = warehouseReceiveRepository.findById(id).orElse(null);
        WarehouseReceiveDTO warehouseReceiveDTO = modelMapper.map(warehouseReceive, WarehouseReceiveDTO.class);
        List<WarehouseReceiveItemDTO> warehouseReceiveItemDTOList = new ArrayList<>();
        for (WarehouseReceiveItem warehouseReceiveItem : warehouseReceive.getWarehouseReceiveItemList()) {
            WarehouseReceiveItemDTO warehouseReceiveItemDTO = new WarehouseReceiveItemDTO();
            FlavorDTO flavorDTO = modelMapper.map(warehouseReceiveItem.getFlavor(), FlavorDTO.class);
            warehouseReceiveItemDTO.setFlavorDTO(flavorDTO);
            modelMapper.map(warehouseReceiveItem, warehouseReceiveItemDTO);
            warehouseReceiveItemDTOList.add(warehouseReceiveItemDTO);
        }
        warehouseReceiveDTO.setWarehouseReceiveItemDTOList(warehouseReceiveItemDTOList);
        if (warehouseReceive.getImageFile() != null) {
            String imageUrl = warehouseReceive.getImageFile().getLink();
            warehouseReceiveDTO.setImageFileUrl(imageUrl);
        }
        if (warehouseReceive.getSupplier() != null) {
            SupplierDTO supplierDTO = modelMapper.map(warehouseReceive.getSupplier(), SupplierDTO.class);
            warehouseReceiveDTO.setSupplierDTO(supplierDTO);
        }
        return ResponseUtils.success(200, "Chi tiết phiếu nhập hàng", warehouseReceiveDTO);
    }

    public ResponseDTO getUser(Integer id) {
        User user = userRepository.findById(id).orElse(null);

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        if (user.getImage() != null) {
            String imageUrl = user.getImage().getLink();
            userDTO.setImageUrl(imageUrl);
        }

        return ResponseUtils.success(200, "Chi tiết người dùng", userDTO);
    }
}