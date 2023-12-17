package com.fanstatic.service.model;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.ImageConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.account.AccountDTO;
import com.fanstatic.dto.model.account.AccountRequestDTO;
import com.fanstatic.dto.model.user.UserDTO;
import com.fanstatic.dto.model.customer.CustomerRequestDTO;
import com.fanstatic.model.Account;
import com.fanstatic.model.File;
import com.fanstatic.model.Shift;
import com.fanstatic.model.User;
import com.fanstatic.repository.CustomerRepository;
import com.fanstatic.service.system.FileService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;
    private final SystemService systemService;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private RoleService roleService;
    @Autowired
    @Lazy
    private AccountService accountService;

    public ResponseDTO create(CustomerRequestDTO customerRequestDTO) {
        List<FieldError> errors = new ArrayList<>();
        // check data exits
        if (customerRepository.findByNumberPhoneAndActiveIsTrue(customerRequestDTO.getNumberPhone()).isPresent()) {
            errors.add(new FieldError("customerRequestDTO", "numberPhone", "Số điện thoại đã được sử dụng."));
        }

        if (customerRepository.findByEmailAndActiveIsTrue(customerRequestDTO.getEmail()).isPresent()) {
            errors.add(new FieldError("customerRequestDTO", "email", "Email đã được sử dụng"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        // check image
        MultipartFile image = customerRequestDTO.getImage();

        User user = modelMapper.map(customerRequestDTO, User.class);

        user.setRole(roleService.getById(3));
        user.setActive(DataConst.ACTIVE_TRUE);
        user.setCreateAt(new Date());
        user.setCreateBy(systemService.getUserLogin());

        File file = fileService.upload(image, ImageConst.USER_FOLDER);
        user.setImage(file);

        User userSaved = customerRepository.saveAndFlush(user);
        if (userSaved != null) {

            // get user saved againt
            ResponseDTO accountReponse = accountService
                    .create(new AccountRequestDTO(user.getNumberPhone(), customerRequestDTO.getPassword(),
                            3, user.getId()));
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

    public ResponseDTO update(CustomerRequestDTO customerRequestDTO) {
        List<FieldError> errors = new ArrayList<>();

        User user = customerRepository.findByIdAndActiveIsTrue(customerRequestDTO.getId()).orElse(null);
        if (user == null) {
            return ResponseUtils.fail(500, "Khách hàng không tồn tại", null);

        }

        // check data exits
        if (customerRepository
                .findByNumberPhoneAndActiveIsTrueAndIdNot(customerRequestDTO.getNumberPhone(),
                        customerRequestDTO.getId())
                .isPresent()) {

            errors.add(new FieldError("customerRequestDTO", "numberPhone", "Số điện thoại đã được sử dụng."));
        }

        if (customerRepository
                .findByEmailAndActiveIsTrueAndIdNot(customerRequestDTO.getEmail(), customerRequestDTO.getId())
                .isPresent()) {
            errors.add(new FieldError("customerRequestDTO", "email", "Email đã được sử dụng"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        modelMapper.map(customerRequestDTO, user);

        user.setActive(DataConst.ACTIVE_TRUE);
        user.setUpdateAt(new Date());
        user.setUpdateBy(systemService.getUserLogin());

        User userSaved = customerRepository.save(user);
        ResponseDTO accountReponse = accountService
                .update(new AccountRequestDTO(user.getNumberPhone(), customerRequestDTO.getPassword(),
                        3, user.getId()));
        if (accountReponse.isSuccess()) {
            systemService.writeSystemLog(user.getId(), userSaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);
        } else {
            return ResponseUtils.fail(accountReponse.getStatusCode(), accountReponse.getMessage(), null);

        }

    }

    public ResponseDTO updateImage(int id, MultipartFile image) {
        User user = customerRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (user == null) {
            return ResponseUtils.fail(404, "Khách hàng không tồn tại", null);
        }
        // check image
        if (image != null) {
            File file = fileService.upload(image, ImageConst.CATEGORY_FOLDER);
            if (user.getImage() != null) {
                fileService.delete(user.getImage().getId());

            }
            user.setImage(file);
            User userSaved = customerRepository.save(user);
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
        User user = customerRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (user == null) {
            return ResponseUtils.fail(500, "Khách hàng không tồn tại", null);

        }

        user.setActive(DataConst.ACTIVE_FALSE);
        user.setDeleteAt(new Date());
        user.setDeleteBy(systemService.getUserLogin());
        User userSaved = customerRepository.save(user);
        ResponseDTO accountReponse = accountService.delete(id);

        if (accountReponse.isSuccess()) {
            systemService.writeSystemLog(user.getId(), userSaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);
        } else {
            return ResponseUtils.fail(accountReponse.getStatusCode(), accountReponse.getMessage(), null);

        }

    }

    public ResponseDTO show(int active) {
        List<User> users = new ArrayList<>();

        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                users = customerRepository.findAllByRoleId(3).orElse(users);
                break;
            case RequestParamConst.ACTIVE_FALSE:
                users = customerRepository.findAllByRoleIdAndActiveIsTrue(3).orElse(users);
                break;
            case RequestParamConst.ACTIVE_TRUE:
                users = customerRepository.findAllByRoleIdAndActiveIsTrue(3).orElse(users);
                break;
            default:
                users = customerRepository.findAllByRoleId(3).orElse(users);
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

      public ResponseDTO restore(int id) {
        User user = customerRepository.findByIdAndActiveIsFalse(id).orElse(null);
        if (user == null) {
            return ResponseUtils.fail(401, "User không tồn tại", null);
        }
        user.setActive(DataConst.ACTIVE_TRUE);
        user.setUpdateAt(new Date());
        user.setUpdateBy(systemService.getUserLogin());
        User userSaved = customerRepository.save(user);
        systemService.writeSystemLog(userSaved.getId(), null, null);
        return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);
    }

    public ResponseDTO resetPassword(Integer id) {
        User user = customerRepository.findById(id).orElse(null);

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        if (user == null) {
            return ResponseUtils.fail(404, "Khách hàng không tồn tại", null);
        }
        // Tìm account đặt lại pass mặc định
        Account account = accountService.getByUserID(user.getId());
        if (account == null) {
            return ResponseUtils.fail(404, "Khách hàng chưa có tài khoản", null);
        }
        AccountRequestDTO accountDTO = modelMapper.map(account, AccountRequestDTO.class);
        account.setPassword(passwordEncoder.encode("123@123"));
        accountService.update(accountDTO);

        return ResponseUtils.success(200, "Đặt lại mật khẩu thành công!", userDTO);
    }

    public ResponseDTO detail(Integer id) {
        User user = customerRepository.findById(id).orElse(null);

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        if (user.getImage() != null) {
            String imageUrl = user.getImage().getLink();
            userDTO.setImageUrl(imageUrl);
        }

        return ResponseUtils.success(200, "Chi tiết người dùng", userDTO);
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

        int count = customerRepository.countByEmployeeCodeLike(username).orElse(0);
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
