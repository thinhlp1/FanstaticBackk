package com.fanstatic.service.model;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.flavor.FlavorDTO;
import com.fanstatic.dto.model.flavor.FlavorRequestDTO;
import com.fanstatic.dto.model.flavorcategory.FlavorCategoryDTO;
import com.fanstatic.dto.model.unit.UnitDTO;
import com.fanstatic.model.Flavor;
import com.fanstatic.model.FlavorCategory;
import com.fanstatic.model.Unit;
import com.fanstatic.repository.FlavorCategoryRepository;
import com.fanstatic.repository.FlavorRepository;
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
public class FlavorService {
    private final UnitRepository unitRepository;
    private final FlavorRepository flavorRepository;

    private final ModelMapper modelMapper;

    private final SystemService systemService;
    private final FlavorCategoryRepository flavorCategoryRepository;

    public ResponseDTO create(FlavorRequestDTO flavorRequestDTO) {
        /*
        Tạo 1 đối tượng FlavorCategory dựa trên categoryId được truyền vào flavorRequestDTO
        Tạo 1 đối tượng Unit dựa trên unitId được truyền vào flavorRequestDTO
         */
        Unit unit = unitRepository.findByIdAndActiveIsTrue(flavorRequestDTO.getUnitId()).orElse(null);
        FlavorCategory flavorCategory = flavorCategoryRepository.findByIdAndActiveIsTrue(flavorRequestDTO.getFlavorCategoryId()).orElse(null);
//        File fileImage = fileRepository.findByIdAndActiveIsTrue(extraPortionRequestDTO.getImageId()).orNull();

        //Tạo 1 list để lưu trữ lỗi, khi thông báo lỗi thì truyền nó lên -> mục đích để thông báo lỗi 1 lượt
        List<FieldError> errors = new ArrayList<>();
        //Bắt lỗi danh mục không tồn tại

        if (unit == null) {
            errors.add(new FieldError("flavorRequestDTO", "unitId", "Đơn vị không tồn tại"));
        }
        if (flavorCategory == null) {
            errors.add(new FieldError("flavorRequestDTO", "flavorCategoryId", "Danh mục nguyên liệu không tồn tại"));
        }

        //Bắt lỗi extra portion Code đã tồn tại
        if (flavorRepository.findByCodeAndActiveIsTrue(flavorRequestDTO.getCode()).isPresent()) {
            errors.add(new FieldError("flavorRequestDTO", "code", "Code đã tồn tại"));
        }

        //Bắt lỗi tên extra portion đã tồn tại
        if (flavorRepository.findByNameAndActiveIsTrue(flavorRequestDTO.getName()).isPresent()) {
            errors.add(new FieldError("flavorRequestDTO", "name", "Nguyên vật liệu đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        //Map voucher request dto vào voucher
        Flavor flavor = modelMapper.map(flavorRequestDTO, Flavor.class);
        /*
        Khi thêm vô thì set:
         category là 1 đối tượng category,
         unit là 1 đối tượng unit,
         active là true,
         create at là ngày mới nhất,
         create by là lấy từ lịch sử truy cập người dùng
         */

        flavor.setActive(DataConst.ACTIVE_TRUE);
        flavor.setUnit(unit);
        flavor.setFlavorCategory(flavorCategory);
        flavor.setCreateAt(new Date());
        flavor.setCreateBy(systemService.getUserLogin());
        //save and flush dung cho việc save vào database và lấy dữ liệu lên ngay lập tức để xử lý
        Flavor flavorSaved = flavorRepository.saveAndFlush(flavor);

        if (flavorSaved != null) {
            //Lưu thông tin vào lịch sử hệ thống
            systemService.writeSystemLog(flavorSaved.getId(), flavorSaved.getName(), null);
            /*
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
             */
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
        }
        //Mã 500 là lỗi internal server
        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
    }

    public ResponseDTO update(FlavorRequestDTO flavorRequestDTO) {
        //Chúng ta đã có id của flavor -> tìm flavor bằng id đó
        Flavor flavor = flavorRepository.findByIdAndActiveIsTrue(flavorRequestDTO.getId()).orElse(null);
        /*
        Tạo 1 đối tượng Category dựa trên categoryId được truyền vào flavorRequestDTO
        Tạo 1 đối tượng Image dựa trên imageId được truyền vào flavorRequestDTO
         */
        Unit unit = unitRepository.findByIdAndActiveIsTrue(flavorRequestDTO.getUnitId()).orElse(null);
        FlavorCategory flavorCategory = flavorCategoryRepository.findByIdAndActiveIsTrue(flavorRequestDTO.getFlavorCategoryId()).orElse(null);

        //Nếu flavor không tồn tại thì nó sẽ lỗi 401
        if (flavor == null) {
            return ResponseUtils.fail(401, "Nguyên liệu không tồn tại", null);
        }

        //Tạo 1 list để lưu trữ lỗi, khi thông báo lỗi thì truyền nó lên -> mục đích để thông báo lỗi 1 lượt
        List<FieldError> errors = new ArrayList<>();
        //Bắt lỗi danh mục không tồn tại
        if (flavorCategory == null) {
            errors.add(new FieldError("flavorRequestDTO", "flavorCategoryId", "Danh mục nguyên liệu không tồn tại"));
        }

        if (unit == null) {
            errors.add(new FieldError("flavorRequestDTO", "unitId", "Đơn vị không tồn tại"));
        }

        //Bắt lỗi Code món ăn đã tồn tại
        if (flavorRepository.findByCodeAndActiveIsTrue(flavorRequestDTO.getCode()).isPresent()) {
            errors.add(new FieldError("extraPortionRequestDTO", "code", "Code nguyên liệu đã tồn tại"));
        }

        //Bắt lỗi tên món ăn đã tồn tại
        if (flavorRepository.findByNameAndActiveIsTrue(flavorRequestDTO.getName()).isPresent()) {
            errors.add(new FieldError("extraPortionRequestDTO", "name", "Tên nguyên liệu đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        modelMapper.map(flavorRequestDTO, flavor);
        flavor.setActive(DataConst.ACTIVE_TRUE);
        flavor.setUpdateAt(new Date());
        flavor.setUpdateBy(systemService.getUserLogin());
        Flavor flavorSaved = flavorRepository.save(flavor);

        if (flavorSaved != null) {
            systemService.writeSystemLog(flavorSaved.getId(), flavorSaved.getName(), null);
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
            return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);
    }

    public ResponseDTO delete(int id) {
        Flavor flavor = flavorRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (flavor == null) {
            return ResponseUtils.fail(401, "Nguyên liệu không tồn tại", null);
        }

        /*
        XÓA MỀM -> nên sau khi đổi active thành false sẽ save lại đối tượng giống như update
         */
        flavor.setActive(DataConst.ACTIVE_FALSE);
        flavor.setDeleteAt(new Date());
        flavor.setDeleteBy(systemService.getUserLogin());
        Flavor flavorSaved = flavorRepository.save(flavor);

        if (flavorSaved != null) {
            systemService.writeSystemLog(flavorSaved.getId(), flavorSaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);
    }

    public ResponseDTO restore(int id) {
        Flavor flavor = flavorRepository.findByIdAndActiveIsFalse(id).orElse(null);

        if (flavor == null) {
            return ResponseUtils.fail(401, "Nguyên liệu không tồn tại", null);
        }

        flavor.setActive(DataConst.ACTIVE_TRUE);
        flavor.setUpdateAt(new Date());
        flavor.setUpdateBy(systemService.getUserLogin());
        Flavor flavorSaved = flavorRepository.save(flavor);

        if (flavorSaved != null) {
            systemService.writeSystemLog(flavorSaved.getId(), flavorSaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.RESTORE_FAIL, null);
    }

    public ResponseDTO detail(int id) {
        Flavor flavor = flavorRepository.findById(id).orElse(null);
        if (flavor == null) {
            return ResponseUtils.fail(401, "Nguyên liệu không tồn tại", null);
        }

        FlavorDTO flavorDTO = modelMapper.map(flavor, FlavorDTO.class);

        if (flavor.getUnit() != null) {
            UnitDTO unitDTO = modelMapper.map(flavor.getUnit(), UnitDTO.class);
            flavorDTO.setUnitDTO(unitDTO);
        }
        if (flavor.getFlavorCategory() != null) {
            FlavorCategoryDTO flavorCategoryDTO = modelMapper.map(flavor.getFlavorCategory(), FlavorCategoryDTO.class);
            flavorDTO.setFlavorCategoryDTO(flavorCategoryDTO);
        }
        return ResponseUtils.success(200, "Chi tiết nguyên liệu", flavorDTO);
    }

    public ResponseDTO show(int active) {//truyền tham số active vào để show list voucher dựa vào trạng thái
        //Tạo 1 list voucher mới chưa có dữ liệu
        List<Flavor> flavors = new ArrayList<>();

        //Dựa vào biến active mà sẽ cho hiển thị list voucher mong muốn
        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                flavors = flavorRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_FALSE:
                flavors = flavorRepository.findAllByActiveIsFalse().orElse(flavors);
                break;
            case RequestParamConst.ACTIVE_TRUE:
                flavors = flavorRepository.findAllByActiveIsTrue().orElse(flavors);
                break;
            default:
                flavors = flavorRepository.findAll();
                break;
        }

//        Bắt đầu từ đoạn này chủ yếu để cấu hình cho json trả về theo dạng nào
        //Tạo 1 list voucher dto chưa có dữ liệu
        List<ResponseDataDTO> flavorDtos = new ArrayList<>();

        //Lập qua vòng for này để map dữ liệu flavor vào flavorDTO
        for (Flavor flavor : flavors) {
            FlavorDTO flavorDTO = new FlavorDTO();
            UnitDTO unitDTO = modelMapper.map(flavor.getUnit(), UnitDTO.class);
            FlavorCategoryDTO flavorCategoryDTO = modelMapper.map(flavor.getFlavorCategory(), FlavorCategoryDTO.class);
            flavorDTO.setUnitDTO(unitDTO);
            flavorDTO.setFlavorCategoryDTO(flavorCategoryDTO);
            modelMapper.map(flavor, flavorDTO);
            flavorDtos.add(flavorDTO);
        }

        //Tạo 1 đối tượng respones list data mới
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();

        /*Trong đối tượng response list data sẽ có thuộc tính "datas"
        -> nên ta set cho nó bằng list voucherDtos vừa có lúc nãy
         */
        reponseListDataDTO.setDatas(flavorDtos);

        /*
        Trong đối tượng response util chứa method success.
        Tại sao lại truyền responseListDataDTO vào?
         */
        return ResponseUtils.success(200, "Danh sách nguyên liệu", reponseListDataDTO);
    }
}
