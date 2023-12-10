package com.fanstatic.service.model;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.WarehouseDeliverItem.WarehouseDeliverItemDTO;
import com.fanstatic.dto.model.WarehouseDeliverItem.WarehouseDeliverItemRequestDTO;
import com.fanstatic.dto.model.WarehouseDeliverReason.WarehouseDeliverReasonDTO;
import com.fanstatic.dto.model.flavor.FlavorDTO;
import com.fanstatic.dto.model.user.UserDTO;
import com.fanstatic.dto.model.warehouseDeliver.WarehouseDeliverDTO;
import com.fanstatic.dto.model.warehouseDeliver.WarehouseDeliverRequestDTO;
import com.fanstatic.dto.model.warehouseDeliver.WarehouseDeliverRequestDeleteDTO;
import com.fanstatic.dto.model.warehouseDeliverSolution.WarehouseDeliverSolutionDTO;
import com.fanstatic.model.*;
import com.fanstatic.repository.WarehouseDeliverReasonRepository;
import com.fanstatic.repository.WarehouseDeliverRepository;
import com.fanstatic.repository.WarehouseDeliverSolutionRepository;
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
public class WarehouseDeliverService {
    private final SystemService systemService;
    private final WarehouseDeliverRepository warehouseDeliverRepository;
    private final WarehouseDeliverReasonRepository warehouseDeliverReasonRepository;
    private final WarehouseDeliverSolutionRepository warehouseDeliverSolutionRepository;
    private final ModelMapper modelMapper;

    private final WarehouseDeliverItemService warehouseDeliverItemService;

