package com.fanstatic.service.model;


import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.flavorcategory.FlavorCategoryDTO;
import com.fanstatic.dto.model.flavorcategory.FlavorCategoryRequestDTO;
import com.fanstatic.model.FlavorCategory;
import com.fanstatic.repository.FlavorCategoryRepository;
import com.fanstatic.repository.UnitRepository;
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
public class FlavorCategoryService {
    private final UnitRepository unitRepository;
    private final FlavorCategoryRepository flavorCategoryRepository;

    private final ModelMapper modelMapper;

    private final SystemService systemService;

    public ResponseDTO create(FlavorCategoryRequestDTO flavorCategoryRequestDTO) {
        //Tạo 1 list để lưu trữ lỗi, khi thông báo lỗi thì truyền nó lên -> mục đích để thông báo lỗi 1 lượt
        List<FieldError> errors = new ArrayList<>();
        //Bắt lỗi Voucher Code đã tồn tại
        if (flavorCategoryRepository.findByCodeAndActiveIsTrue(flavorCategoryRequestDTO.getCode()).isPresent()) {
            errors.add(new FieldError("flavorCategoryRequestDTO", "code", "Code danh mục nguyên liệu đã tồn tại"));
        }

        //Bắt lỗi tên flavor category đã tồn tại
        if (flavorCategoryRepository.findByNameAndActiveIsTrue(flavorCategoryRequestDTO.getName()).isPresent()) {
            errors.add(new FieldError("flavorCategoryRequestDTO", "name", "Danh mục nguyên liệu đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        //Map voucher request dto vào voucher
        FlavorCategory flavorCategory = modelMapper.map(flavorCategoryRequestDTO, FlavorCategory.class);

        /*
        Khi thêm vô thì set:
         active là true,
         create at là ngày mới nhất,
         create by là lấy từ lịch sử truy cập người dùng
         */
        flavorCategory.setActive(DataConst.ACTIVE_TRUE);
        flavorCategory.setCreateAt(new Date());
        flavorCategory.setCreateBy(systemService.getUserLogin());

        //save and flush sẽ làm gì?
        FlavorCategory flavorCategorySaved = flavorCategoryRepository.saveAndFlush(flavorCategory);

        if (flavorCategorySaved != null) {
            //Lưu thông tin vào lịch sử hệ thống
            systemService.writeSystemLog(flavorCategorySaved.getId(), flavorCategorySaved.getName(), null);
            /*
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
             */
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
        }
        //Mã 500 là lỗi internal server
        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
    }

    public ResponseDTO update(FlavorCategoryRequestDTO flavorCategoryRequestDTO) {
        //Chúng ta đã có id của voucher -> tìm voucher bằng id đó
        FlavorCategory flavorCategory = flavorCategoryRepository.findByIdAndActiveIsTrue(flavorCategoryRequestDTO.getId()).orElse(null);

        //Nếu voucher không tồn tại thì nó sẽ lỗi 401
        if (flavorCategory == null) {
            return ResponseUtils.fail(401, "Danh mục nguyên liệu không tồn tại", null);
        }

        //Tạo 1 list để lưu trữ lỗi, khi thông báo lỗi thì truyền nó lên -> mục đích để thông báo lỗi 1 lượt
        List<FieldError> errors = new ArrayList<>();
        //Bắt lỗi Voucher Code đã tồn tại
        if (flavorCategoryRepository.findByCodeAndActiveIsTrue(flavorCategoryRequestDTO.getCode()).isPresent()) {
            errors.add(new FieldError("flavorCategoryRequestDTO", "code", "Code danh mục nguyên liệu đã tồn tại"));
        }

        //Bắt lỗi tên voucher đã tồn tại
        if (flavorCategoryRepository.findByNameAndActiveIsTrue(flavorCategoryRequestDTO.getName()).isPresent()) {
            errors.add(new FieldError("flavorCategoryRequestDTO", "name", "Danh mục nguyên liệu đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        modelMapper.map(flavorCategoryRequestDTO, flavorCategory);
        flavorCategory.setUpdateAt(new Date());
        flavorCategory.setUpdateBy(systemService.getUserLogin());
        FlavorCategory flavorCategoySaved = flavorCategoryRepository.save(flavorCategory);

        if (flavorCategoySaved != null) {
            systemService.writeSystemLog(flavorCategoySaved.getId(), flavorCategoySaved.getName(), null);
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
            return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);
    }

    public ResponseDTO delete(int id) {
        FlavorCategory flavorCategory = flavorCategoryRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (flavorCategory == null) {
            return ResponseUtils.fail(401, "Danh mục nguyên liệu không tồn tại", null);
        }

        /*
        XÓA MỀM -> nên sau khi đổi active thành false sẽ save lại đối tượng giống như update
         */
        flavorCategory.setActive(DataConst.ACTIVE_FALSE);
        flavorCategory.setDeleteAt(new Date());
        flavorCategory.setDeleteBy(systemService.getUserLogin());
        FlavorCategory flavorCategorySaved = flavorCategoryRepository.save(flavorCategory);

        if (flavorCategorySaved != null) {
            systemService.writeSystemLog(flavorCategorySaved.getId(), flavorCategorySaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);
    }

    public ResponseDTO restore(int id) {
        FlavorCategory flavorCategory = flavorCategoryRepository.findByIdAndActiveIsFalse(id).orElse(null);

        if (flavorCategory == null) {
            return ResponseUtils.fail(401, "Danh mục nguyên liệu không tồn tại", null);
        }

        flavorCategory.setActive(DataConst.ACTIVE_TRUE);
        flavorCategory.setUpdateAt(new Date());
        flavorCategory.setUpdateBy(systemService.getUserLogin());
        FlavorCategory flavorCategorySaved = flavorCategoryRepository.save(flavorCategory);

        if (flavorCategorySaved != null) {
            systemService.writeSystemLog(flavorCategorySaved.getId(), flavorCategorySaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.RESTORE_FAIL, null);
    }

    public ResponseDTO detail(int id) {
        FlavorCategory flavorCategory = flavorCategoryRepository.findById(id).orElse(null);
        if (flavorCategory == null) {
            return ResponseUtils.fail(401, "Danh mục nguyên liệu không tồn tại", null);
        }
        FlavorCategoryDTO flavorCategoryDTO = modelMapper.map(flavorCategory, FlavorCategoryDTO.class);
        return ResponseUtils.success(200, "Chi tiết danh mục nguyên liệu", flavorCategoryDTO);
    }

    public ResponseDTO show(int active) {//truyền tham số active vào để show list voucher dựa vào trạng thái
        //Tạo 1 list voucher mới chưa có dữ liệu
        List<FlavorCategory> flavorCategories = new ArrayList<>();

        //Dựa vào biến active mà sẽ cho hiển thị list voucher mong muốn
        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                flavorCategories = flavorCategoryRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_FALSE:
                flavorCategories = flavorCategoryRepository.findAllByActiveIsFalse().orElse(flavorCategories);
                break;
            case RequestParamConst.ACTIVE_TRUE:
                flavorCategories = flavorCategoryRepository.findAllByActiveIsTrue().orElse(flavorCategories);
                break;
            default:
                flavorCategories = flavorCategoryRepository.findAll();
                break;
        }
//        Bắt đầu từ đoạn này chủ yếu để cấu hình cho json trả về theo dạng nào
        //Tạo 1 list voucher dto chưa có dữ liệu
        List<ResponseDataDTO> flavorCategoryDTOS = new ArrayList<>();

        //Lập qua vòng for này để map dữ liệu voucher vào voucherDTO
        for (FlavorCategory flavorCategory : flavorCategories) {
            FlavorCategoryDTO flavorCategoryDTO = new FlavorCategoryDTO();
            modelMapper.map(flavorCategory, flavorCategoryDTO);
            flavorCategoryDTOS.add(flavorCategoryDTO);
        }

        //Tạo 1 đối tượng respones list data mới
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();

        /*Trong đối tượng response list data sẽ có thuộc tính "datas"
        -> nên ta set cho nó bằng list voucherDtos vừa có lúc nãy
         */
        reponseListDataDTO.setDatas(flavorCategoryDTOS);

        /*
        Trong đối tượng response util chứa method success.
        Tại sao lại truyền responseListDataDTO vào?
         */
        return ResponseUtils.success(200, "Danh sách kho nguyên liệu", reponseListDataDTO);
    }
}
