package com.fanstatic.service.model;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.voucher.VoucherDTO;
import com.fanstatic.repository.UserVoucherRepository;
import com.fanstatic.repository.VoucherRepository;
import com.fanstatic.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserVoucherService {
    private final UserVoucherRepository userVoucherRepository;
    private final VoucherRepository voucherRepository;
    private final ModelMapper modelMapper;

    public ResponseDTO getListVoucher() {

        List<Object[]> vouchers = userVoucherRepository.findValidVouchersWithCount(new Date());
        List<ResponseDataDTO> voucherDTOs = new ArrayList<>();
        for (Object[] voucher : vouchers) {
            VoucherDTO voucherDTO = modelMapper.map(voucher[0], VoucherDTO.class);
            voucherDTO.setQuantityCollected((Long) voucher[1]);
            if (voucherDTO.getQuantityCollected() >= voucherDTO.getQuantity()) {
                continue;
            }

            voucherDTOs.add(voucherDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(voucherDTOs);

        return ResponseUtils.success(200, "Danh sách voucher", reponseListDataDTO);
    }


}
