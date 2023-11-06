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
import com.fanstatic.config.constants.ImageConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.category.CategoryDTO;
import com.fanstatic.dto.model.permissioin.RoleDTO;
import com.fanstatic.dto.model.product.ProductDTO;
import com.fanstatic.dto.model.product.ProductImageDTO;
import com.fanstatic.dto.model.product.ProductRequestDTO;
import com.fanstatic.dto.model.product.ProductVarientDTO;
import com.fanstatic.model.Category;
import com.fanstatic.model.File;
import com.fanstatic.model.Product;
import com.fanstatic.model.ProductCategory;
import com.fanstatic.model.ProductImage;
import com.fanstatic.model.ProductVarient;
import com.fanstatic.repository.CategoryRepository;
import com.fanstatic.repository.ProductCategoryRepository;
import com.fanstatic.repository.ProductImageRepository;
import com.fanstatic.repository.ProductRepository;
import com.fanstatic.repository.ProductVarientRepository;
import com.fanstatic.service.system.FileService;
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
    private final ProductImageRepository productImageRepository;
    private final FileService fileService;

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

        if (productRequestDTO.getImageFiles().isEmpty() || productRequestDTO.getImageFiles() == null) {
            errors.add(new FieldError("productRequestDTO", "image", "Vui lòng chọn ảnh"));
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

            // save product image
            ResponseDTO productImageSaved = saveProductImage(productRequestDTO.getImageFiles(), productSaved);
            if (!productImageSaved.isSuccess()) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(productImageSaved.getStatusCode(),
                        productImageSaved.getMessage(),
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

    public ResponseDTO saveProductImage(List<MultipartFile> images, Product product) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        // save product category
        try {

            for (MultipartFile image : images) {
                File file = fileService.upload(image, ImageConst.PRODUCT_FOLDER);
                System.out.println("FILE ID:  " + file.getId());
                ProductImage productImage = new ProductImage();
                productImage.setImage(file);
                productImage.setProduct(product);
                ProductImage productImage2 = productImageRepository.saveAndFlush(productImage);
                System.out.println("Lưu thành công: " + productImage2.getId());
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

            systemService.writeSystemLog(product.getId(), product.getName(), null);
            transactionManager.commit(transactionStatus);
            return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

        }
        transactionManager.rollback(transactionStatus);

        return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);
    }

    public ResponseDTO updateImage(int id, List<MultipartFile> newImage, List<Integer> removeImagesId) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        Product product = productRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (product == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
        }

        for (Integer imageId : removeImagesId) {
            fileService.delete(imageId);
            productImageRepository.deleteByImageId(imageId);
        }

        ResponseDTO productImageSaved = saveProductImage(newImage, product);
        if (!productImageSaved.isSuccess()) {
            transactionManager.rollback(transactionStatus);
            return ResponseUtils.fail(productImageSaved.getStatusCode(),
                    productImageSaved.getMessage(),
                    null);
        }

        transactionManager.commit(transactionStatus);

        return ResponseUtils.success(200, "Cập nhật hình ảnh thành công", null);

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

        for (ProductVarient productVarient : productVarients) {
            ProductVarientDTO productVarientDTO = modelMapper.map(productVarient, ProductVarientDTO.class);
            productVarientDTOs.add(productVarientDTO);

        }

        List<ProductImage> productImages = productImageRepository.findByProduct(product);
        List<ProductImageDTO> productImageDTOs = new ArrayList<>();
        for (ProductImage productImage : productImages) {
            ProductImageDTO productImageDTO = new ProductImageDTO();
            File image = productImage.getImage();
            if (image != null) {
                productImageDTO.setId(id);
                productImageDTO.setImageUrl(image.getLink());
                productImageDTOs.add(productImageDTO);

            }

        }`

        productDTO.setCategories(categoryDTOs);
        productDTO.setProductVarients(productVarientDTOs);
        productDTO.setImageUrl(productImageDTOs);
        return ResponseUtils.success(200, "Chi tiết sản phẩm", productDTO);

    }

    public ResponseDTO show(int active) {
        List<Product> products = new ArrayList<>();

        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                products = productRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_TRUE:
                products = productRepository.findAllByActiveIsTrue().orElse(products);
                break;
            case RequestParamConst.ACTIVE_FALSE:
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
