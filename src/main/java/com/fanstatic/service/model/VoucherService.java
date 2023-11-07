package com.fanstatic.service.model;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.voucher.VoucherDTO;
import com.fanstatic.dto.model.voucher.VoucherRequestDTO;
import com.fanstatic.model.Voucher;
import com.fanstatic.repository.VoucherRepository;
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
public class VoucherService {
    private final VoucherRepository voucherRepository;

    private final ModelMapper modelMapper;

    private final SystemService systemService;

    /*
    Method get dữ liệu lên
     */
    public ResponseDTO show(int active) {//truyền tham số active vào để show list voucher dựa vào trạng thái
        //Tạo 1 list voucher mới chưa có dữ liệu
        List<Voucher> vouchers = new ArrayList<>();

        //Dựa vào biến active mà sẽ cho hiển thị list voucher mong muốn
        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                vouchers = voucherRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_FALSE:
                vouchers = voucherRepository.findAllByActiveIsFalse().orElse(vouchers);
                break;
            case RequestParamConst.ACTIVE_TRUE:
                vouchers = voucherRepository.findAllByActiveIsTrue().orElse(vouchers);
                break;
            default:
                vouchers = voucherRepository.findAll();
                break;
        }
//        Bắt đầu từ đoạn này chủ yếu để cấu hình cho json trả về theo dạng nào
        //Tạo 1 list voucher dto chưa có dữ liệu
        List<ResponseDataDTO> voucherDTOS = new ArrayList<>();

        //Lập qua vòng for này để map dữ liệu voucher vào voucherDTO
        for (Voucher voucher : vouchers) {
            VoucherDTO voucherDTO = new VoucherDTO();
            modelMapper.map(voucher, voucherDTO);
            voucherDTOS.add(voucherDTO);
        }

        //Tạo 1 đối tượng respones list data mới
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();

        /*Trong đối tượng response list data sẽ có thuộc tính "datas"
        -> nên ta set cho nó bằng list voucherDtos vừa có lúc nãy
         */
        reponseListDataDTO.setDatas(voucherDTOS);

        /*
        Trong đối tượng response util chứa method success.
        Tại sao lại truyền responseListDataDTO vào?
         */
        return ResponseUtils.success(200, "Danh sách voucher", reponseListDataDTO);
    }

    /*
    Method thêm đối tượng vào
     */
    public ResponseDTO create(VoucherRequestDTO voucherRequestDTO) {
        //Tạo 1 list để lưu trữ lỗi, khi thông báo lỗi thì truyền nó lên -> mục đích để thông báo lỗi 1 lượt
        List<FieldError> errors = new ArrayList<>();
        //Bắt lỗi Voucher Code đã tồn tại
        if (voucherRepository.findByVoucherCodeAndActiveIsTrue(voucherRequestDTO.getVoucherCode()).isPresent()) {
            errors.add(new FieldError("voucherRequestDTO", "voucher_code", "Voucher Code đã tồn tại"));
        }

        //Bắt lỗi tên voucher đã tồn tại
        if (voucherRepository.findByNameAndActiveIsTrue(voucherRequestDTO.getName()).isPresent()) {
            errors.add(new FieldError("voucherRequestDTO", "name", "Voucher đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        //Map voucher request dto vào voucher
        Voucher voucher = modelMapper.map(voucherRequestDTO, Voucher.class);

        /*
        Khi thêm vô thì set:
         active là true,
         create at là ngày mới nhất,
         create by là lấy từ lịch sử truy cập người dùng
         */
        voucher.setActive(DataConst.ACTIVE_TRUE);
        voucher.setCreateAt(new Date());
        voucher.setCreateBy(systemService.getUserLogin());

        //save and flush sẽ làm gì?
        Voucher voucherSaved = voucherRepository.saveAndFlush(voucher);

        if (voucherSaved != null) {
            //Lưu thông tin vào lịch sử hệ thống
            systemService.writeSystemLog(voucherSaved.getId(), voucherSaved.getName(), null);
            /*
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
             */
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
        }
        //Mã 500 là lỗi internal server
        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
    }

    /*
    Method update đối tượng
     */
    public ResponseDTO update(VoucherRequestDTO voucherRequestDTO) {
        //Chúng ta đã có id của voucher -> tìm voucher bằng id đó
        Voucher voucher = voucherRepository.findByIdAndActiveIsTrue(voucherRequestDTO.getId()).orElse(null);

        //Nếu voucher không tồn tại thì nó sẽ lỗi 401
        if (voucher == null) {
            return ResponseUtils.fail(401, "Voucher không tồn tại", null);
        }

        //Tạo 1 list để lưu trữ lỗi, khi thông báo lỗi thì truyền nó lên -> mục đích để thông báo lỗi 1 lượt
        List<FieldError> errors = new ArrayList<>();
        //Bắt lỗi Voucher Code đã tồn tại
        if (voucherRepository.findByVoucherCodeAndActiveIsTrue(voucherRequestDTO.getVoucherCode()).isPresent()) {
            errors.add(new FieldError("voucherRequestDTO", "voucher_code", "Voucher Code đã tồn tại"));
        }

        //Bắt lỗi tên voucher đã tồn tại
        if (voucherRepository.findByNameAndActiveIsTrue(voucherRequestDTO.getName()).isPresent()) {
            errors.add(new FieldError("voucherRequestDTO", "name", "Voucher đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        modelMapper.map(voucherRequestDTO, voucher);
        voucher.setUpdateAt(new Date());
        voucher.setUpdateBy(systemService.getUserLogin());
        Voucher voucherSaved = voucherRepository.save(voucher);

        if (voucherSaved != null) {
            systemService.writeSystemLog(voucherSaved.getId(), voucherSaved.getName(), null);
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
            return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);
    }

    /*
    Method delete đối tượng
     */
    public ResponseDTO delete(int id) {
        Voucher voucher = voucherRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (voucher == null) {
            return ResponseUtils.fail(401, "Voucher không tồn tại", null);
        }

        /*
        XÓA MỀM -> nên sau khi đổi active thành false sẽ save lại đối tượng giống như update
         */
        voucher.setActive(DataConst.ACTIVE_FALSE);
        voucher.setDeleteAt(new Date());
        voucher.setDeleteBy(systemService.getUserLogin());
        Voucher voucherSaved = voucherRepository.save(voucher);

        if (voucherSaved != null) {
            systemService.writeSystemLog(voucherSaved.getId(), voucherSaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);
    }

    /*
    Method khôi phục active đối tượng
     */
    public ResponseDTO restore(int id) {
        Voucher voucher = voucherRepository.findByIdAndActiveIsFalse(id).orElse(null);

        if (voucher == null) {
            return ResponseUtils.fail(401, "Voucher không tồn tại", null);
        }

        voucher.setActive(DataConst.ACTIVE_TRUE);
        voucher.setUpdateAt(new Date());
        voucher.setUpdateBy(systemService.getUserLogin());
        Voucher voucherSaved = voucherRepository.save(voucher);

        if (voucherSaved != null) {
            systemService.writeSystemLog(voucherSaved.getId(), voucherSaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.RESTORE_FAIL, null);
    }

    /*
    Method xem thông tin chi tiết đối tượng
     */
    public ResponseDTO detail(int id) {
        Voucher voucher = voucherRepository.findById(id).orElse(null);
        if (voucher == null) {
            return ResponseUtils.fail(401, "Voucher không tồn tại", null);
        }
        VoucherDTO voucherDTO = modelMapper.map(voucher, VoucherDTO.class);
        return ResponseUtils.success(200, "Chi tiết voucher", voucherDTO);
    }
}
