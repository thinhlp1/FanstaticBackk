package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.ImageConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.category.CategoryCompactDTO;
import com.fanstatic.dto.model.combo.ComboProductDTO;
import com.fanstatic.dto.model.combo.ComboProductDetailDTO;
import com.fanstatic.dto.model.combo.ComboProductDetailRequestDTO;
import com.fanstatic.dto.model.combo.ComboProductRequestDTO;
import com.fanstatic.dto.model.saleevent.SaleEventDTO;
import com.fanstatic.model.Category;
import com.fanstatic.model.ComboProduct;
import com.fanstatic.model.ComboProductDetail;
import com.fanstatic.model.ExtraPortion;
import com.fanstatic.model.File;
import com.fanstatic.model.Product;
import com.fanstatic.model.ProductVarient;
import com.fanstatic.model.SaleEvent;
import com.fanstatic.repository.CategoryRepository;
import com.fanstatic.repository.ComboProductDetailsRepository;
import com.fanstatic.repository.ComboProductRepository;
import com.fanstatic.repository.ExtraPortionRepository;
import com.fanstatic.repository.ProductRepository;
import com.fanstatic.repository.ProductVarientRepository;
import com.fanstatic.repository.SaleProductRepository;
import com.fanstatic.service.system.FileService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComboProductService {
    private final ComboProductRepository comboProductRepository;

    private final CategoryRepository categoryRepository;
    private final PlatformTransactionManager transactionManager;
    private final ProductRepository productRepository;
    private final ProductVarientRepository productVarientRepository;
    private final ExtraPortionRepository extraPortionRepository;
    private final ComboProductDetailsRepository comboProductDetailsRepository;
    private final SaleProductRepository saleProductRepository;

    private final ModelMapper modelMapper;
    private final SystemService systemService;
    private final FileService fileService;

    public ResponseDTO create(ComboProductRequestDTO comboProductRequestDTO) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        ComboProduct comboProduct = new ComboProduct();
        List<FieldError> errors = new ArrayList<>();
        if (comboProductRepository.findByCodeAndActiveIsTrue(comboProductRequestDTO.getCode()).isPresent()) {
            errors.add(new FieldError("comboProductRequestDTO", "code", "Code đã tồn tại"));
        }

        if (comboProductRequestDTO.getImage() == null) {
            errors.add(new FieldError("productRequestDTO", "image", "Vui lòng chọn ảnh"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        comboProduct = modelMapper.map(comboProductRequestDTO, ComboProduct.class);

        Category category = categoryRepository.findByIdAndActiveIsTrue(comboProductRequestDTO.getCategoryId())
                .orElse(null);
        if (category != null) {
            comboProduct.setCategory(category);
        } else {
            return ResponseUtils.fail(500, "Danh mục không tồn tại", null);
        }

        // valid product varient dto

        comboProduct.setActive(DataConst.ACTIVE_TRUE);
        comboProduct.setCreateAt(new Date());
        comboProduct.setCreateBy(systemService.getUserLogin());

        if (comboProductRequestDTO.getDescription() != null) {
            File file = fileService.upload(comboProductRequestDTO.getDescription(), ImageConst.COMBO_PRODUCT_FOLDER);
            comboProduct.setDescription(file);

        }

        if (comboProductRequestDTO.getImage() != null) {
            File file = fileService.upload(comboProductRequestDTO.getImage(), ImageConst.COMBO_PRODUCT_FOLDER);
            comboProduct.setImage(file);

        }

        ComboProduct comboProductSaved = comboProductRepository.saveAndFlush(comboProduct);

        if (comboProductSaved != null) {
            ResponseDTO comboProductDetail = saveComboProductDetails(comboProductSaved,
                    comboProductRequestDTO.getComboProductDetails());
            if (!comboProductDetail.isSuccess()) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(comboProductDetail.getStatusCode(), comboProductDetail.getMessage(),
                        null);
            }

            systemService.writeSystemLog(comboProductSaved.getId(), comboProductSaved.getName(), null);
            transactionManager.commit(transactionStatus);
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);

        }
        transactionManager.rollback(transactionStatus);

        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);

    }

    public ResponseDTO saveComboProductDetails(ComboProduct comboProduct,
            List<ComboProductDetailRequestDTO> comboProductDetailDTOs) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        // save product category

        try {
            List<ComboProductDetail> comboProductDetails = new ArrayList<>();
            for (ComboProductDetailRequestDTO comboProductDetailDTO : comboProductDetailDTOs) {

                ComboProductDetail comboProductDetail = new ComboProductDetail();
                comboProductDetail.setComboProduct(comboProduct);
                comboProductDetail.setQuantity(comboProductDetailDTO.getQuantity());
                comboProductDetail.setCreateAt(new Date());
                comboProductDetail.setCreateBy(systemService.getUserLogin());
                if (comboProductDetailDTO.getProductId() != null) {
                    Product product = productRepository.findByIdAndActiveIsTrue(comboProductDetailDTO.getProductId())
                            .orElse(null);
                    if (product != null) {

                        comboProductDetail.setProduct(product);

                    } else {
                        transactionManager.rollback(transactionStatus);
                        return ResponseUtils.fail(404, "Sản phẩm không tồn tại", null);

                    }
                } else if (comboProductDetailDTO.getProductVarientId() != null) {
                    ProductVarient productVarient = productVarientRepository
                            .findByIdAndActiveIsTrue(comboProductDetailDTO.getProductVarientId())
                            .orElse(null);
                    if (productVarient == null) {
                        transactionManager.rollback(transactionStatus);
                        return ResponseUtils.fail(404, "Sản phẩm không tồn tại", null);
                    }
                    comboProductDetail.setProductVarient(productVarient);

                } else if (comboProductDetailDTO.getExtraPortionId() != null) {
                    ExtraPortion extraPortion = extraPortionRepository
                            .findByExtraPortionIdAndActiveIsTrue(comboProductDetailDTO.getExtraPortionId())
                            .orElse(null);
                    if (extraPortion == null) {
                        transactionManager.rollback(transactionStatus);
                        return ResponseUtils.fail(404, "Món thêm không tồn tại", null);
                    }
                    comboProductDetail.setExtraPortion(extraPortion);

                }
                comboProductDetails.add(comboProductDetail);
            }

            List<ComboProductDetail> comboProductDetailsSaved = comboProductDetailsRepository
                    .saveAllAndFlush(comboProductDetails);
            if (comboProductDetailsSaved == null) {
                transactionManager.commit(transactionStatus);

                return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);

            }

        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        }

        return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
    }

    public ResponseDTO update(ComboProductRequestDTO comboProductRequestDTO) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        ComboProduct comboProduct = comboProductRepository.findByIdAndActiveIsTrue(comboProductRequestDTO.getId())
                .orElse(null);
        if (comboProduct == null) {
            return ResponseUtils.fail(500, "Combo không tồn tại", null);

        }
        List<FieldError> errors = new ArrayList<>();
        if (comboProductRepository.findByCodeAndActiveIsTrue(comboProductRequestDTO.getCode()).isPresent()) {
            errors.add(new FieldError("comboProductRequestDTO", "code", "Code đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        // valid product varient dto
        modelMapper.map(comboProductRequestDTO, comboProduct);
        Category category = categoryRepository.findByIdAndActiveIsTrue(comboProductRequestDTO.getCategoryId())
                .orElse(null);
        if (category != null) {
            comboProduct.setCategory(category);
        } else {
            return ResponseUtils.fail(500, "Danh mục không tồn tại", null);
        }
        comboProduct.setUpdateAt(new Date());
        comboProduct.setUpdateBy(systemService.getUserLogin());

        ComboProduct comboProductSaved = comboProductRepository.saveAndFlush(comboProduct);

        if (comboProductSaved != null) {

            systemService.writeSystemLog(comboProductSaved.getId(), comboProductSaved.getName(), null);
            transactionManager.commit(transactionStatus);
            return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

        }
        transactionManager.rollback(transactionStatus);

        return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);

    }

    public ResponseDTO updateImage(int id, MultipartFile image) {
        ComboProduct comboProduct = comboProductRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (comboProduct == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
        }

        if (image != null) {
            if (comboProduct.getImage() != null) {
                fileService.deleteFireStore(comboProduct.getImage().getName());

                fileService.updateFile(image, ImageConst.CATEGORY_FOLDER, comboProduct.getImage());

            } else {
                File file = fileService.upload(image, ImageConst.CATEGORY_FOLDER);
                comboProduct.setImage(file);

            }
            ComboProduct comboProductSaved = comboProductRepository.save(comboProduct);
            if (comboProductSaved != null) {

                systemService.writeSystemLog(comboProductSaved.getId(), comboProductSaved.getName(), null);
                return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

            } else {
                return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);

            }

        }
        return ResponseUtils.fail(200, "Cập nhật hình ảnh ", null);
    }

    public ResponseDTO addComboProductDetails(ComboProductDetailRequestDTO comboProductDetailRequestDTO) {
        ComboProduct comboProduct = comboProductRepository
                .findByIdAndActiveIsTrue(comboProductDetailRequestDTO.getComboProductId()).orElse(null);
        if (comboProduct == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
        }

        ComboProductDetail comboProductDetail = new ComboProductDetail();
        comboProductDetail.setComboProduct(comboProduct);
        comboProductDetail.setQuantity(comboProductDetailRequestDTO.getQuantity());
        comboProductDetail.setUpdateAt(new Date());
        comboProductDetail.setUpdateBy(systemService.getUserLogin());
        if (comboProductDetailRequestDTO.getProductId() != null) {
            Product product = productRepository.findByIdAndActiveIsTrue(comboProductDetailRequestDTO.getProductId())
                    .orElse(null);
            if (product != null) {

                comboProductDetail.setProduct(product);

            } else {
                return ResponseUtils.fail(404, "Sản phẩm không tồn tại", null);

            }
        } else if (comboProductDetailRequestDTO.getProductVarientId() != null) {
            ProductVarient productVarient = productVarientRepository
                    .findByIdAndActiveIsTrue(comboProductDetailRequestDTO.getProductVarientId())
                    .orElse(null);
            if (productVarient == null) {
                return ResponseUtils.fail(404, "Sản phẩm không tồn tại", null);
            }
            comboProductDetail.setProductVarient(productVarient);

        } else if (comboProductDetailRequestDTO.getExtraPortionId() != null) {
            ExtraPortion extraPortion = extraPortionRepository
                    .findByExtraPortionIdAndActiveIsTrue(comboProductDetailRequestDTO.getExtraPortionId())
                    .orElse(null);
            if (extraPortion == null) {
                return ResponseUtils.fail(404, "Món thêm không tồn tại", null);
            }
            comboProductDetail.setExtraPortion(extraPortion);

        }

        comboProductDetailsRepository.save(comboProductDetail);
        return ResponseUtils.success(200, "Thêm thành công", null);

    }

    public ResponseDTO updateComboProductDetails(ComboProductDetailRequestDTO comboProductDetailRequestDTO) {
        ComboProduct comboProduct = comboProductRepository
                .findByIdAndActiveIsTrue(comboProductDetailRequestDTO.getComboProductId()).orElse(null);
        if (comboProduct == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
        }

        ComboProductDetail comboProductDetail = comboProductDetailsRepository
                .findById(comboProductDetailRequestDTO.getId()).orElse(null);

        if (comboProductDetail == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);

        }
        comboProductDetail.setQuantity(comboProductDetailRequestDTO.getQuantity());
        comboProductDetail.setUpdateAt(new Date());
        comboProductDetail.setUpdateBy(systemService.getUserLogin());

        comboProductDetailsRepository.save(comboProductDetail);
        return ResponseUtils.success(200, "Cập nhật thành công", null);

    }

    public ResponseDTO removeComboProductDetails(ComboProductDetailRequestDTO comboProductDetailRequestDTO) {
        ComboProduct comboProduct = comboProductRepository
                .findByIdAndActiveIsTrue(comboProductDetailRequestDTO.getComboProductId()).orElse(null);
        if (comboProduct == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
        }

        ComboProductDetail comboProductDetail = comboProductDetailsRepository
                .findById(comboProductDetailRequestDTO.getId()).orElse(null);

        if (comboProductDetail == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);

        }
        comboProductDetail.setUpdateAt(new Date());
        comboProductDetail.setUpdateBy(systemService.getUserLogin());
        comboProductDetailsRepository.delete(comboProductDetail);
        return ResponseUtils.success(200, "Xóa thành công", null);

    }

    public ResponseDTO updateDescription(int id, MultipartFile description) {
        ComboProduct comboProduct = comboProductRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (comboProduct == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
        }

        if (description != null) {
            if (comboProduct.getDescription() != null) {
                fileService.deleteFireStore(comboProduct.getDescription().getName());

                fileService.updateFile(description, ImageConst.CATEGORY_FOLDER, comboProduct.getImage());

            } else {
                File file = fileService.upload(description, ImageConst.CATEGORY_FOLDER);
                comboProduct.setDescription(file);

            }
            ComboProduct comboProductSaved = comboProductRepository.save(comboProduct);
            if (comboProductSaved != null) {

                systemService.writeSystemLog(comboProductSaved.getId(), comboProductSaved.getName(), null);
                return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

            } else {
                return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);

            }

        }
        return ResponseUtils.success(200, "Cập nhật mô tả thành công", null);
    }

    public ResponseDTO delete(int id) {
        ComboProduct comboProduct = comboProductRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (comboProduct == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
        }

        comboProduct.setActive(DataConst.ACTIVE_FALSE);
        comboProduct.setDeleteAt(new Date());
        comboProduct.setDeleteBy(systemService.getUserLogin());

        ComboProduct comboProductSaved = comboProductRepository.save(comboProduct);

        if (comboProductSaved != null) {

            // delete all varient comboProduct

            systemService.writeSystemLog(comboProduct.getId(), comboProduct.getName(), null);

            return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);

        }
        return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);

    }

    public ResponseDTO restore(int id) {
        ComboProduct comboProduct = comboProductRepository.findByIdAndActiveIsFalse(id).orElse(null);

        if (comboProduct == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
        }

        comboProduct.setActive(DataConst.ACTIVE_TRUE);
        comboProduct.setUpdateAt(new Date());
        comboProduct.setUpdateBy(systemService.getUserLogin());

        ComboProduct comboProductSaved = comboProductRepository.save(comboProduct);

        if (comboProductSaved != null) {

            // delete all varient comboProduct

            systemService.writeSystemLog(comboProduct.getId(), comboProduct.getName(), null);

            return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);

        }
        return ResponseUtils.fail(500, MessageConst.RESTORE_FAIL, null);

    }

    public ResponseDTO detail(int id) {
        ComboProduct comboProduct = comboProductRepository.findById(id).orElse(null);
        if (comboProduct == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
        }

        ComboProductDTO comboProductDTO = new ComboProductDTO();
        comboProductDTO.setCode(comboProduct.getCode());
        comboProductDTO.setId(comboProduct.getId());
        comboProductDTO.setName(comboProduct.getName());
        comboProductDTO.setPrice(comboProduct.getPrice());
        comboProductDTO.setActive(comboProduct.getActive() == 1 ? true : false);
        comboProductDTO.setOutOfStock(comboProduct.getOutOfStock() == 1? true : false);
        comboProductDTO.setSoldQuantity(comboProductRepository.countSoldQuantityByProductId(comboProduct.getId()));

        SaleEvent saleEvent = saleProductRepository.findSaleByComboId(comboProduct.getId()).orElse(null);
        if (saleEvent != null) {
            comboProductDTO.setSaleEvent(modelMapper.map(saleEvent, SaleEventDTO.class));
        }

        CategoryCompactDTO categoryDTO = modelMapper.map(comboProduct.getCategory(), CategoryCompactDTO.class);
        comboProductDTO.setCategory(categoryDTO);

        File image = comboProduct.getImage();
        if (image != null) {
            comboProductDTO.setImageUrl(image.getLink());
        }

        File description = comboProduct.getDescription();
        if (description != null) {
            comboProductDTO.setDescriptionUrl(description.getLink());
        }

        List<ComboProductDetailDTO> comboProductDetailDTOs = new ArrayList<>();
        for (ComboProductDetail comboProductDetail : comboProduct.getComboProductDetails()) {
            ComboProductDetailDTO comboProductDetailDTO = modelMapper.map(comboProductDetail,
                    ComboProductDetailDTO.class);
            comboProductDetailDTOs.add(comboProductDetailDTO);

        }

        comboProductDTO.setComboProductDetails(comboProductDetailDTOs);

        return ResponseUtils.success(200, "Chi tiết sản phẩm", comboProductDTO);

    }

    public ResponseDTO show(int active) {
        List<ComboProduct> comboProducts = new ArrayList<>();

        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                comboProducts = comboProductRepository.findAllByOrderByCreateAtDesc();
                break;
            case RequestParamConst.ACTIVE_TRUE:
                comboProducts = comboProductRepository.findAllByActiveIsTrueOrderByCreateAtDesc().orElse(comboProducts);
                break;
            case RequestParamConst.ACTIVE_FALSE:
                comboProducts = comboProductRepository.findAllByActiveIsFalseOrderByCreateAtDesc().orElse(comboProducts);
                break;
            default:
                comboProducts = comboProductRepository.findAllByOrderByCreateAtDesc();
                break;
        }
        List<ResponseDataDTO> comboProductDTOs = new ArrayList<>();

        for (ComboProduct comboProduct : comboProducts) {
            ComboProductDTO comboProdutDTO = (ComboProductDTO) detail(comboProduct.getId()).getData();
            comboProductDTOs.add(comboProdutDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(comboProductDTOs);
        reponseListDataDTO.setNameList("Danh sách combo sản phẩm");
        return ResponseUtils.success(200, "Danh sách combo sản phẩm", reponseListDataDTO);
    }
}
