package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.ImageConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.account.AccountRequestDTO;
import com.fanstatic.dto.model.user.UserDTO;
import com.fanstatic.dto.model.user.UserRequestDTO;
import com.fanstatic.model.User;
import com.fanstatic.model.File;
import com.fanstatic.model.User;
import com.fanstatic.repository.UserRepository;
import com.fanstatic.service.system.FileService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final SystemService systemService;
    private final FileService fileService;

    @Autowired
    @Lazy
    private RoleService roleService;
    @Autowired
    @Lazy
    private AccountService accountService;

    public ResponseDTO create(UserRequestDTO userRequestDTO) {
        List<FieldError> errors = new ArrayList<>();
        // check data exits
        if (userRepository.findByNumberPhoneAndActiveIsTrue(userRequestDTO.getNumberPhone()).isPresent()) {
            errors.add(new FieldError("userRequestDTO", "numberPhone", "Số điện thoại đã được sử dụng."));
        }

        if (userRepository.findByEmailAndActiveIsTrue(userRequestDTO.getEmail()).isPresent()) {
            errors.add(new FieldError("userRequestDTO", "email", "Email đã được sử dụng"));
        }

        if (userRepository.findByCccdCmndAndActiveIsTrue(userRequestDTO.getCccdCmnd()).isPresent()) {
            errors.add(new FieldError("userRequestDTO", "cccdCmnd", "CCCD đã được sử dụng"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        // check image

        User user = modelMapper.map(userRequestDTO, User.class);
        MultipartFile image = userRequestDTO.getImage();
        if (image != null) {
            File file = fileService.upload(image, ImageConst.USER_FOLDER);
            user.setImage(file);
            // save image to Fisebase and file table
        }

        String employeeCode = generateEmployeeCode(user.getName());

        user.setRole(roleService.getById(userRequestDTO.getRoleId()));
        user.setEmployeeCode(employeeCode);
        user.setActive(DataConst.ACTIVE_TRUE);
        user.setCreateAt(new Date());
        user.setCreateBy(systemService.getUserLogin());

        User userSaved = userRepository.saveAndFlush(user);
        if (userSaved != null) {

            // get user saved againt
            ResponseDTO accountReponse = accountService
                    .create(new AccountRequestDTO(user.getNumberPhone(), userRequestDTO.getPassword(),
                            userRequestDTO.getRoleId(), user.getId()));
            if (accountReponse.isSuccess()) {
                systemService.writeSystemLog(user.getId(), userSaved.getName(), null);
                return ResponseUtils.success(200, "Tạo thành công", null);
            } else {
                return ResponseUtils.fail(accountReponse.getStatusCode(), accountReponse.getMessage(), null);

            }

        } else {
            return ResponseUtils.fail(500, "Tạo thất bại", null);

        }
    }

    public ResponseDTO update(UserRequestDTO userRequestDTO) {
        List<FieldError> errors = new ArrayList<>();

        User user = userRepository.findByIdAndActiveIsTrue(userRequestDTO.getId()).orElse(null);
        if (user == null) {
            return ResponseUtils.fail(500, "User không tồn tại", null);

        }

        // check data exits
        if (userRepository
                .findByNumberPhoneAndActiveIsTrueAndIdNot(userRequestDTO.getNumberPhone(), userRequestDTO.getId())
                .isPresent()) {

            errors.add(new FieldError("userRequestDTO", "numberPhone", "Số điện thoại đã được sử dụng."));
        }

        if (userRepository.findByEmailAndActiveIsTrueAndIdNot(userRequestDTO.getEmail(), userRequestDTO.getId())
                .isPresent()) {
            errors.add(new FieldError("userRequestDTO", "email", "Email đã được sử dụng"));
        }

        if (userRepository.findByCccdCmndAndActiveIsTrueAndIdNot(userRequestDTO.getCccdCmnd(), userRequestDTO.getId())
                .isPresent()) {
            errors.add(new FieldError("userRequestDTO", "cccdCmnd", "CCCD đã được sử dụng"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        modelMapper.map(userRequestDTO, user);

        user.setRole(roleService.getById(userRequestDTO.getRoleId()));
        user.setActive(DataConst.ACTIVE_TRUE);
        user.setUpdateAt(new Date());
        user.setUpdateBy(systemService.getUserLogin());

        User userSaved = userRepository.save(user);
        if (userSaved != null) {

            ResponseDTO accountReponse = accountService
                    .update(new AccountRequestDTO(user.getNumberPhone(), userRequestDTO.getPassword(),
                            userRequestDTO.getRoleId(), user.getId()));
            if (accountReponse.isSuccess()) {
                systemService.writeSystemLog(user.getId(), userSaved.getName(), null);
                return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);
            } else {
                return ResponseUtils.fail(accountReponse.getStatusCode(), accountReponse.getMessage(), null);

            }

        } else {
            return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);

        }
    }

    public ResponseDTO updateImage(int id, MultipartFile image) {
        User user = userRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (user == null) {
            return ResponseUtils.fail(404, "Danh mục không tồn tại", null);
        }
        // check image
        if (image != null) {
            if (user.getImage() != null) {
                fileService.deleteFireStore(user.getImage().getName());

                fileService.updateFile(image, ImageConst.CATEGORY_FOLDER, user.getImage());

            } else {
                File file = fileService.upload(image, ImageConst.CATEGORY_FOLDER);
                user.setImage(file);

            }
            User userSaved = userRepository.save(user);
            if (userSaved != null) {

                systemService.writeSystemLog(userSaved.getId(), userSaved.getName(), null);
                return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

            } else {
                return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);

            }

        }
        return ResponseUtils.fail(200, "Uploadimage", null);

    }

    public ResponseDTO delete(Integer id) {
        User user = userRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (user == null) {
            return ResponseUtils.fail(500, "User không tồn tại", null);

        }

        user.setActive(DataConst.ACTIVE_FALSE);
        user.setDeleteAt(new Date());
        user.setDeleteBy(systemService.getUserLogin());
        User userSaved = userRepository.save(user);
        if (userSaved != null) {

            ResponseDTO accountReponse = accountService.delete(id);

            if (accountReponse.isSuccess()) {
                systemService.writeSystemLog(user.getId(), userSaved.getName(), null);
                return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);
            } else {
                return ResponseUtils.fail(accountReponse.getStatusCode(), accountReponse.getMessage(), null);

            }

        } else {
            return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);

        }

    }

    public ResponseDTO restore(Integer id) {
        User user = userRepository.findByIdAndActiveIsFalse(id).orElse(null);
        if (user == null) {
            return ResponseUtils.fail(500, "User không tồn tại", null);

        }

        user.setActive(DataConst.ACTIVE_TRUE);
        user.setUpdateAt(new Date());
        user.setUpdateBy(systemService.getUserLogin());
        User userSaved = userRepository.save(user);
        if (userSaved != null) {

            ResponseDTO accountReponse = accountService.restore(id);

            if (accountReponse.isSuccess()) {
                systemService.writeSystemLog(user.getId(), userSaved.getName(), null);
                return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);
            } else {
                return ResponseUtils.fail(accountReponse.getStatusCode(), accountReponse.getMessage(), null);

            }

        } else {
            return ResponseUtils.fail(500, MessageConst.RESTORE_FAIL, null);

        }

    }

    public ResponseDTO show(int active) {
        List<User> users = new ArrayList<>();

        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                users = userRepository.findAllByOrderByCreateAtDesc();
                break;
            case RequestParamConst.ACTIVE_TRUE:
                users = userRepository.findAllByActiveIsTrueOrderByCreateAtDesc().orElse(users);
                break;
            case RequestParamConst.ACTIVE_FALSE:
                users = userRepository.findAllByActiveIsFalseOrderByCreateAtDesc().orElse(users);
                break;
            default:
                users = userRepository.findAllByOrderByCreateAtDesc();
                break;
        }

        List<ResponseDataDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            modelMapper.map(user, userDTO);
            if (user.getImage() != null) {
                String imageUrl = user.getImage().getLink();
                userDTO.setImageUrl(imageUrl);
            }
            userDTOs.add(userDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(userDTOs);
        return ResponseUtils.success(200, "Danh sách người dùng", reponseListDataDTO);
    }

    public ResponseDTO detail(Integer id) {
        User user = userRepository.findById(id).orElse(null);

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        if (user.getImage() != null) {
            String imageUrl = user.getImage().getLink();
            userDTO.setImageUrl(imageUrl);
        }

        return ResponseUtils.success(200, "Chi tiết người dùng", userDTO);
    }

    public ResponseDTO checkCustomerExits(String numberPhone) {
        User user = userRepository.findByNumberPhoneAndActiveIsTrue(numberPhone).orElse(null);
        if (user == null) {
            return ResponseUtils.fail(404, "Khách hàng không tồn tại", null);

        }
        if (user.getRole().getId() == ApplicationConst.CUSTOMER_ROLE_ID) {
            return ResponseUtils.success(200, "Khách hàng có tồn tại", null);
        }
        return ResponseUtils.fail(404, "Khách hàng không tồn tại", null);

    }

    public User getDTOById(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return modelMapper.map(user, User.class);
        } else {
            return null;
        }
    }

    public User getById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public String generateEmployeeCode(String name) {
        name = name.trim();
        name = removeVietnameseCharacters(name);

        name = name.toUpperCase();

        String[] words = name.split(" ");
        int indexName = words.length - 1;
        String lastname = words[indexName];

        StringBuilder firstName = new StringBuilder();
        for (int index = 0; index < words.length; index++) {
            if (index != indexName) {
                firstName.append(words[index].charAt(0));
            }
        }

        String username = lastname + firstName.toString();

        int count = userRepository.countByEmployeeCodeLike(username).orElse(0);
        if (count > 0) {
            username = username + (count + 1);
        }
        return username;
    }

    public static String removeVietnameseCharacters(String input) {
        // Loại bỏ các ký tự tiếng Việt
        input = input.replaceAll("[đĐ]", "d");
        input = input.replaceAll("[àáảãạăắằẳẵặâầấẩẫậ]", "a");
        input = input.replaceAll("[èéẻẽẹêềếểễệ]", "e");
        input = input.replaceAll("[ìíỉĩị]", "i");
        input = input.replaceAll("[òóỏõọôồốổỗộơờớởỡợ]", "o");
        input = input.replaceAll("[ùúủũụưừứửữự]", "u");
        input = input.replaceAll("[ỳýỷỹỵ]", "y");

        return input;
    }

}
