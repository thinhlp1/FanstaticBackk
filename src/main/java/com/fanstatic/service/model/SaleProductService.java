package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.saleEventProduct.SaleProductDTO;
import com.fanstatic.dto.model.saleEventProduct.SaleProductRequestDTO;
import com.fanstatic.dto.model.supplier.SupplierDTO;
import com.fanstatic.model.SaleProduct;
import com.fanstatic.model.Supplier;
import com.fanstatic.repository.SaleProductRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleProductService {
    private final SaleProductRepository saleProductRepository;
    private final ModelMapper modelMapper;
    private final SystemService systemService;

    //  public ResponseDTO create(SaleProductRequestDTO saleProductRequestDTO) {


    //     SaleProduct saleProduct = modelMapper.map(saleProductRequestDTO, SaleProduct.class);

    //     saleProduct.setActive(DataConst.ACTIVE_TRUE);
    //     saleProduct.setCreateAt(new Date());
    //     saleProduct.setCreateBy(systemService.getUserLogin());

    //     SaleProduct saleProductSaved = saleProductRepository.saveAndFlush(saleProduct);

    //     systemService.writeSystemLog(saleProductSaved.getId(), null, null);
    //     return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);

    // }

    // public ResponseDTO update(SaleProductRequestDTO saleProductRequestDTO) {

    //     SaleProduct saleProduct = saleProductRepository.findByIdAndActiveIsTrue(saleProductRequestDTO.getId()).orElse(null);

    //     if (saleProduct == null) {
    //         return ResponseUtils.fail(401, "Sự kiện không tồn tại", null);
    //     }

    //     modelMapper.map(saleProductRequestDTO, saleProduct);

    //     saleProduct.setUpdateAt(new Date());
    //     saleProduct.setUpdateBy(systemService.getUserLogin());
    //     saleProduct.setActive(DataConst.ACTIVE_TRUE);
    //     SaleProduct saleProductSaved = saleProductRepository.save(saleProduct);

    //     systemService.writeSystemLog(saleProductSaved.getId(),null, null);

    //     return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

    // }

    // public ResponseDTO delete(int id) {

    //     SaleProduct saleProduct = saleProductRepository.findByIdAndActiveIsTrue(id).orElse(null);

    //     if (saleProduct == null) {
    //         return ResponseUtils.fail(401, "Sự kiện không tồn tại", null);
    //     }

    //     saleProduct.setActive(DataConst.ACTIVE_FALSE);
    //     saleProduct.setDeleteAt(new Date());
    //     saleProduct.setDeleteBy(systemService.getUserLogin());

    //     SaleProduct saleProductSaved = saleProductRepository.save(saleProduct);

    //     systemService.writeSystemLog(saleProductSaved.getId(), null, null);
    //     return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);

    // }

    // public ResponseDTO restore(int id) {

    //     SaleProduct saleProduct = saleProductRepository.findByIdAndActiveIsFalse(id).orElse(null);

    //     if (saleProduct == null) {
    //         return ResponseUtils.fail(401, "Sự kiện không tồn tại", null);
    //     }

    //     saleProduct.setActive(DataConst.ACTIVE_TRUE);
    //     saleProduct.setUpdateAt(new Date());
    //     saleProduct.setUpdateBy(systemService.getUserLogin());

    //     SaleProduct saleProductSaved = saleProductRepository.save(saleProduct);

    //     systemService.writeSystemLog(saleProductSaved.getId(), null, null);

    //     return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);

    // }

    // public ResponseDTO detail(int id) {

    //     SaleProduct saleProduct = saleProductRepository.findById(id).orElse(null);

    //     if (saleProduct == null) {
    //         return ResponseUtils.fail(401, "SaleEvent không tồn tại", null);
    //     }

    //     SaleProductDTO saleProductDTO = modelMapper.map(saleProduct, SaleProductDTO.class);

    //     return ResponseUtils.success(200, "Chi tiết sự kiện.", saleProductDTO);

    // }

      public ResponseDTO show(int active) {
        List<SaleProduct> saleProducts = new ArrayList<>();

        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                saleProducts = saleProductRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_TRUE:
                saleProducts = saleProductRepository.findAllByActiveIsTrue().orElse(saleProducts);
                break;
            case RequestParamConst.ACTIVE_FALSE:
                saleProducts = saleProductRepository.findAllByActiveIsFalse().orElse(saleProducts);
                break;
            default:
                saleProducts = saleProductRepository.findAll();
                break;
        }
        List<ResponseDataDTO> saleProductDTOS = new ArrayList<>();

        for (SaleProduct saleProduct : saleProducts) {
            SaleProductDTO saleProductDTO = new SaleProductDTO();
            modelMapper.map(saleProduct, saleProductDTO);

            saleProductDTOS.add(saleProductDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(saleProductDTOS);
        return ResponseUtils.success(200, "Danh sách sản phẩm sale", reponseListDataDTO);
    }
}
