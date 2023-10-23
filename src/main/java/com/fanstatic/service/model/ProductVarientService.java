package com.fanstatic.service.model;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.product.ProductVarientRequestDTO;
import com.fanstatic.model.Product;
import com.fanstatic.model.ProductVarient;
import com.fanstatic.model.Size;
import com.fanstatic.repository.ProductRepository;
import com.fanstatic.repository.ProductVarientRepository;
import com.fanstatic.repository.SizeRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductVarientService {
    private final ProductVarientRepository productVarientRepository;
    private final SizeRepository sizeRepository;
    private final PlatformTransactionManager transactionManager;
    private final ModelMapper modelMapper;
    private final SystemService systemService;

    public ResponseDTO saveProductVarient(List<ProductVarientRequestDTO> productVarientRequestDTOs, Product product) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            for (ProductVarientRequestDTO productVarientDTO : productVarientRequestDTOs) {
                // check if product has same size
                if (productVarientRepository.findByProductAndSize(product.getId(), productVarientDTO.getSize())
                        .isPresent()) {
                    transactionManager.rollback(transactionStatus);
                    return ResponseUtils.fail(500, "Product đã có size này", null);

                }

                ProductVarient productVarient = new ProductVarient();
                Size size = sizeRepository.findById(productVarientDTO.getSize()).orElse(null);

                if (size == null) {
                    transactionManager.rollback(transactionStatus);
                    return ResponseUtils.fail(404, "Size không tồn tại", null);
                }

                productVarient.setCode(product.getCode() + "_" + size.getCode());
                productVarient.setPrice(productVarientDTO.getPrice());
                productVarient.setCreateAt(new Date());
                productVarient.setCreateBy(systemService.getUserLogin());
                productVarient.setSize(size);
                productVarient.setProduct(product);

                ProductVarient productVarientSaved = productVarientRepository.save(productVarient);
                if (productVarientSaved == null) {

                    transactionManager.rollback(transactionStatus);
                    return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(transactionStatus);
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        }
        transactionManager.commit(transactionStatus);
        return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
    }

    public ResponseDTO updateProductVarient(List<ProductVarientRequestDTO> productVarientRequestDTOs, Product product) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            for (ProductVarientRequestDTO productVarientDTO : productVarientRequestDTOs) {
                // check if product has same size

                ProductVarient productVarient = productVarientRepository.findById(productVarientDTO.getId())
                        .orElse(null);
                Size size = sizeRepository.findById(productVarientDTO.getSize()).orElse(null);

                if (size == null) {
                    transactionManager.rollback(transactionStatus);
                    return ResponseUtils.fail(404, "Size không tồn tại", null);
                }

                productVarient.setPrice(productVarientDTO.getPrice());
                productVarient.setUpdateAt(new Date());
                productVarient.setUpdateBy(systemService.getUserLogin());
                productVarient.setSize(size);

                ProductVarient productVarientSaved = productVarientRepository.save(productVarient);
                if (productVarientSaved == null) {

                    transactionManager.rollback(transactionStatus);
                    return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(transactionStatus);
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        }
        transactionManager.commit(transactionStatus);
        return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
    }

}
