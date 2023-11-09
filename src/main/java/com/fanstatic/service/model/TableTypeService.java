package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
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
import com.fanstatic.dto.model.table.TableTypeDTO;
import com.fanstatic.dto.model.table.TableTypeRequestDTO;
import com.fanstatic.model.File;
import com.fanstatic.model.TableType;
import com.fanstatic.repository.TableTypeRepository;
import com.fanstatic.service.system.FileService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TableTypeService {
    private final ModelMapper modelMapper;
    private final TableTypeRepository tableTypeRepository;
    private final SystemService systemService;
    private final FileService fileService;

    public ResponseDTO create(TableTypeRequestDTO tableTypeRequestDTO) {

        List<FieldError> errors = new ArrayList<>();
        if (tableTypeRepository.findByCodeAndActiveIsTrue(tableTypeRequestDTO.getCode()).isPresent()) {
            errors.add(new FieldError("tableTypeRequestDTO", "code", "Code đã tồn tại"));
        }

        if (tableTypeRepository.findByNameAndActiveIsTrue(tableTypeRequestDTO.getName()).isPresent()) {
            errors.add(new FieldError("tableTypeRequestDTO", "name", "TableType đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        TableType tableType = modelMapper.map(tableTypeRequestDTO, TableType.class);

        tableType.setActive(DataConst.ACTIVE_TRUE);
        tableType.setCreateAt(new Date());
        tableType.setCreateBy(systemService.getUserLogin());

        MultipartFile image = tableTypeRequestDTO.getImage();
        if (image != null) {
            File file = fileService.upload(image, ImageConst.TALBE_FOLDER);
            tableType.setImage(file);
            // save image to Fisebase and file table
        }

        TableType tableTypeSaved = tableTypeRepository.saveAndFlush(tableType);

        if (tableTypeSaved != null) {

            systemService.writeSystemLog(tableTypeSaved.getId(), tableTypeSaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, getDTOById(tableTypeSaved.getId()));

        }
        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);

    }

    public ResponseDTO update(TableTypeRequestDTO tableTypeRequestDTO) {

        TableType tableType = tableTypeRepository.findByIdAndActiveIsTrue(tableTypeRequestDTO.getId()).orElse(null);
        if (tableType == null) {
            return ResponseUtils.fail(401, "TableType không tồn tại", null);
        }

        List<FieldError> errors = new ArrayList<>();
        if (tableTypeRepository
                .findByCodeAndActiveIsTrueAndIdNot(tableTypeRequestDTO.getCode(), tableTypeRequestDTO.getId())
                .isPresent()) {
            errors.add(new FieldError("tableTypeRequestDTO", "code", "Code đã tồn tại"));
        }

        if (tableTypeRepository
                .findByNameAndActiveIsTrueAndIdNot(tableTypeRequestDTO.getName(), tableTypeRequestDTO.getId())
                .isPresent()) {
            errors.add(new FieldError("tableTypeRequestDTO", "name", "TableType đã tồn tại"));
        }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        modelMapper.map(tableTypeRequestDTO, tableType);

        tableType.setActive(DataConst.ACTIVE_TRUE);
        tableType.setUpdateAt(new Date());
        tableType.setUpdateBy(systemService.getUserLogin());

        TableType tableTypeSaved = tableTypeRepository.saveAndFlush(tableType);

        if (tableTypeSaved != null) {

            systemService.writeSystemLog(tableTypeSaved.getId(), tableTypeSaved.getName(), null);
            return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, getDTOById(tableTypeSaved.getId()));

        }
        return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);

    }

    public ResponseDTO updateImage(int id, MultipartFile image) {
        TableType tableType = tableTypeRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (tableType == null) {
            return ResponseUtils.fail(404, "Table type không tồn tại", null);
        }
        // check image
        if (image != null) {
            if (tableType.getImage() != null) {
                fileService.deleteFireStore(tableType.getImage().getName());

                fileService.updateFile(image, ImageConst.TALBE_FOLDER, tableType.getImage());

            } else {
                File file = fileService.upload(image, ImageConst.TALBE_FOLDER);
                tableType.setImage(file);

            }
            TableType tableTypeSaved = tableTypeRepository.save(tableType);
            if (tableTypeSaved != null) {

                systemService.writeSystemLog(tableTypeSaved.getId(), tableTypeSaved.getName(), null);
                return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, getDTOById(id));

            } else {
                return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);

            }

        }
        return ResponseUtils.fail(200, "Uploadimage", null);

    }

    public ResponseDTO delete(int id) {

        TableType tableType = tableTypeRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (tableType == null) {
            return ResponseUtils.fail(401, "Table type không tồn tại", null);
        }

        tableType.setActive(DataConst.ACTIVE_FALSE);
        tableType.setDeleteAt(new Date());
        tableType.setDeleteBy(systemService.getUserLogin());

        TableType tableTypeSaved = tableTypeRepository.save(tableType);

        if (tableTypeSaved != null) {
            systemService.writeSystemLog(tableType.getId(), tableType.getName(), null);
            return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);

        }
        return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);

    }

    public ResponseDTO show() {
        List<TableType> tableTypes = new ArrayList<>();
        int active = RequestParamConst.ACTIVE_TRUE;
        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                tableTypes = tableTypeRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_TRUE:
                tableTypes = tableTypeRepository.findAllByActiveIsTrue().orElse(tableTypes);
                break;
            case RequestParamConst.ACTIVE_FALSE:
                tableTypes = tableTypeRepository.findAllByActiveIsFalse().orElse(tableTypes);
                break;
            default:
                tableTypes = tableTypeRepository.findAll();
                break;
        }
        List<ResponseDataDTO> tableTypeDTOS = new ArrayList<>();

        for (TableType tableType : tableTypes) {
            TableTypeDTO tableTypeDTO = new TableTypeDTO();
            modelMapper.map(tableType, tableTypeDTO);
            File image = tableType.getImage();
            if (image != null) {
                tableTypeDTO.setImageUrl(image.getLink());
            }
            tableTypeDTOS.add(tableTypeDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(tableTypeDTOS);
        return ResponseUtils.success(200, "Danh sách loại bàn", reponseListDataDTO);
    }

    public TableTypeDTO getDTOById(int id) {
        TableType tableType = tableTypeRepository.findById(id).orElse(null);
        if (tableType != null) {
            TableTypeDTO tableTypeDTO = modelMapper.map(tableType, TableTypeDTO.class);
            File image = tableType.getImage();
            if (image != null) {
                tableTypeDTO.setImageUrl(image.getLink());
            }
            return tableTypeDTO;
        } else {
            return null;
        }
    }
}
