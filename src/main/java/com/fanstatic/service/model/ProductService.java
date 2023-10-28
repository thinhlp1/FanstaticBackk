package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.category.CategoryDTO;
import com.fanstatic.dto.model.permissioin.RoleDTO;
import com.fanstatic.dto.model.product.ProductDTO;
import com.fanstatic.dto.model.product.ProductRequestDTO;
import com.fanstatic.dto.model.product.ProductVarientDTO;
import com.fanstatic.model.Category;
import com.fanstatic.model.Product;
import com.fanstatic.model.ProductCategory;
import com.fanstatic.model.ProductVarient;
import com.fanstatic.repository.CategoryRepository;
import com.fanstatic.repository.ProductCategoryRepository;
import com.fanstatic.repository.ProductRepository;
import com.fanstatic.repository.ProductVarientRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final PlatformTransactionManager transactionManager;
    private final ModelMapper modelMapper;
    private final SystemService systemService;

    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductVarientRepository productVarientRepository;

    @Autowired
    @Lazy
    private ProductVarientService productVarientService;

    public ResponseDTO create(ProductRequestDTO productRequestDTO) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        Product product = new Product();
        List<FieldError> errors = new ArrayList<>();
        if (productRepository.findByCodeAndActiveIsTrue(productRequestDTO.getCode()).isPresent()) {
            errors.add(new FieldError("productRequestDTO", "code", "Code đã tồn tại"));
        }
        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        // check category exit
        List<Category> categories = new ArrayList<>();
        for (int categoryId : productRequestDTO.getCategoriesId()) {
            Category category = categoryRepository.findByIdAndActiveIsTrue(categoryId).orElse(null);
            if (category != null) {
                categories.add(category);
            } else {
                return ResponseUtils.fail(500, "Danh mục không tồn tại", null);
            }
        }

        // valid product varient dto

        product = modelMapper.map(productRequestDTO, Product.class);

        product.setActive(DataConst.ACTIVE_TRUE);
        product.setCreateAt(new Date());
        product.setCreateBy(systemService.getUserLogin());

        Product productSaved = productRepository.saveAndFlush(product);

        if (productSaved != null) {

            // save product category
            ResponseDTO productCategorySaved = saveProductCategory(categories, productSaved);
            if (!productCategorySaved.isSuccess()) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(productCategorySaved.getStatusCode(), productCategorySaved.getMessage(),
                        null);
            }

            // save product varient
            ResponseDTO productVarientSaved = productVarientService
                    .saveProductVarient(productRequestDTO.getProductVarients(), productSaved);
            if (!productVarientSaved.isSuccess()) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(productVarientSaved.getStatusCode(),
                        productVarientSaved.getMessage(),
                        null);
            }

            systemService.writeSystemLog(product.getId(), product.getName(), null);
            transactionManager.commit(transactionStatus);
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);

        }
        transactionManager.rollback(transactionStatus);

        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);

    }

    public ResponseDTO saveProductCategory(List<Category> categories, Product product) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        // save product category
        try {

            // delete cac danh muc cu va insert lai cac danh muc moi

            productCategoryRepository.deleteByProductId(product.getId());

            for (Category category : categories) {
                ProductCategory productCategory = new ProductCategory();
                productCategory.setCategory(category);
                productCategory.setProduct(product);
                productCategoryRepository.save(productCategory);

            }
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        }
        return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
    }

    public ResponseDTO update(ProductRequestDTO productRequestDTO) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        List<FieldError> errors = new ArrayList<>();
        if (productRepository.findByCodeAndActiveIsTrueAndIdNot(productRequestDTO.getCode(), productRequestDTO.getId())
                .isPresent()) {
            errors.add(new FieldError("productRequestDTO", "code", "Code đã tồn tại"));
        }
        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        Product product = productRepository.findByIdAndActiveIsTrue(productRequestDTO.getId()).orElse(null);

        if (product == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
        }

        modelMapper.map(productRequestDTO, product);

        product.setUpdateAt(new Date());
        product.setUpdateBy(systemService.getUserLogin());

        // check category exit
        List<Category> categories = new ArrayList<>();
        for (int categoryId : productRequestDTO.getCategoriesId()) {
            Category category = categoryRepository.findByIdAndActiveIsTrue(categoryId).orElse(null);
            if (category != null) {
                categories.add(category);
            } else {
                return ResponseUtils.fail(500, "Danh mục không tồn tại", null);
            }
        }
        Product productSaved = productRepository.save(product);

        if (productSaved != null) {

            // save product category
            ResponseDTO productCategorySaved = saveProductCategory(categories, productSaved);
            if (!productCategorySaved.isSuccess()) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(productCategorySaved.getStatusCode(), productCategorySaved.getMessage(),
                        null);
            }

            // save product varient
            // ResponseDTO productVarientSaved = productVarientService
            // .updateProductVarient(productRequestDTO.getProductVarients(), productSaved);
            // if (!productVarientSaved.isSuccess()) {
            // transactionManager.rollback(transactionStatus);
            // return ResponseUtils.fail(productVarientSaved.getStatusCode(),
            // productVarientSaved.getMessage(),
            // null);
            // }

            systemService.writeSystemLog(product.getId(), product.getName(), null);
            transactionManager.commit(transactionStatus);
            return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

        }
        transactionManager.rollback(transactionStatus);

        return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);
    }

    public ResponseDTO updateImage(int id, List<MultipartFile> images) {

        // check image
        // if (image != null) {
        // String fileName = image.getOriginalFilename();
        // String contentType = image.getContentType();
        // long fileSize = image.getSize();
        // System.out.println(fileName);
        // // save image to Fisebase and file table
        // }
        return ResponseUtils.fail(200, "Uploadimage", null);

    }

    public ResponseDTO delete(int id) {
        Product product = productRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (product == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
        }

        product.setActive(DataConst.ACTIVE_FALSE);
        product.setDeleteAt(new Date());
        product.setDeleteBy(systemService.getUserLogin());

        Product productSaved = productRepository.save(product);

        if (productSaved != null) {

            // delete all varient product

            systemService.writeSystemLog(product.getId(), product.getName(), null);

            return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);

        }
        return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);

    }

    public ResponseDTO restore(int id) {
        Product product = productRepository.findByIdAndActiveIsFalse(id).orElse(null);

        if (product == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
        }

        product.setActive(DataConst.ACTIVE_TRUE);
        product.setUpdateAt(new Date());
        product.setUpdateBy(systemService.getUserLogin());

        Product productSaved = productRepository.save(product);

        if (productSaved != null) {

            // resotre all varient product

            systemService.writeSystemLog(product.getId(), product.getName(), null);

            return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);

        }
        return ResponseUtils.fail(500, MessageConst.RESTORE_FAIL, null);

    }

    public ResponseDTO detail(int id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode(product.getCode());
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setActive(product.getActive());

        List<ProductCategory> productCategories = productCategoryRepository.findByProduct(product);
        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        for (ProductCategory productCategory : productCategories) {
            Category category = productCategory.getCategory();
            CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
            categoryDTOs.add(categoryDTO);
        }

        List<ProductVarient> productVarients = productVarientRepository.findByProduct(product);
        List<ProductVarientDTO> productVarientDTOs = new ArrayList<>();

        for (ProductVarient productVarient : productVarients){
            ProductVarientDTO productVarientDTO = modelMapper.map(productVarient, ProductVarientDTO.class);
            productVarientDTOs.add(productVarientDTO);
            
        }

        productDTO.setCategories(categoryDTOs);
        productDTO.setProductVarients(productVarientDTOs);
        return ResponseUtils.success(200, "Chi tiết sản phẩm", productDTO);

    }

    public ResponseDTO show(int active) {
        List<Product> products = new ArrayList<>();

        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                products = productRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_FALSE:
                products = productRepository.findAllByActiveIsTrue().orElse(products);
                break;
            case RequestParamConst.ACTIVE_TRUE:
                products = productRepository.findAllByActiveIsFalse().orElse(products);
                break;
            default:
                products = productRepository.findAll();
                break;
        }
        List<ResponseDataDTO> productDTOS = new ArrayList<>();

        for (Product product : products) {
            ProductDTO productDTO = (ProductDTO) detail(product.getId()).getData();
            productDTOS.add(productDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(productDTOS);
        reponseListDataDTO.setNameList("Danh sách sản phẩm");
        return ResponseUtils.success(200, "Danh sách sản phẩm", reponseListDataDTO);
    }
}
