package com.fanstatic.service.model;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.ImageConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.category.CategoryDTO;
import com.fanstatic.dto.model.extraportion.ExtraPortionDTO;
import com.fanstatic.dto.model.extraportion.ExtraPortionRequestDTO;
import com.fanstatic.model.Category;
import com.fanstatic.model.ExtraPortion;
import com.fanstatic.model.File;
import com.fanstatic.repository.CategoryRepository;
import com.fanstatic.repository.ExtraPortionRepository;
import com.fanstatic.repository.FileRepository;
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
public class ExtraPortionService {
    private final ExtraPortionRepository extraPortionRepository;
    private final CategoryRepository categoryRepository;

    private final FileRepository fileRepository;

    private final ModelMapper modelMapper;

    private final SystemService systemService;
    private final FileService fileService;

    public ResponseDTO create(ExtraPortionRequestDTO extraPortionRequestDTO) {
        /*
        Tạo 1 đối tượng Category dựa trên categoryId được truyền vào ExtraPortionRequestDTO
        Tạo 1 đối tượng Image dựa trên imageId được truyền vào ExtraPortionRequestDTO
         */
        Category category = categoryRepository.findByIdAndActiveIsTrue(extraPortionRequestDTO.getCategoryId()).orElse(null);
//        File fileImage = fileRepository.findByIdAndActiveIsTrue(extraPortionRequestDTO.getImageId()).orNull();

        //Tạo 1 list để lưu trữ lỗi, khi thông báo lỗi thì truyền nó lên -> mục đích để thông báo lỗi 1 lượt
        List<FieldError> errors = new ArrayList<>();
        //Bắt lỗi danh mục không tồn tại
        if (category == null) {
            errors.add(new FieldError("extraPortionRequestDTO", "categoryId", "Danh mục không tồn tại"));
        }

        if (extraPortionRequestDTO.getImageFile().isEmpty() || extraPortionRequestDTO.getImageFile() == null) {
            errors.add(new FieldError("extraPortionRequestDTO", "imageFile", "File không tồn tại"));
        }
//        //Bắt lỗi file ảnh không tồn tại
//        if (fileImage == null) {
//            errors.add(new FieldError("extraPortionRequestDTO", "imageId", "File ảnh không tồn tại"));
//        }

        //Bắt lỗi extra portion Code đã tồn tại
        if (extraPortionRepository.findByCodeAndActiveIsTrue(extraPortionRequestDTO.getCode()).isPresent()) {
            errors.add(new FieldError("extraPortionRequestDTO", "code", "Code đã tồn tại"));
        }

        //Bắt lỗi tên extra portion đã tồn tại
        if (extraPortionRepository.findByNameAndActiveIsTrue(extraPortionRequestDTO.getName()).isPresent()) {
            errors.add(new FieldError("extraPortionRequestDTO", "name", "Món thêm đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        //Map voucher request dto vào voucher
        ExtraPortion extraPortion = modelMapper.map(extraPortionRequestDTO, ExtraPortion.class);
        /*
        Khi thêm vô thì set:
         image là 1 file
         category là 1 đối tượng category
         active là true,
         create at là ngày mới nhất,
         create by là lấy từ lịch sử truy cập người dùng
         */
//        extraPortion.setImageFile(fileImage);

        MultipartFile image = extraPortionRequestDTO.getImageFile();
        if (image != null) {
            File file = fileService.upload(image, ImageConst.EXTRA_PORTION_FOLDER);
            extraPortion.setImageFile(file);
            // save image to Fisebase and file table
        }
        extraPortion.setCategory(category);
        extraPortion.setActive(DataConst.ACTIVE_TRUE);
        extraPortion.setCreateAt(new Date());
        extraPortion.setCreateBy(systemService.getUserLogin());
        //save and flush dung cho việc save vào database và lấy dữ liệu lên ngay lập tức để xử lý
        ExtraPortion extraPortionSaved = extraPortionRepository.saveAndFlush(extraPortion);

        if (extraPortionSaved != null) {
            //Lưu thông tin vào lịch sử hệ thống
            systemService.writeSystemLog(extraPortionSaved.getExtraPortionId(), extraPortionSaved.getName(), null);
            /*
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
             */
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
        }
        //Mã 500 là lỗi internal server
        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
    }

    public ResponseDTO update(ExtraPortionRequestDTO extraPortionRequestDTO) {
        //Chúng ta đã có id của extra portion -> tìm extra portion bằng id đó
        ExtraPortion extraPortion = extraPortionRepository.findByExtraPortionIdAndActiveIsTrue(extraPortionRequestDTO.getExtraPortionId()).orElse(null);
        /*
        Tạo 1 đối tượng Category dựa trên categoryId được truyền vào ExtraPortionRequestDTO
        Tạo 1 đối tượng Image dựa trên imageId được truyền vào ExtraPortionRequestDTO
         */
        Category category = categoryRepository.findByIdAndActiveIsTrue(extraPortionRequestDTO.getCategoryId()).orElse(null);
//        File fileImage = fileRepository.findByIdAndActiveIsTrue(extraPortionRequestDTO.getImageId()).orNull();
        //Nếu extra portion không tồn tại thì nó sẽ lỗi 401
        if (extraPortion == null) {
            return ResponseUtils.fail(401, "Món ăn không tồn tại", null);
        }

        //Tạo 1 list để lưu trữ lỗi, khi thông báo lỗi thì truyền nó lên -> mục đích để thông báo lỗi 1 lượt
        List<FieldError> errors = new ArrayList<>();
        //Bắt lỗi danh mục không tồn tại
        if (category == null) {
            errors.add(new FieldError("extraPortionRequestDTO", "categoryId", "Danh mục không tồn tại"));
        }

        //Bắt lỗi Code món ăn đã tồn tại
        if (extraPortionRepository.findByCodeAndActiveIsTrue(extraPortionRequestDTO.getCode()).isPresent()) {
            errors.add(new FieldError("extraPortionRequestDTO", "code", "Code món ăn đã tồn tại"));
        }

        //Bắt lỗi tên món ăn đã tồn tại
        if (extraPortionRepository.findByNameAndActiveIsTrue(extraPortionRequestDTO.getName()).isPresent()) {
            errors.add(new FieldError("extraPortionRequestDTO", "name", "Tên món ăn đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        modelMapper.map(extraPortionRequestDTO, extraPortion);
        extraPortion.setCategory(category);
        extraPortion.setActive(DataConst.ACTIVE_TRUE);
        extraPortion.setUpdateAt(new Date());
        extraPortion.setUpdateBy(systemService.getUserLogin());
        ExtraPortion extraPortionSaved = extraPortionRepository.save(extraPortion);

        if (extraPortionSaved != null) {
            systemService.writeSystemLog(extraPortionSaved.getExtraPortionId(), extraPortionSaved.getName(), null);
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
            return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);
    }

    public ResponseDTO updateImage(int id, MultipartFile imageFile) {
        ExtraPortion extraPortion = extraPortionRepository.findByExtraPortionIdAndActiveIsTrue(id).orElse(null);
        if (extraPortion == null) {
            return ResponseUtils.fail(404, "Món ăn không tồn tại", null);
        }
        // check image
        if (imageFile != null) {
            if (extraPortion.getImageFile() != null) {

                fileService.deleteFireStore(extraPortion.getImageFile().getName());
                fileService.updateFile(imageFile, ImageConst.EXTRA_PORTION_FOLDER, extraPortion.getImageFile());
            } else {
                File file = fileService.upload(imageFile, ImageConst.EXTRA_PORTION_FOLDER);
                extraPortion.setImageFile(file);
            }
            System.out.println(extraPortion.getImageFile());
            ExtraPortion extraPortionSaved = extraPortionRepository.save(extraPortion);
            if (extraPortionSaved != null) {
                systemService.writeSystemLog(extraPortionSaved.getExtraPortionId(), extraPortionSaved.getName(), null);
                return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);
            } else {
                return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);
            }
        }
        return ResponseUtils.fail(200, "UploadImage", null);
    }


    public ResponseDTO delete(int id) {
        ExtraPortion extraPortion = extraPortionRepository.findByExtraPortionIdAndActiveIsTrue(id).orElse(null);

        if (extraPortion == null) {
            return ResponseUtils.fail(401, "Món ăn không tồn tại", null);
        }

        /*
        XÓA MỀM -> nên sau khi đổi active thành false sẽ save lại đối tượng giống như update
         */
        extraPortion.setActive(DataConst.ACTIVE_FALSE);
        extraPortion.setDeleteAt(new Date());
        extraPortion.setDeleteBy(systemService.getUserLogin());
        ExtraPortion extraPortionSaved = extraPortionRepository.save(extraPortion);

        if (extraPortionSaved != null) {
            systemService.writeSystemLog(extraPortionSaved.getExtraPortionId(), extraPortionSaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);
    }

    //
//    /*
//    Method khôi phục active đối tượng
//     */
    public ResponseDTO restore(int id) {
        ExtraPortion extraPortion = extraPortionRepository.findByExtraPortionIdAndActiveIsFalse(id).orElse(null);

        if (extraPortion == null) {
            return ResponseUtils.fail(401, "Món ăn không tồn tại", null);
        }

        extraPortion.setActive(DataConst.ACTIVE_TRUE);
        extraPortion.setUpdateAt(new Date());
        extraPortion.setUpdateBy(systemService.getUserLogin());
        ExtraPortion extraPortionSaved = extraPortionRepository.save(extraPortion);

        if (extraPortionSaved != null) {
            systemService.writeSystemLog(extraPortionSaved.getExtraPortionId(), extraPortionSaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.RESTORE_FAIL, null);
    }

    /*
    Method get dữ liệu lên
     */
    public ResponseDTO show(int active) {//truyền tham số active vào để show list voucher dựa vào trạng thái
        //Tạo 1 list voucher mới chưa có dữ liệu
        List<ExtraPortion> extraPortions = new ArrayList<>();

        //Dựa vào biến active mà sẽ cho hiển thị list voucher mong muốn
        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                extraPortions = extraPortionRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_FALSE:
                extraPortions = extraPortionRepository.findAllByActiveIsFalse().orElse(extraPortions);
                break;
            case RequestParamConst.ACTIVE_TRUE:
                extraPortions = extraPortionRepository.findAllByActiveIsTrue().orElse(extraPortions);
                break;
            default:
                extraPortions = extraPortionRepository.findAll();
                break;
        }

//        Bắt đầu từ đoạn này chủ yếu để cấu hình cho json trả về theo dạng nào
        //Tạo 1 list voucher dto chưa có dữ liệu
        List<ResponseDataDTO> extraPortionDtos = new ArrayList<>();

        //Lập qua vòng for này để map dữ liệu voucher vào voucherDTO
        for (ExtraPortion extraPortion : extraPortions) {
            ExtraPortionDTO extraPortionDTO = new ExtraPortionDTO();
            extraPortionDTO.setImageFileUrl(extraPortion.getImageFile().getLink());
            CategoryDTO categoryDTO = modelMapper.map(extraPortion.getCategory(), CategoryDTO.class);
            extraPortionDTO.setCategory(categoryDTO);
            modelMapper.map(extraPortion, extraPortionDTO);
            extraPortionDtos.add(extraPortionDTO);
        }

        //Tạo 1 đối tượng respones list data mới
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();

        /*Trong đối tượng response list data sẽ có thuộc tính "datas"
        -> nên ta set cho nó bằng list voucherDtos vừa có lúc nãy
         */
        reponseListDataDTO.setDatas(extraPortionDtos);

        /*
        Trong đối tượng response util chứa method success.
        Tại sao lại truyền responseListDataDTO vào?
         */
        return ResponseUtils.success(200, "Danh sách món ăn kèm", reponseListDataDTO);
    }

    /*
    Method xem thông tin chi tiết đối tượng
     */
    public ResponseDTO detail(int id) {
        ExtraPortion extraPortion = extraPortionRepository.findById(id).orElse(null);
        ExtraPortionDTO extraPortionDTO = modelMapper.map(extraPortion, ExtraPortionDTO.class);
        if (extraPortion.getImageFile() != null) {
            String imageUrl = extraPortion.getImageFile().getLink();
            extraPortionDTO.setImageFileUrl(imageUrl);
        }
        if (extraPortion.getCategory() != null) {
            CategoryDTO categoryDto = modelMapper.map(extraPortion.getCategory(), CategoryDTO.class);
            extraPortionDTO.setCategory(categoryDto);
        }
        return ResponseUtils.success(200, "Chi tiết món ăn kèm", extraPortionDTO);
    }
}
