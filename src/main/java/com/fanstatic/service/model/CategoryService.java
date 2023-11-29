package com.fanstatic.service.model;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.ImageConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.category.CategoryDTO;
import com.fanstatic.dto.model.category.CategoryRequestDTO;
import com.fanstatic.model.Category;
import com.fanstatic.model.File;
import com.fanstatic.repository.CategoryRepository;
import com.fanstatic.service.system.FileService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final SystemService systemService;
    private final FileService fileService;

    private final int MAX_LEVEL = 4;
    private final int ROOT_LEVEL = 1;

    public ResponseDTO create(CategoryRequestDTO categoryRequestDTO) {
        List<FieldError> errors = new ArrayList<>();

        if (categoryRepository.findByCodeAndActiveIsTrue(categoryRequestDTO.getCode()).isPresent()) {
            errors.add(new FieldError("categoryRequestDTO", "code", "Code đã tồn tại"));
        }
        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        Category category = modelMapper.map(categoryRequestDTO, Category.class);

        // check image
        MultipartFile image = categoryRequestDTO.getImageFile();
        if (image != null) {
            File file = fileService.upload(image, ImageConst.CATEGORY_FOLDER);
            category.setImage(file);
            // save image to Fisebase and file table
        }

        category.setActive(DataConst.ACTIVE_TRUE);
        category.setCreateAt(new Date());
        category.setCreateBy(systemService.getUserLogin());

        // check parent category
        if (categoryRequestDTO.getParent_id() != null) {
            Category parenCategory = categoryRepository.findByIdAndActiveIsTrue(categoryRequestDTO.getParent_id())
                    .orElse(null);
            if (parenCategory != null) {
                // check max level
                if (parenCategory.getLevel() == MAX_LEVEL) {
                    return ResponseUtils.fail(404, "Không thể tạo danh mục này", null);
                }

                // set category level hight than paren 1 level
                category.setParentCategory(parenCategory);

                category.setLevel(parenCategory.getLevel() + 1);
            } else {
                return ResponseUtils.fail(404, "Danh mục cha không tồn tại", null);
            }
        } else {
            category.setLevel(ROOT_LEVEL);
        }
        File file = fileService.upload(image, ImageConst.CATEGORY_FOLDER);
        category.setImage(file);

        Category categorySaved = categoryRepository.saveAndFlush(category);
        if (categorySaved != null) {

            systemService.writeSystemLog(categorySaved.getId(), categorySaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);

        } else {
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);

        }
    }

    public ResponseDTO update(CategoryRequestDTO categoryRequestDTO) {
        List<FieldError> errors = new ArrayList<>();

        Category category = categoryRepository.findByIdAndActiveIsTrue(categoryRequestDTO.getId()).orElse(null);
        if (category == null) {
            return ResponseUtils.fail(404, "Danh mục không tồn tại", null);
        }

        if (categoryRepository
                .findByCodeAndActiveIsTrueAndIdNot(categoryRequestDTO.getCode(), categoryRequestDTO.getId())
                .isPresent()) {
            errors.add(new FieldError("categoryRequestDTO", "code", "Code đã tồn tại"));
        }
        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        category = modelMapper.map(categoryRequestDTO, Category.class);
        category.setActive(DataConst.ACTIVE_TRUE);
        category.setUpdateAt(new Date());
        category.setUpdateBy(systemService.getUserLogin());

        // check parent category
        if (categoryRequestDTO.getParent_id() != null) {
            Category parenCategory = categoryRepository.findByIdAndActiveIsTrue(categoryRequestDTO.getParent_id())
                    .orElse(null);
            if (parenCategory != null) {
                // set category level hight than paren 1 level
                category.setParentCategory(parenCategory);

                category.setLevel(parenCategory.getLevel() + 1);
            } else {
                return ResponseUtils.fail(404, "Danh mục cha không tồn tại", null);
            }

        } else {
            category.setLevel(1);
        }

        Category categorySaved = categoryRepository.save(category);
        if (categorySaved != null) {

            systemService.writeSystemLog(categorySaved.getId(), categorySaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

        } else {
            return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);

        }
    }

    public ResponseDTO updateImage(int id, MultipartFile image) {
        Category category = categoryRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (category == null) {
            return ResponseUtils.fail(404, "Danh mục không tồn tại", null);
        }
        // check image
        if (image != null) {
            if (category.getImage() != null) {
                fileService.deleteFireStore(category.getImage().getName());

                fileService.updateFile(image, ImageConst.CATEGORY_FOLDER, category.getImage());

            } else {
                File file = fileService.upload(image, ImageConst.CATEGORY_FOLDER);
                category.setImage(file);

            }
            Category categorySaved = categoryRepository.save(category);
            if (categorySaved != null) {

                systemService.writeSystemLog(categorySaved.getId(), categorySaved.getName(), null);
                return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

            } else {
                return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);

            }

        }
        return ResponseUtils.fail(200, "Uploadimage", null);

    }

    public void deleteChildCatetegory(List<Category> categories) {
        for (Category category : categories) {

            category.setActive(DataConst.ACTIVE_FALSE);
            category.setDeleteAt(new Date());
            category.setDeleteBy(systemService.getUserLogin());
            List<Category> childCategories = categoryRepository.findByParentCategoryAndActiveIsTrue(category)
                    .orElse(new ArrayList<>());
            if (!childCategories.isEmpty()) {
                deleteChildCatetegory(childCategories);
            }
        }
    }

    public ResponseDTO delete(int id) {
        Category category = categoryRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (category == null) {
            return ResponseUtils.fail(404, "Danh mục không tồn tại", null);
        }

        category.setActive(DataConst.ACTIVE_FALSE);
        category.setDeleteAt(new Date());
        category.setDeleteBy(systemService.getUserLogin());

        Category categorySaved = categoryRepository.save(category);
        if (categorySaved != null) {

            // delete child category
            List<Category> chidCategories = categoryRepository.findByParentCategoryAndActiveIsTrue(category)
                    .orElse(new ArrayList<>());
            deleteChildCatetegory(chidCategories);

            systemService.writeSystemLog(categorySaved.getId(), categorySaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);

        } else {
            return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);

        }
    }

    public void restoreChildCatetegory(List<Category> categories) {
        for (Category category : categories) {

            category.setActive(DataConst.ACTIVE_TRUE);
            category.setUpdateAt(new Date());
            category.setUpdateBy(systemService.getUserLogin());
            List<Category> childCategories = categoryRepository.findByParentCategoryAndActiveIsTrue(category)
                    .orElse(new ArrayList<>());
            if (!childCategories.isEmpty()) {
                deleteChildCatetegory(childCategories);
            }
        }
    }

    public ResponseDTO restore(int id) {
        Category category = categoryRepository.findByIdAndActiveIsFalse(id).orElse(null);
        if (category == null) {
            return ResponseUtils.fail(404, "Danh mục không tồn tại", null);
        }

        category.setActive(DataConst.ACTIVE_TRUE);
        category.setUpdateAt(new Date());
        category.setUpdateBy(systemService.getUserLogin());

        Category categorySaved = categoryRepository.save(category);
        if (categorySaved != null) {

            List<Category> chidCategories = categoryRepository.findByParentCategoryAndActiveIsTrue(category)
                    .orElse(new ArrayList<>());
            restoreChildCatetegory(chidCategories);

            systemService.writeSystemLog(categorySaved.getId(), categorySaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);

        } else {
            return ResponseUtils.fail(500, MessageConst.RESTORE_FAIL, null);

        }
    }

    // private void processCategoryTree(Category category, List<ResponseDataDTO>
    // categoryDTOs) {
    // System.out.println("PARENT LEVEL " + category.getLevel());
    // System.out.println("=====");
    // System.out.println(category.getId() + " - " + category.getCode());

    // CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
    // categoryDTOs.add(categoryDTO);

    // List<ResponseDataDTO> subCategoryDTOs = new ArrayList<>();

    // List<Category> subCategories =
    // categoryRepository.findByParentCategoryAndActiveIsTrue(category)
    // .orElse(new ArrayList<>());

    // for (Category subCategory : subCategories) {
    // processCategoryTree(subCategory, subCategoryDTOs);
    // }
    // }

    public ResponseDTO showRoot(int active) {

        List<Category> rootCategories = categoryRepository.findByLevelAndActiveIsTrue(1).orElse(new ArrayList<>());

        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                rootCategories = categoryRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_TRUE:
                rootCategories = categoryRepository.findByLevelAndActiveIsTrue(ROOT_LEVEL).orElse(rootCategories);
                break;
            case RequestParamConst.ACTIVE_FALSE:
                rootCategories = categoryRepository.findByLevelAndActiveIsFalse(ROOT_LEVEL).orElse(rootCategories);
                break;
            default:
                rootCategories = categoryRepository.findByLevel(ROOT_LEVEL).orElse(rootCategories);
                break;
        }
        List<ResponseDataDTO> categoryDTOs = new ArrayList<>();

        for (Category category : rootCategories) {
            CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
            categoryDTOs.add(categoryDTO);
        }

        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(categoryDTOs);
        return ResponseUtils.success(200, "Danh mục sản phẩm", reponseListDataDTO);
    }

    public ResponseDTO show(int active) {

        List<Category> rootCategories = categoryRepository.findByLevelAndActiveIsTrue(1).orElse(new ArrayList<>());

        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                rootCategories = categoryRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_TRUE:
                rootCategories = categoryRepository.findByLevelAndActiveIsTrue(ROOT_LEVEL).orElse(rootCategories);
                break;
            case RequestParamConst.ACTIVE_FALSE:
                rootCategories = categoryRepository.findByLevelAndActiveIsFalse(ROOT_LEVEL).orElse(rootCategories);
                break;
            default:
                rootCategories = categoryRepository.findByLevel(ROOT_LEVEL).orElse(rootCategories);
                break;
        }
        List<ResponseDataDTO> categoryDTOs = new ArrayList<>();

        // for (Category rootCategory : rootCategories) {
        // processCategoryTree(rootCategory, categoryDTOs);
        // }

        for (Category category : rootCategories) {
            CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
            categoryDTOs.add(categoryDTO);

            List<Category> categories2 = categoryRepository.findByParentCategoryAndActiveIsTrue(category)
                    .orElse(new ArrayList<>());

            List<ResponseDataDTO> categoryDTO2s = new ArrayList<>();

            categoryDTO.setChildCategories(categoryDTO2s);

            for (Category category2 : categories2) {
                CategoryDTO categoryDTO2 = modelMapper.map(category2, CategoryDTO.class);
                String imageUrl = category2.getImage() != null ? category2.getImage().getLink() : "";
                categoryDTO2.setImageUrl(imageUrl);
                categoryDTO2s.add(categoryDTO2);
                List<Category> categories3 = categoryRepository.findByParentCategoryAndActiveIsTrue(category2)
                        .orElse(new ArrayList<>());
                List<ResponseDataDTO> categoryDTO3s = new ArrayList<>();
                categoryDTO2.setChildCategories(categoryDTO3s);

                for (Category category3 : categories3) {
                    CategoryDTO categoryDTO3 = modelMapper.map(category3, CategoryDTO.class);
                    String imageUrl3 = category3.getImage() != null ? category3.getImage().getLink() : "";
                    categoryDTO3.setImageUrl(imageUrl3);

                    categoryDTO3s.add(categoryDTO3);
                    List<Category> categories4 = categoryRepository.findByParentCategoryAndActiveIsTrue(category3)
                            .orElse(new ArrayList<>());
                    List<ResponseDataDTO> categoryDTO4s = new ArrayList<>();

                    categoryDTO3.setChildCategories(categoryDTO4s);

                    for (Category category4 : categories4) {
                        CategoryDTO categoryDTO4 = modelMapper.map(category4, CategoryDTO.class);
                        String imageUrl4 = category4.getImage() != null ? category4.getImage().getLink() : "";
                        categoryDTO4.setImageUrl(imageUrl4);

                        categoryDTO4s.add(categoryDTO4);

                    }
                }
            }

        }

        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(categoryDTOs);
        return ResponseUtils.success(200, "Danh mục sản phẩm", reponseListDataDTO);
    }

    public ResponseDTO detail(int id) {
        Category category = categoryRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (category == null) {
            return ResponseUtils.fail(404, "Danh mục không tồn tại", null);
        }

        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

        List<Category> categories2 = categoryRepository.findByParentCategoryAndActiveIsTrue(category)
                .orElse(new ArrayList<>());

        List<ResponseDataDTO> categoryDTO2s = new ArrayList<>();

        categoryDTO.setChildCategories(categoryDTO2s);

        for (Category category2 : categories2) {
            CategoryDTO categoryDTO2 = modelMapper.map(category2, CategoryDTO.class);
            String imageUrl = category2.getImage() != null ? category2.getImage().getLink() : "";
            categoryDTO2.setImageUrl(imageUrl);

            categoryDTO2s.add(categoryDTO2);
            List<Category> categories3 = categoryRepository.findByParentCategoryAndActiveIsTrue(category2)
                    .orElse(new ArrayList<>());
            List<ResponseDataDTO> categoryDTO3s = new ArrayList<>();
            categoryDTO2.setChildCategories(categoryDTO3s);

            for (Category category3 : categories3) {
                CategoryDTO categoryDTO3 = modelMapper.map(category3, CategoryDTO.class);
                String imageUrl3 = category3.getImage() != null ? category3.getImage().getLink() : "";
                categoryDTO3.setImageUrl(imageUrl3);

                categoryDTO3s.add(categoryDTO3);
                List<Category> categories4 = categoryRepository.findByParentCategoryAndActiveIsTrue(category3)
                        .orElse(new ArrayList<>());
                List<ResponseDataDTO> categoryDTO4s = new ArrayList<>();

                categoryDTO3.setChildCategories(categoryDTO4s);
                for (Category category4 : categories4) {
                    CategoryDTO categoryDTO4 = modelMapper.map(category4, CategoryDTO.class);
                    String imageUrl4 = category4.getImage() != null ? category4.getImage().getLink() : "";
                    categoryDTO4.setImageUrl(imageUrl4);

                    categoryDTO4s.add(categoryDTO4);

                }
            }
        }

        return ResponseUtils.success(200, "Chi tiết danh mục", categoryDTO);

    }
}
