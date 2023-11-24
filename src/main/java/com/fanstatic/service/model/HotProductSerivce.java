package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.hotproduct.HotProductDTO;
import com.fanstatic.dto.model.hotproduct.HotProductRequestDTO;
import com.fanstatic.dto.model.product.ProductDTO;
import com.fanstatic.model.ComboProduct;
import com.fanstatic.model.HotProduct;
import com.fanstatic.model.Product;
import com.fanstatic.repository.ComboProductRepository;
import com.fanstatic.repository.HotProductRepository;
import com.fanstatic.repository.ProductRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class HotProductSerivce {
    private final HotProductRepository hotProductRepository;
    private final ProductRepository productRepository;
    private final ComboProductRepository comboProductRepository;
    private final ModelMapper modelMapper;
    private final SystemService systemService;
    private final ProductService productService;
    private final PlatformTransactionManager transactionManager;

    public ResponseDTO addHotProduct(List<HotProductRequestDTO> hotProductRequestDTOs) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        List<HotProduct> hotProducts = new ArrayList<>();
        for (HotProductRequestDTO hotProductRequestDTO : hotProductRequestDTOs) {
            HotProduct hotProduct = new HotProduct();
            hotProduct.setCreateAt(new Date());
            hotProduct.setCreateBy(systemService.getUserLogin());
            if (hotProductRequestDTO.getProductId() != null) {
                Product product = productRepository.findByIdAndActiveIsTrue(hotProductRequestDTO.getProductId())
                        .orElse(null);
                if (product == null) {
                    transactionManager.rollback(transactionStatus);
                    return ResponseUtils.fail(404, "Sản phẩm không tồn tại", null);
                }

                if (!hotProductRepository.findByProduct(product).isPresent()) {
                    hotProduct.setProduct(product);

                }

            } else if (hotProductRequestDTO.getComboProductId() != null) {
                ComboProduct comboProduct = comboProductRepository
                        .findByIdAndActiveIsTrue(hotProductRequestDTO.getComboProductId())
                        .orElse(null);
                if (comboProduct == null) {
                    transactionManager.rollback(transactionStatus);
                    return ResponseUtils.fail(404, "Sản phẩm không tồn tại", null);
                }
                if (!hotProductRepository.findByComboProduct(comboProduct).isPresent()) {
                    hotProduct.setComboProduct(comboProduct);

                }
            }

            hotProducts.add(hotProduct);
        }

        try {
            List<HotProduct> hotProductsSave = hotProductRepository.saveAllAndFlush(hotProducts);
            if (hotProductsSave != null) {
                transactionManager.commit(transactionStatus);
                return ResponseUtils.success(200, "Thêm thành công", null);

            }
            transactionManager.rollback(transactionStatus);
            return ResponseUtils.fail(404, "Thêm thất bại", null);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            return ResponseUtils.fail(404, "Thêm thất bại", null);
        }

    }

    public ResponseDTO removeHotProduct(List<Integer> hotProductRequestDTO) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        for (Integer id : hotProductRequestDTO) {
            HotProduct hotProduct = hotProductRepository.findById(id).orElse(null);
            if (hotProduct == null) {
                transactionManager.rollback(transactionStatus);

                return ResponseUtils.fail(404, "Sản phẩm không tồn tại", null);
            }

            hotProductRepository.delete(hotProduct);
        }
        transactionManager.commit(transactionStatus);

        return ResponseUtils.success(200, "Xóa thành công", null);

    }

    public ResponseDTO show() {
        List<HotProduct> hotProducts = hotProductRepository.findAllByOrderByCreateAtDesc();

        List<ResponseDataDTO> productDTOS = new ArrayList<>();

        for (HotProduct hotProduct : hotProducts) {
            HotProductDTO hotProductDTO = modelMapper.map(hotProduct, HotProductDTO.class);
            productDTOS.add(hotProductDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(productDTOS);
        reponseListDataDTO.setNameList("Danh sách hot product");
        return ResponseUtils.success(200, "Danh sách hot product", reponseListDataDTO);
    }

    public ResponseDTO showOnOrder() {
        List<HotProduct> hotProducts = hotProductRepository.findAllByOrderByCreateAtDesc();

        List<ResponseDataDTO> productDTOS = new ArrayList<>();

        for (HotProduct hotProduct : hotProducts) {
            HotProductDTO hotProductDTO = modelMapper.map(hotProduct, HotProductDTO.class);
            // if 
            productDTOS.add(hotProductDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(productDTOS);
        reponseListDataDTO.setNameList("Danh sách hot product");
        return ResponseUtils.success(200, "Danh sách hot product", reponseListDataDTO);
    }
}