    public ResponseDTO create(WarehouseDeliverRequestDTO warehouseDeliverRequestDTO) {
        User employee = systemService.getUserLogin(); //lấy user đang đăng nhập
        WarehouseDeliverReason warehouseDeliverReason = warehouseDeliverReasonRepository.findByIdAndActiveIsTrue(warehouseDeliverRequestDTO.getReasonId()).orElse(null);
        WarehouseDeliverSolution warehouseDeliverSolution = warehouseDeliverSolutionRepository.findByIdAndActiveIsTrue(warehouseDeliverRequestDTO.getSolutionId()).orElse(null);

        //Tạo 1 list để lưu trữ lỗi, khi thông báo lỗi thì truyền nó lên -> mục đích để thông báo lỗi 1 lượt
        List<FieldError> errors = new ArrayList<>();
        //Bắt lỗi không tồn tại
        if (employee == null) {
            errors.add(new FieldError("warehouseDeliverRequestDTO", "employeeId", "Người dùng không tồn tại"));
        }
        if (warehouseDeliverReason == null) {
            errors.add(new FieldError("warehouseDeliverRequestDTO", "reasonId", "Lý do xuất kho không tồn tại"));
        }
        if (warehouseDeliverSolution == null) {
            errors.add(new FieldError("warehouseDeliverRequestDTO", "solutionId", "Giải pháp xuất kho không tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        WarehouseDeliver warehouseDeliver = modelMapper.map(warehouseDeliverRequestDTO, WarehouseDeliver.class);

        warehouseDeliver.setEmployee(employee);
        warehouseDeliver.setWarehouseDeliverReason(warehouseDeliverReason);
        warehouseDeliver.setWarehouseDeliverSolution(warehouseDeliverSolution);
        warehouseDeliver.setActive(DataConst.ACTIVE_TRUE);
        warehouseDeliver.setCreateAt(new Date());
        warehouseDeliver.setCreateBy(employee);
        //save and flush dung cho việc save vào database và lấy dữ liệu lên ngay lập tức để xử lý
        WarehouseDeliver warehouseDeliverSaved = warehouseDeliverRepository.saveAndFlush(warehouseDeliver);
        System.out.println(warehouseDeliverSaved.getId());
        if (warehouseDeliverSaved != null && warehouseDeliverRequestDTO.getWarehouseDeliverItemRequestDTOList().size() != 0) {
            for (WarehouseDeliverItemRequestDTO warehouseDeliverItemRequestDTOSaved : warehouseDeliverRequestDTO.getWarehouseDeliverItemRequestDTOList()) {
                warehouseDeliverItemRequestDTOSaved.setWarehouseDeliverItemId(warehouseDeliverSaved.getId());
                warehouseDeliverItemService.create(warehouseDeliverItemRequestDTOSaved);
            }
        }

        if (warehouseDeliverSaved != null) {
            //Lưu thông tin vào lịch sử hệ thống
            systemService.writeSystemLog(warehouseDeliverSaved.getId(), warehouseDeliverSaved.getDescription(), null);
            /*
            //Mã 200 là thêm thành công (Khi success thì không truyền data nào để return cho json cả mà chỉ thông báo thành công)
             */
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
        }
        //Mã 500 là lỗi internal server
        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
    }

    public ResponseDTO delete(int id, WarehouseDeliverRequestDeleteDTO warehouseDeliverRequestDeleteDTO) {

        WarehouseDeliver warehouseDeliver = warehouseDeliverRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (warehouseDeliver == null) {
            return ResponseUtils.fail(401, "Phiếu xuất kho không tồn tại", null);
        }

        /*
        XÓA MỀM -> nên sau khi đổi active thành false sẽ save lại đối tượng giống như update
         */
        warehouseDeliver.setActive(DataConst.ACTIVE_FALSE);
        warehouseDeliver.setDeleteAt(new Date());
        warehouseDeliver.setCancelReason(warehouseDeliverRequestDeleteDTO.getCancelReason());
        warehouseDeliver.setDeleteBy(systemService.getUserLogin());
        WarehouseDeliver warehouseDeliverSaved = warehouseDeliverRepository.save(warehouseDeliver);

        if (warehouseDeliverSaved != null) {
            systemService.writeSystemLog(warehouseDeliverSaved.getId(), warehouseDeliverSaved.getDescription(), null);
            return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);
    }

    public ResponseDTO restore(int id) {
        WarehouseDeliver warehouseDeliver = warehouseDeliverRepository.findByIdAndActiveIsFalse(id).orElse(null);

        if (warehouseDeliver == null) {
            return ResponseUtils.fail(401, "Phiếu xuất kho không tồn tại", null);
        }

        warehouseDeliver.setActive(DataConst.ACTIVE_TRUE);
        warehouseDeliver.setUpdateAt(new Date());
        warehouseDeliver.setUpdateBy(systemService.getUserLogin());
        WarehouseDeliver warehouseDeliverSaved = warehouseDeliverRepository.save(warehouseDeliver);

        if (warehouseDeliverSaved != null) {
            systemService.writeSystemLog(warehouseDeliverSaved.getId(), warehouseDeliverSaved.getDescription(), null);
            return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);
        }
        return ResponseUtils.fail(500, MessageConst.RESTORE_FAIL, null);
    }

    public ResponseDTO show(int active) {//truyền tham số active vào để show list voucher dựa vào trạng thái
        //Tạo 1 list voucher mới chưa có dữ liệu
        List<WarehouseDeliver> warehouseDeliverList = new ArrayList<>();

        //Dựa vào biến active mà sẽ cho hiển thị list voucher mong muốn
        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                warehouseDeliverList = warehouseDeliverRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_FALSE:
                warehouseDeliverList = warehouseDeliverRepository.findAllByActiveIsFalse().orElse(warehouseDeliverList);
                break;
            case RequestParamConst.ACTIVE_TRUE:
                warehouseDeliverList = warehouseDeliverRepository.findAllByActiveIsTrue().orElse(warehouseDeliverList);
                break;
            default:
                warehouseDeliverList = warehouseDeliverRepository.findAll();
                break;
        }
//        Bắt đầu từ đoạn này chủ yếu để cấu hình cho json trả về theo dạng nào
        List<ResponseDataDTO> warehouseDeliverDtos = new ArrayList<>();

        for (WarehouseDeliver warehouseDeliver : warehouseDeliverList) {
            WarehouseDeliverDTO warehouseDeliverDTO = new WarehouseDeliverDTO();
            UserDTO employeeDTO = modelMapper.map(warehouseDeliver.getEmployee(), UserDTO.class);
            WarehouseDeliverReasonDTO warehouseDeliverReasonDTO = modelMapper.map(warehouseDeliver.getWarehouseDeliverReason(), WarehouseDeliverReasonDTO.class);
            WarehouseDeliverSolutionDTO warehouseDeliverSolutionDTO = modelMapper.map(warehouseDeliver.getWarehouseDeliverSolution(), WarehouseDeliverSolutionDTO.class);
            warehouseDeliverDTO.setEmployeeDTO(employeeDTO);
            warehouseDeliverDTO.setWarehouseDeliverReasonDTO(warehouseDeliverReasonDTO);
            warehouseDeliverDTO.setWarehouseDeliverSolutionDTO(warehouseDeliverSolutionDTO);
            List<WarehouseDeliverItemDTO> warehouseDeliverItemDTOList = new ArrayList<>();
            for (WarehouseDeliverItem warehouseDeliverItem : warehouseDeliver.getWarehouseDeliverItems()) {
                WarehouseDeliverItemDTO warehouseDeliverItemDTO = new WarehouseDeliverItemDTO();
                FlavorDTO flavorDTO = modelMapper.map(warehouseDeliverItem.getFlavor(), FlavorDTO.class);
                warehouseDeliverItemDTO.setFlavorDTO(flavorDTO);
                modelMapper.map(warehouseDeliverItem, warehouseDeliverItemDTO);
                warehouseDeliverItemDTOList.add(warehouseDeliverItemDTO);
            }
            warehouseDeliverDTO.setWarehouseDeliverItemDTOList(warehouseDeliverItemDTOList);
            modelMapper.map(warehouseDeliver, warehouseDeliverDTO);
            warehouseDeliverDtos.add(warehouseDeliverDTO);
        }

        //Tạo 1 đối tượng respones list data mới
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();

        /*Trong đối tượng response list data sẽ có thuộc tính "datas"
        -> nên ta set cho nó bằng list voucherDtos vừa có lúc nãy
         */
        reponseListDataDTO.setDatas(warehouseDeliverDtos);

        /*
        Trong đối tượng response util chứa method success.
        Tại sao lại truyền responseListDataDTO vào?
         */
        return ResponseUtils.success(200, "Danh sách xuất kho", reponseListDataDTO);
    }

    public ResponseDTO detail(int id) {
        WarehouseDeliver warehouseDeliver = warehouseDeliverRepository.findById(id).orElse(null);
        WarehouseDeliverDTO warehouseDeliverDTO = modelMapper.map(warehouseDeliver, WarehouseDeliverDTO.class);
        List<WarehouseDeliverItemDTO> warehouseDeliverItemDTOList = new ArrayList<>();
        for (WarehouseDeliverItem warehouseDeliverItem : warehouseDeliver.getWarehouseDeliverItems()) {
            WarehouseDeliverItemDTO warehouseDeliverItemDTO = new WarehouseDeliverItemDTO();
            FlavorDTO flavorDTO = modelMapper.map(warehouseDeliverItem.getFlavor(), FlavorDTO.class);
            warehouseDeliverItemDTO.setFlavorDTO(flavorDTO);
            modelMapper.map(warehouseDeliverItem, warehouseDeliverItemDTO);
            warehouseDeliverItemDTOList.add(warehouseDeliverItemDTO);
        }
        warehouseDeliverDTO.setWarehouseDeliverItemDTOList(warehouseDeliverItemDTOList);
        if (warehouseDeliver.getWarehouseDeliverSolution() != null) {
            WarehouseDeliverSolutionDTO warehouseDeliverSolutionDTO = modelMapper.map(warehouseDeliver.getWarehouseDeliverSolution(), WarehouseDeliverSolutionDTO.class);
            warehouseDeliverDTO.setWarehouseDeliverSolutionDTO(warehouseDeliverSolutionDTO);
        }
        if (warehouseDeliver.getWarehouseDeliverReason() != null) {
            WarehouseDeliverReasonDTO warehouseDeliverReasonDTO = modelMapper.map(warehouseDeliver.getWarehouseDeliverReason(), WarehouseDeliverReasonDTO.class);
            warehouseDeliverDTO.setWarehouseDeliverReasonDTO(warehouseDeliverReasonDTO);
        }
        if (warehouseDeliver.getEmployee() != null) {
            UserDTO employeeDTO = modelMapper.map(warehouseDeliver.getEmployee(), UserDTO.class);
            warehouseDeliverDTO.setEmployeeDTO(employeeDTO);
        }
        return ResponseUtils.success(200, "Chi tiết phiếu nhập hàng", warehouseDeliverDTO);
    }

    public ResponseDTO showReason(int active) {//truyền tham số active vào để show list voucher dựa vào trạng thái
        //Tạo 1 list voucher mới chưa có dữ liệu
        List<WarehouseDeliverReason> warehouseDeliverReasonList = new ArrayList<>();

        //Dựa vào biến active mà sẽ cho hiển thị list voucher mong muốn
        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                warehouseDeliverReasonList = warehouseDeliverReasonRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_FALSE:
                warehouseDeliverReasonList = warehouseDeliverReasonRepository.findAllByActiveIsFalse().orElse(warehouseDeliverReasonList);
                break;
            case RequestParamConst.ACTIVE_TRUE:
                warehouseDeliverReasonList = warehouseDeliverReasonRepository.findAllByActiveIsTrue().orElse(warehouseDeliverReasonList);
                break;
            default:
                warehouseDeliverReasonList = warehouseDeliverReasonRepository.findAll();
                break;
        }
//        Bắt đầu từ đoạn này chủ yếu để cấu hình cho json trả về theo dạng nào
        //Tạo 1 list voucher dto chưa có dữ liệu
        List<ResponseDataDTO> warehouseDeliverReasonDTOList = new ArrayList<>();

        //Lập qua vòng for này để map dữ liệu voucher vào voucherDTO
        for (WarehouseDeliverReason warehouseDeliverReason : warehouseDeliverReasonList) {
            WarehouseDeliverReasonDTO warehouseDeliverReasonDTO = new WarehouseDeliverReasonDTO();
            modelMapper.map(warehouseDeliverReason, warehouseDeliverReasonDTO);
            warehouseDeliverReasonDTOList.add(warehouseDeliverReasonDTO);
        }

        //Tạo 1 đối tượng respones list data mới
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();

        /*Trong đối tượng response list data sẽ có thuộc tính "datas"
        -> nên ta set cho nó bằng list voucherDtos vừa có lúc nãy
         */
        reponseListDataDTO.setDatas(warehouseDeliverReasonDTOList);

        /*
        Trong đối tượng response util chứa method success.
        Tại sao lại truyền responseListDataDTO vào?
         */
        return ResponseUtils.success(200, "Danh sách lý do xuất kho", reponseListDataDTO);
    }

