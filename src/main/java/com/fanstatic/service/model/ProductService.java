package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import com.fanstatic.dto.model.option.OptionGroupDTO;
import com.fanstatic.dto.model.option.OptionGroupRequestDTO;
import com.fanstatic.dto.model.option.OptionRequestDTO;
import com.fanstatic.dto.model.permissioin.RoleDTO;
import com.fanstatic.dto.model.product.ProductChangeStockDTO;
import com.fanstatic.dto.model.product.ProductDTO;
import com.fanstatic.dto.model.product.ProductImageDTO;
import com.fanstatic.dto.model.product.ProductOptionRequestDTO;
import com.fanstatic.dto.model.product.ProductRequestDTO;
import com.fanstatic.dto.model.product.ProductVarientDTO;
import com.fanstatic.dto.model.saleevent.SaleEventDTO;
import com.fanstatic.model.Category;
import com.fanstatic.model.File;
import com.fanstatic.model.Option;
import com.fanstatic.model.OptionGroup;
import com.fanstatic.model.Product;
import com.fanstatic.model.ProductCategory;
import com.fanstatic.model.ProductImage;
import com.fanstatic.model.ProductOption;
import com.fanstatic.model.ProductVarient;
import com.fanstatic.model.SaleEvent;
import com.fanstatic.repository.CategoryRepository;
import com.fanstatic.repository.OptionGroupRepository;
import com.fanstatic.repository.OptionRepository;
import com.fanstatic.repository.ProductCategoryRepository;
import com.fanstatic.repository.ProductImageRepository;
import com.fanstatic.repository.ProductOptionRepository;
import com.fanstatic.repository.ProductRepository;
import com.fanstatic.repository.ProductVarientRepository;
import com.fanstatic.repository.SaleProductRepository;
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
    private final SaleProductRepository saleProductRepository;
    private final ProductOptionRepository productOptionRepository;
    private final OptionRepository optionRepository;
    private final OptionGroupRepository optionGroupRepository;
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

        if (productRequestDTO.getDescriptionFile() != null) {
            File file = fileService.upload(productRequestDTO.getDescriptionFile(), ImageConst.PRODUCT_FOLDER);
            product.setDescription(file);

        }

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

            // save product description

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
                ProductImage productImage = new ProductImage();
                productImage.setImage(file);
                productImage.setProduct(product);
                productImageRepository.saveAndFlush(productImage);
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

    // public ResponseDTO saveProductOption(ProductOptionRequestDTO
    // productOptionRequestDTO) {
    // TransactionStatus transactionStatus = transactionManager.getTransaction(new
    // DefaultTransactionDefinition());
    // // save product category
    // try {
    // List<OptionGroupRequestDTO> optionGroupRequestDTOs =
    // productOptionRequestDTO.getOptionGroups();
    // Product product =
    // productRepository.findByIdAndActiveIsTrue(productOptionRequestDTO.getProductId())
    // .orElse(null);

    // if (product == null) {
    // return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
    // }

    // } catch (Exception e) {
    // transactionManager.rollback(transactionStatus);
    // return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
    // }
    // return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
    // }

    public ResponseDTO saveProductOption(ProductOptionRequestDTO productOptionRequestDTO) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<OptionGroupRequestDTO> optionGroupRequestDTOs = productOptionRequestDTO.getOptionGroups();
            Product product = productRepository.findByIdAndActiveIsTrue(productOptionRequestDTO.getProductId())
                    .orElse(null);

            if (product == null) {
                return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
            }

            List<ProductOption> existingProductOptions = product.getProductOptions();

            System.out.println("LENG: " + existingProductOptions.size());
            List<OptionGroupRequestDTO> optionsSaved = productOptionRequestDTO.getOptionGroups().stream()
                    .map(OptionGroupRequestDTO::new) // Tạo một bản sao của mỗi đối tượng
                    .collect(Collectors.toList());

            // Thêm mới các OptionGroup từ request
            for (OptionGroupRequestDTO optionGroupRequestDTO : optionGroupRequestDTOs) {

                if (optionGroupRequestDTO.getId() == null) {

                    ProductOption newProductOption = new ProductOption();

                    OptionGroup optionGroup = new OptionGroup();
                    optionGroup.setMultichoice(
                            optionGroupRequestDTO.isMultichoice() ? DataConst.ACTIVE_TRUE : DataConst.ACTIVE_FALSE);
                    optionGroup.setShare(
                            optionGroupRequestDTO.isShared() ? DataConst.ACTIVE_TRUE : DataConst.ACTIVE_FALSE);
                    optionGroup.setName(optionGroupRequestDTO.getName());

                    optionGroup = optionGroupRepository.saveAndFlush(optionGroup);

                    newProductOption.setProduct(product);
                    newProductOption.setOptionGroup(optionGroup);
                    newProductOption.setActive(DataConst.ACTIVE_TRUE);

                    productOptionRepository.save(newProductOption);
                    System.out.println("SET NEW : " + 1);

                    // Kiểm tra và thêm mới các Option
                    for (OptionRequestDTO optionRequestDTO : optionGroupRequestDTO.getOptions()) {
                        Option newOption = new Option();
                        newOption.setName(optionRequestDTO.getName());
                        newOption.setPrice(optionRequestDTO.getPrice());
                        newOption.setOptionGroup(optionGroup);
                        newOption.setActive(DataConst.ACTIVE_TRUE);
                        optionRepository.save(newOption);
                    }

                    optionsSaved.remove(optionGroupRequestDTO);
                }

                // Nếu không tồn tại trong sản phẩm, thêm mới

            }
            System.out.println("LENG: " + existingProductOptions.size());

            for (ProductOption existingProductOption : existingProductOptions) {
                System.out.println("EXIT: " + existingProductOption.getId());
                boolean existsInRequest = false;

                for (OptionGroupRequestDTO optionGroupRequestDTO : optionsSaved) {
                    if (existingProductOption.getOptionGroup().getId() == optionGroupRequestDTO.getId()) {
                        existsInRequest = true;
                        OptionGroup optionGroup = existingProductOption.getOptionGroup();
                        optionGroup.setMultichoice(
                                optionGroupRequestDTO.isMultichoice() ? DataConst.ACTIVE_TRUE : DataConst.ACTIVE_FALSE);
                        optionGroup.setShare(
                                optionGroupRequestDTO.isShared() ? DataConst.ACTIVE_TRUE : DataConst.ACTIVE_FALSE);
                        optionGroup.setName(optionGroupRequestDTO.getName());
                        optionGroup = optionGroupRepository.saveAndFlush(optionGroup);

                        existingProductOption.setOptionGroup(optionGroup);
                        productOptionRepository.save(existingProductOption);
                        updateOptions(existingProductOption, optionGroupRequestDTO.getOptions());
                        break;
                    }
                }

                if (!existsInRequest) {
                    // Nếu không tồn tại trong request, xóa bỏ
                    System.out.println("SET FALSE: " + existingProductOption.getId());
                    existingProductOption.setActive(DataConst.ACTIVE_FALSE);
                    productOptionRepository.save(existingProductOption);
                }
            }

            transactionManager.commit(transactionStatus);
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
        } catch (

        Exception e) {
            e.printStackTrace();
            transactionManager.rollback(transactionStatus);
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        }

    }

    private void updateOptions(ProductOption productOption, List<OptionRequestDTO> optionRequestDTOs) {
        List<Option> existingOptions = productOption.getOptionGroup().getOptions();

        List<OptionRequestDTO> optionsSaved = optionRequestDTOs.stream()
                .map(OptionRequestDTO::new) // Tạo một bản sao của mỗi đối tượng OptionRequestDTO
                .collect(Collectors.toList());

        for (OptionRequestDTO optionRequestDTO : optionRequestDTOs) {
            if (optionRequestDTO.getId() == null) {
                Option newOption = new Option();
                newOption.setName(optionRequestDTO.getName());
                newOption.setPrice(optionRequestDTO.getPrice());
                newOption.setOptionGroup(productOption.getOptionGroup());
                newOption.setActive(DataConst.ACTIVE_TRUE);
                optionRepository.save(newOption);
                optionsSaved.remove(optionRequestDTO);

            }
        }
        // check if option need to delete
        for (Option existingOption : existingOptions) {
            boolean isExits = false;

            for (OptionRequestDTO optionRequestDTO : optionsSaved) {
                if (existingOption.getId() == optionRequestDTO.getId()) {

                    existingOption.setName(optionRequestDTO.getName());
                    existingOption.setPrice(optionRequestDTO.getPrice());
                    optionRepository.save(existingOption);
                    isExits = true;
                    break;
                }
            }

            if (!isExits) {
                existingOption.setActive(DataConst.ACTIVE_FALSE);
                optionRepository.save(existingOption);
            }
        }

    }

    public ResponseDTO updateDescriptionProduct(Integer productId, MultipartFile fileDescription) {
        Product product = productRepository.findByIdAndActiveIsTrue(productId).orElse(null);
        if (product == null) {
            return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
        }

        File description = product.getDescription();

        if (description != null) {
            fileService.updateFile(fileDescription, ImageConst.PRODUCT_FOLDER, description);
            product.setDescription(description);

        } else {
            File file = fileService.upload(fileDescription, ImageConst.PRODUCT_FOLDER);

            product.setDescription(file);
        }
        productRepository.save(product);

        return ResponseUtils.success(200, "Cập nhật mô tả thành công", null);

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
        productDTO.setOutOfStock(product.getOutOfStock());


        SaleEvent saleEvent = saleProductRepository.findSaleByProductId(product.getId()).orNull();
        if (saleEvent != null) {
            productDTO.setSaleEvent(modelMapper.map(saleEvent, SaleEventDTO.class));
        }

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
            SaleEvent saleEventVarient = saleProductRepository.findSaleByProductVarientId(productVarient.getId())
                    .orNull();
            if (saleEventVarient != null) {
                productVarientDTO.setSaleEvent(modelMapper.map(saleEventVarient, SaleEventDTO.class));
            }
            productVarientDTO.setImageUrl(getProductVarientImage(productVarient));
            productVarientDTOs.add(productVarientDTO);

        }

        List<ProductOption> productOptions = productOptionRepository.findByProduct(product);
        List<OptionGroupDTO> optionGroupDTOs = new ArrayList<>();
        for (ProductOption productOption : productOptions) {
            OptionGroup optionGroup = productOption.getOptionGroup();
            OptionGroupDTO optionGroupDTO = modelMapper.map(optionGroup, OptionGroupDTO.class);
            optionGroupDTOs.add(optionGroupDTO);
        }

        productDTO.setCategories(categoryDTOs);
        productDTO.setOptionGroups(optionGroupDTOs);
        productDTO.setProductVarients(productVarientDTOs);
        productDTO.setImageUrl(getProductImage(product));

        File description = product.getDescription();
        if (description != null) {
            productDTO.setDescriptionUrl(description.getLink());

        }

        return ResponseUtils.success(200, "Chi tiết sản phẩm", productDTO);

    }

    public List<ProductImageDTO> getProductImage(Product product) {
        List<ProductImage> productImages = productImageRepository.findByProduct(product);
        List<ProductImageDTO> productImageDTOs = new ArrayList<>();
        for (ProductImage productImage : productImages) {
            ProductImageDTO productImageDTO = new ProductImageDTO();
            File image = productImage.getImage();
            if (image != null) {
                productImageDTO.setId(image.getId());
                productImageDTO.setImageUrl(image.getLink());
                productImageDTOs.add(productImageDTO);

            }
            productImageDTOs.add(productImageDTO);

        }

        return productImageDTOs;
    }

    public List<ProductImageDTO> getProductVarientImage(ProductVarient productVarient) {
        List<ProductImage> productImages = productImageRepository.findByProductVarient(productVarient);
        List<ProductImageDTO> productImageDTOs = new ArrayList<>();
        for (ProductImage productImage : productImages) {
            ProductImageDTO productImageDTO = new ProductImageDTO();
            File image = productImage.getImage();
            if (image != null) {
                productImageDTO.setId(image.getId());
                productImageDTO.setImageUrl(image.getLink());
                productImageDTOs.add(productImageDTO);

            }
            productImageDTOs.add(productImageDTO);

        }

        return productImageDTOs;
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

    public ResponseDTO changeIsOutOfStock(ProductChangeStockDTO productChangeStockDTO) {
        if (productChangeStockDTO.getProductId() != null) {
            Product product = productRepository.findByIdAndActiveIsTrue(productChangeStockDTO.getProductId())
                    .orElse(null);
            if (product == null) {
                return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);

            }

            product.setOutOfStock((byte) (productChangeStockDTO.isOutOfStack() == true ? 1 : 0));
            productRepository.save(product);
            return ResponseUtils.success(200, "Cập nhật thành công", detail(product.getId()).getData());

        }
        return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);

    }

    public ResponseDTO changeProductVarientIsOutOfStock(ProductChangeStockDTO productChangeStockDTO) {
        if (productChangeStockDTO.getProductVariantId() != null) {
            ProductVarient productVarient = productVarientRepository
                    .findByIdAndActiveIsTrue(productChangeStockDTO.getProductVariantId())
                    .orElse(null);
            if (productVarient == null) {
                return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);

            }

            productVarient.setOutOfStock((byte) (productChangeStockDTO.isOutOfStack() ? 1 : 0));
            productVarientRepository.save(productVarient);

            return ResponseUtils.success(200, "Cập nhật thành công", detail(productVarient.getId()).getData());

        }
        return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);

    }

}
