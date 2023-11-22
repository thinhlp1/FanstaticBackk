package com.fanstatic.service.model;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.ImageConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.supplier.SupplierDTO;
<<<<<<< HEAD
import com.fanstatic.dto.model.warehouseReceiver.WarehouseReceiveDTO;
import com.fanstatic.dto.model.warehouseReceiver.WarehouseReceiveRequestDTO;
=======
import com.fanstatic.dto.model.warehouseReceive.WarehouseReceiveDTO;
import com.fanstatic.dto.model.warehouseReceive.WarehouseReceiveRequestDTO;
import com.fanstatic.dto.model.warehouseReceiveItem.WarehouseReceiveItemDTO;
import com.fanstatic.dto.model.warehouseReceiveItem.WarehouseReceiveItemRequestDTO;
>>>>>>> parent of 901909f ([API] DONE API warehouse deliver)
import com.fanstatic.model.File;
import com.fanstatic.model.Supplier;
import com.fanstatic.model.WarehouseReceive;
import com.fanstatic.repository.SupplierRepository;
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

    private final SystemService systemService;

    public ResponseDTO create(WarehouseReceiveRequestDTO warehouseReceiveRequestDTO) {
        /*
        Tạo 1 đối tượng supplier dựa trên supplierId được truyền vào warehouseReceiveRequestDTO
         */
        Supplier supplier = supplierRepository.findByIdAndActiveIsTrue(warehouseReceiveRequestDTO.getSupplierId()).orElse(null);
//        File fileImage = fileRepository.findByIdAndActiveIsTrue(extraPortionRequestDTO.getImageId()).orNull();

        //Tạo 1 list để lưu trữ lỗi, khi thông báo lỗi thì truyền nó lên -> mục đích để thông báo lỗi 1 lượt
        List<FieldError> errors = new ArrayList<>();
        //Bắt lỗi danh mục không tồn tại
        if (supplier == null) {
            errors.add(new FieldError("warehouseReceiveRequestDTO", "supplierId", "Nhà cung cấp không tồn tại"));
        }

        if (warehouseReceiveRequestDTO.getImageFile().isEmpty() || warehouseReceiveRequestDTO.getImageFile() == null) {
            errors.add(new FieldError("warehouseReceiveRequestDTO", "imageFile", "File không tồn tại"));
        }
//        //Bắt lỗi file ảnh không tồn tại
//        if (fileImage == null) {
//            errors.add(new FieldError("extraPortionRequestDTO", "imageId", "File ảnh không tồn tại"));
//        }

        //Bắt lỗi extra portion Code đã tồn tại
        if (warehouseReceiveRepository.findByDescriptionAndActiveIsTrue(warehouseReceiveRequestDTO.getDescription()).isPresent()) {
            errors.add(new FieldError("warehouseReceiveRequestDTO", "description", "Diễn giải đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        //Map voucher request dto vào voucher
        WarehouseReceive warehouseReceive = modelMapper.map(warehouseReceiveRequestDTO, WarehouseReceive.class);
        /*
        Khi thêm vô thì set:
         image là 1 file
         category là 1 đối tượng category
         active là true,
         create at là ngày mới nhất,
         create by là lấy từ lịch sử truy cập người dùng
         */
//        extraPortion.setImageFile(fileImage);

        MultipartFile image = warehouseReceiveRequestDTO.getImageFile();
        if (image != null) {
            File file = fileService.upload(image, ImageConst.EXTRA_PORTION_FOLDER);
            warehouseReceive.setImageFile(file);
            // save image to Fisebase and file table
        }
        warehouseReceive.setSupplier(supplier);
        warehouseReceive.setActive(DataConst.ACTIVE_TRUE);
        warehouseReceive.setCreateAt(new Date());
        warehouseReceive.setCreateBy(systemService.getUserLogin());
        //save and flush dung cho việc save vào database và lấy dữ liệu lên ngay lập tức để xử lý
        WarehouseReceive warehouseReceiveSaved = warehouseReceiveRepository.saveAndFlush(warehouseReceive);

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

    public ResponseDTO update(WarehouseReceiveRequestDTO warehouseReceiveRequestDTO) {
        //Chúng ta đã có id của extra portion -> tìm extra portion bằng id đó
        WarehouseReceive warehouseReceive = warehouseReceiveRepository.findByIdAndActiveIsTrue(warehouseReceiveRequestDTO.getId()).orElse(null);
        Supplier supplier = supplierRepository.findByIdAndActiveIsTrue(warehouseReceiveRequestDTO.getSupplierId()).orElse(null);
//        File fileImage = fileRepository.findByIdAndActiveIsTrue(extraPortionRequestDTO.getImageId()).orNull();
        //Nếu warehouseReceive không tồn tại thì nó sẽ lỗi 401
        if (warehouseReceive == null) {
            return ResponseUtils.fail(401, "Phiếu nhập hàng không tồn tại", null);
        }

<<<<<<< HEAD
        //Tạo 1 list để lưu trữ lỗi, khi thông báo lỗi thì truyền nó lên -> mục đích để thông báo lỗi 1 lượt
        List<FieldError> errors = new ArrayList<>();

        //Bắt lỗi extra portion Code đã tồn tại
        if (warehouseReceiveRepository.findByDescriptionAndActiveIsTrue(warehouseReceiveRequestDTO.getDescription()).isPresent()) {
            errors.add(new FieldError("warehouseReceiveRequestDTO", "description", "Diễn giải đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        modelMapper.map(warehouseReceiveRequestDTO, warehouseReceive);
        warehouseReceive.setSupplier(supplier);
        warehouseReceive.setActive(DataConst.ACTIVE_TRUE);
        warehouseReceive.setUpdateAt(new Date());
        warehouseReceive.setUpdateBy(systemService.getUserLogin());
        WarehouseReceive warehouseReceiveSaved = warehouseReceiveRepository.save(warehouseReceive);

        if (warehouseReceiveSaved != null) {
            systemService.writeSystemLog(warehouseReceiveSaved.getId(), warehouseReceiveSaved.getDescription(), null);
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
            return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);
    }

    public ResponseDTO updateImage(int id, MultipartFile imageFile) {
        WarehouseReceive warehouseReceive = warehouseReceiveRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (warehouseReceive == null) {
            return ResponseUtils.fail(404, "Phiếu nhập hàng không tồn tại", null);
        }
        // check image
        if (imageFile != null) {
            if (warehouseReceive.getImageFile() != null) {

                fileService.deleteFireStore(warehouseReceive.getImageFile().getName());
                fileService.updateFile(imageFile, ImageConst.EXTRA_PORTION_FOLDER, warehouseReceive.getImageFile());
            } else {
                File file = fileService.upload(imageFile, ImageConst.EXTRA_PORTION_FOLDER);
                warehouseReceive.setImageFile(file);
            }
            System.out.println(warehouseReceive.getImageFile());
            WarehouseReceive warehouseReceiveSaved = warehouseReceiveRepository.save(warehouseReceive);
            if (warehouseReceiveSaved != null) {
                systemService.writeSystemLog(warehouseReceiveSaved.getId(), warehouseReceiveSaved.getDescription(), null);
                return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);
            } else {
                return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);
            }
        }
        return ResponseUtils.fail(200, "UploadImage", null);
    }

=======
>>>>>>> parent of 901909f ([API] DONE API warehouse deliver)
    public ResponseDTO delete(int id) {
        WarehouseReceive warehouseReceive = warehouseReceiveRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (warehouseReceive == null) {
            return ResponseUtils.fail(401, "Phiếu nhập hàng không tồn tại", null);
        }

        /*
        XÓA MỀM -> nên sau khi đổi active thành false sẽ save lại đối tượng giống như update
         */
        warehouseReceive.setActive(DataConst.ACTIVE_FALSE);
        warehouseReceive.setDeleteAt(new Date());
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
        //Tạo 1 list voucher dto chưa có dữ liệu
        List<ResponseDataDTO> warehouseReceiveDTOs = new ArrayList<>();

        //Lập qua vòng for này để map dữ liệu voucher vào voucherDTO
        for (WarehouseReceive warehouseReceive : warehouseReceives) {
            WarehouseReceiveDTO warehouseReceiveDTO = new WarehouseReceiveDTO();
            warehouseReceiveDTO.setImageFileUrl(warehouseReceive.getImageFile().getLink());
            SupplierDTO supplierDTO = modelMapper.map(warehouseReceive.getSupplier(), SupplierDTO.class);
            warehouseReceiveDTO.setSupplierDTO(supplierDTO);
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
}