    public ResponseDTO showSolution(int active) {//truyền tham số active vào để show list voucher dựa vào trạng thái
        //Tạo 1 list voucher mới chưa có dữ liệu
        List<WarehouseDeliverSolution> warehouseDeliverSolutionList = new ArrayList<>();

        //Dựa vào biến active mà sẽ cho hiển thị list voucher mong muốn
        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                warehouseDeliverSolutionList = warehouseDeliverSolutionRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_FALSE:
                warehouseDeliverSolutionList = warehouseDeliverSolutionRepository.findAllByActiveIsFalse().orElse(warehouseDeliverSolutionList);
                break;
            case RequestParamConst.ACTIVE_TRUE:
                warehouseDeliverSolutionList = warehouseDeliverSolutionRepository.findAllByActiveIsTrue().orElse(warehouseDeliverSolutionList);
                break;
            default:
                warehouseDeliverSolutionList = warehouseDeliverSolutionRepository.findAll();
                break;
        }
//        Bắt đầu từ đoạn này chủ yếu để cấu hình cho json trả về theo dạng nào
        //Tạo 1 list voucher dto chưa có dữ liệu
        List<ResponseDataDTO> warehouseDeliverSolutionDTOList = new ArrayList<>();

        //Lập qua vòng for này để map dữ liệu voucher vào voucherDTO
        for (WarehouseDeliverSolution warehouseDeliverSolution : warehouseDeliverSolutionList) {
            WarehouseDeliverSolutionDTO warehouseDeliverSolutionDTO = new WarehouseDeliverSolutionDTO();
            modelMapper.map(warehouseDeliverSolution, warehouseDeliverSolutionDTO);
            warehouseDeliverSolutionDTOList.add(warehouseDeliverSolutionDTO);
        }

        //Tạo 1 đối tượng respones list data mới
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();

        /*Trong đối tượng response list data sẽ có thuộc tính "datas"
        -> nên ta set cho nó bằng list voucherDtos vừa có lúc nãy
         */
        reponseListDataDTO.setDatas(warehouseDeliverSolutionDTOList);

        /*
        Trong đối tượng response util chứa method success.
        Tại sao lại truyền responseListDataDTO vào?
         */
        return ResponseUtils.success(200, "Danh sách giải pháp xuất kho", reponseListDataDTO);
    }
}
