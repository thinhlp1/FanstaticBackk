package com.fanstatic.service.model;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.product.ProductChangeStockDTO;
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
    private final ProductRepository productRepository;
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
                productVarient.setActive(DataConst.ACTIVE_TRUE);
                productVarient.setCreateAt(new Date());
                productVarient.setCreateBy(systemService.getUserLogin());
                productVarient.setSize(size);
                productVarient.setProduct(product);
                productVarient.setDefaulSize(
                        productVarientDTO.isDefaultSize() ? DataConst.ACTIVE_TRUE : DataConst.ACTIVE_FALSE);

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

    public ResponseDTO saveProductVarient(ProductVarientRequestDTO productVarientRequestDTO) {
        Product product = productRepository.findByIdAndActiveIsTrue(productVarientRequestDTO.getProductId())
                .orElse(null);

        if (product == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
        }
        try {

            // check if product has same size
            if (productVarientRepository.findByProductAndSize(product.getId(), productVarientRequestDTO.getSize())
                    .isPresent()) {
                return ResponseUtils.fail(500, "Product đã có size này", null);

            }

            ProductVarient productVarient = new ProductVarient();
            Size size = sizeRepository.findById(productVarientRequestDTO.getSize()).orElse(null);

            if (size == null) {
                return ResponseUtils.fail(404, "Size không tồn tại", null);
            }

            productVarient.setCode(product.getCode() + "_" + size.getCode());
            productVarient.setPrice(productVarientRequestDTO.getPrice());
            productVarient.setActive(DataConst.ACTIVE_TRUE);
            productVarient.setCreateAt(new Date());
            productVarient.setCreateBy(systemService.getUserLogin());
            productVarient.setSize(size);
            productVarient.setProduct(product);
            productVarient
                    .setDefaulSize(productVarientRequestDTO.isDefaultSize() ? DataConst.ACTIVE_TRUE : DataConst.ACTIVE_FALSE);

            ProductVarient productVarientSaved = productVarientRepository.save(productVarient);
            if (productVarientSaved == null) {

                return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);

            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        }
        return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
    }

    public ResponseDTO updateProductVarient(ProductVarientRequestDTO productVarientDTO) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {

            // check if product has same size

            ProductVarient productVarient = productVarientRepository.findByIdAndActiveIsTrue(productVarientDTO.getId())
                    .orElse(null);
            Size size = sizeRepository.findById(productVarientDTO.getSize()).orElse(null);

            if (productVarient == null) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(404, "Sản phẩm không tồn tại", null);
            }

            if (size == null) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(404, "Size không tồn tại", null);
            }

            productVarient.setPrice(productVarientDTO.getPrice());
            productVarient.setUpdateAt(new Date());
            productVarient.setUpdateBy(systemService.getUserLogin());
            productVarient.setSize(size);
              productVarient
                    .setDefaulSize(productVarientDTO.isDefaultSize() ? DataConst.ACTIVE_TRUE : DataConst.ACTIVE_FALSE);

            ProductVarient productVarientSaved = productVarientRepository.save(productVarient);
            if (productVarientSaved == null) {

                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);

            }

        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(transactionStatus);
            return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);
        }
        transactionManager.commit(transactionStatus);
        return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);
    }

    public ResponseDTO deleteProductVarient(int id) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {

            // check if product has same size

            ProductVarient productVarient = productVarientRepository.findByIdAndActiveIsTrue(id)
                    .orElse(null);

            if (productVarient == null) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(404, "Sản phẩm không tồn tại", null);
            }

            productVarient.setDeleteAt(new Date());
            productVarient.setDeleteBy(systemService.getUserLogin());
            productVarient.setActive(DataConst.ACTIVE_FALSE);

            ProductVarient productVarientSaved = productVarientRepository.save(productVarient);
            if (productVarientSaved == null) {

                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);

            }

        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(transactionStatus);
            return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);
        }
        transactionManager.commit(transactionStatus);
        return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);
    }

    public ResponseDTO restoreProductVarient(int id) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {

            // check if product has same size

            ProductVarient productVarient = productVarientRepository.findByIdAndActiveIsFalse(id)
                    .orElse(null);

            if (productVarient == null) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(404, "Sản phẩm không tồn tại", null);
            }

            productVarient.setUpdateAt(new Date());
            productVarient.setUpdateBy(systemService.getUserLogin());
            productVarient.setActive(DataConst.ACTIVE_TRUE);

            ProductVarient productVarientSaved = productVarientRepository.save(productVarient);
            if (productVarientSaved == null) {

                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);

            }

        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(transactionStatus);
            return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);
        }
        transactionManager.commit(transactionStatus);
        return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);
    }

}
