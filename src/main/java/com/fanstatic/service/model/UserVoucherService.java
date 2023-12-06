package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
// import com.fanstatic.dto.model.voucher.VoucherCountCollected;
import com.fanstatic.dto.model.voucher.VoucherDTO;
import com.fanstatic.model.Voucher;
import com.fanstatic.repository.UserVoucherRepository;
import com.fanstatic.repository.VoucherRepository;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

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
