package com.fanstatic.service.model;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.ImageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.firebase.FileUploadInfoDTO;
import com.fanstatic.dto.model.table.TableCompactRequestDTO;
import com.fanstatic.dto.model.table.TableDTO;
import com.fanstatic.dto.model.table.TableRequestDTO;
import com.fanstatic.dto.model.table.TableTypeDTO;
import com.fanstatic.model.File;
import com.fanstatic.model.QrCode;
import com.fanstatic.model.Table;
import com.fanstatic.model.TableType;
import com.fanstatic.repository.FileRepository;
import com.fanstatic.repository.TableRepository;
import com.fanstatic.repository.TableTypeRepository;
import com.fanstatic.service.firebase.FirebaseStorageService;
import com.fanstatic.service.system.FileService;
import com.fanstatic.service.system.QRCodeService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import com.google.zxing.WriterException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TableService {
    private final ModelMapper modelMapper;
    private final TableRepository tableRepository;
    private final TableTypeRepository tableTypeRepository;
    private final SystemService systemService;
    private final FileService fileService;
    private final PlatformTransactionManager transactionManager;
    private final QRCodeService qrCodeService;
    private final FirebaseStorageService firebaseStorageService;
    private final FileRepository fileRepository;

    public ResponseDTO checkExits(int number) {
        Table table = tableRepository.findByNumberTableAndActiveIsTrue(number).orElse(null);
        if (table != null) {
            return ResponseUtils.fail(401, "Bàn đã tồn tại", modelMapper.map(table, TableDTO.class));
        }
        return ResponseUtils.success(200, "Bàn chưa tồn tại", null);
    }

    public ResponseDTO save(@Valid TableRequestDTO tableRequestDTO) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        List<Table> tableSaveds = new ArrayList<>();
        try {
            // Lưu các giá trị mới
            if (tableRequestDTO.getNewTables() != null && !tableRequestDTO.getNewTables().isEmpty()) {
                for (TableCompactRequestDTO newTable : tableRequestDTO.getNewTables()) {
                    Table table = new Table();

                    if (tableRepository.findByNumberTableAndActiveIsTrue(newTable.getNumberTable())
                            .orElse(null) != null) {
                        transactionManager.rollback(transactionStatus);

                        return ResponseUtils.fail(404, "Bàn đã tồn tại", null);
                    }

                    table.setNumberTable(newTable.getNumberTable());

                    TableType tableType = tableTypeRepository.findByIdAndActiveIsTrue(newTable.getTableType())
                            .orElse(null);
                    if (tableType == null) {
                        transactionManager.rollback(transactionStatus);

                        return ResponseUtils.fail(404, "Loại bàn không tồn tại", null);
                    }

                    table.setNumberTable(newTable.getNumberTable());
                    table.setTableType(tableType);
                    table.setActive(DataConst.ACTIVE_TRUE);
                    table.setCreateAt(new Date());
                    table.setCreateBy(systemService.getUserLogin());

                    // Lưu đối tượng table mới
                    Table tableSaved = tableRepository.saveAndFlush(table);
                    tableSaveds.add(tableSaved);
                }

            }

            // Xóa các bản ghi có id trong danh sách deleteTables
            if (tableRequestDTO.getDeleteTables() != null && !tableRequestDTO.getDeleteTables().isEmpty()) {
                for (Integer id : tableRequestDTO.getDeleteTables()) {
                    Table table = tableRepository.findByIdAndActiveIsTrue(id).orElse(null);
                    if (table != null) {

                        table.setActive(DataConst.ACTIVE_FALSE);
                        table.setDeleteAt(new Date());
                        table.setDeleteBy(systemService.getUserLogin());
                        tableRepository.save(table);
                    }
                }
            }

            // Cập nhật các giá trị
            if (tableRequestDTO.getUpdateTables() != null && !tableRequestDTO.getUpdateTables().isEmpty()) {
                for (TableCompactRequestDTO updateTable : tableRequestDTO.getUpdateTables()) {
                    Table table = tableRepository.findByIdAndActiveIsTrue(updateTable.getId()).orElse(null);

                    if (table == null) {
                        transactionManager.rollback(transactionStatus);

                        return ResponseUtils.fail(404, "Bàn không tồn tại", null);
                    }

                    TableType tableType = tableTypeRepository.findByIdAndActiveIsTrue(updateTable.getTableType())
                            .orElse(null);
                    if (tableType == null) {
                        transactionManager.rollback(transactionStatus);

                        return ResponseUtils.fail(404, "Loại bàn không tồn tại", null);
                    }

                    table.setNumberTable(updateTable.getNumberTable());

                    table.setTableType(tableType);
                    table.setUpdateAt(new Date());
                    table.setUpdateBy(systemService.getUserLogin());
                    tableRepository.save(table);

                }
            }

            // create QR code for save table
            for (Table table : tableSaveds) {
                QrCode qrCode = qrCodeService.saveQrCodeTable(ApplicationConst.CLIENT_HOST + "?table=" + table.getId(),
                        String.valueOf(table.getId()));
                table.setQrCode(qrCode);
                tableRepository.save(table);
            }

            // convert table to tableDTO
            List<ResponseDataDTO> tableDTOs = new ArrayList<>();
            for (Table table : tableSaveds) {
                TableDTO tableDTO = modelMapper.map(table, TableDTO.class);
                String qrCodeUrl = table.getQrCode().getImage().getLink();
                TableTypeDTO tableTypeDTO = modelMapper.map(table.getTableType(), TableTypeDTO.class);
                File file = table.getTableType().getImage();
                if (file != null) {
                    tableTypeDTO.setImageUrl(file.getLink());

                }
                tableDTO.setQrImageUrl(qrCodeUrl);
                tableDTO.setTableTypeDTO(tableTypeDTO);
                tableDTOs.add(tableDTO);
            }

            ResponseListDataDTO responseListDataDTO = new ResponseListDataDTO();
            responseListDataDTO.setDatas(tableDTOs);
            responseListDataDTO.setNameList("Danh sách bàn mới thêm");

            // Commit giao dịch
            transactionManager.commit(transactionStatus);
            return ResponseUtils.success(200, "Lưu bàn thành công", responseListDataDTO);
        } catch (Exception e) {
            // Nếu có lỗi, rollback giao dịch
            transactionManager.rollback(transactionStatus);
            e.printStackTrace();
            return ResponseUtils.fail(500, "Lưu bàn không thành công", null);
        }
    }

    public ResponseDTO show() {
        List<Table> tables = new ArrayList<>();
        int active = RequestParamConst.ACTIVE_TRUE;
        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                tables = tableRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_TRUE:
                tables = tableRepository.findAllByActiveIsTrue().orElse(tables);
                break;
            case RequestParamConst.ACTIVE_FALSE:
                tables = tableRepository.findAllByActiveIsFalse().orElse(tables);
                break;
            default:
                tables = tableRepository.findAll();
                break;
        }
        List<ResponseDataDTO> tableDTOS = new ArrayList<>();

        for (Table table : tables) {
            TableDTO tableDTO = new TableDTO();
            modelMapper.map(table, tableDTO);
            String qrCodeUrl = table.getQrCode().getImage().getLink();

            TableTypeDTO tableTypeDTO = modelMapper.map(table.getTableType(), TableTypeDTO.class);
            File file = table.getTableType().getImage();
            if (file != null) {
                tableTypeDTO.setImageUrl(file.getLink());

            }

            tableDTO.setQrImageUrl(qrCodeUrl);
            tableDTO.setTableTypeDTO(tableTypeDTO);
            tableDTOS.add(tableDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(tableDTOS);
        return ResponseUtils.success(200, "Danh sách bàn", reponseListDataDTO);
    }

    public ResponseDTO saveLayout(MultipartFile tableLayout) {

        File file = fileRepository.findByFileTypeAndActiveIsTrue(File.FILE_TYPE_TABLE_LAYOUT).orNull();
        File fileSaved;
        if (file == null) {
            fileSaved = fileService.upload(tableLayout, ImageConst.TALBE_LAYOUT_FOLDER,
                    File.FILE_TYPE_TABLE_LAYOUT);
        } else {
            fileService.deleteFireStore(file.getName());

            fileSaved = fileService.updateFile(tableLayout, ImageConst.TALBE_LAYOUT_FOLDER,
                    file);
        }

        if (fileSaved != null) {
            return ResponseUtils.success(200, "Upload layout bàn thành công", null);
        }

        return ResponseUtils.fail(500, "Upload layout không thành công", null);

    }

    public ResponseDTO showTableLayout() {
        File file = fileRepository.findByFileTypeAndActiveIsTrue(File.FILE_TYPE_TABLE_LAYOUT).orNull();
        if (file != null) {
            FileUploadInfoDTO fileUploadInfoDTO = new FileUploadInfoDTO();
            fileUploadInfoDTO.setImageName(file.getName());
            fileUploadInfoDTO.setImageUrl(file.getLink());
            return ResponseUtils.success(200, "Layout table", fileUploadInfoDTO);
        }
        return ResponseUtils.fail(404, "Không tìm thấy layout table", null);

    }

    public ResponseDTO qrTable() {
        try {
            Path pathToFile = qrCodeService.createQR(ApplicationConst.CLIENT_HOST, "table1");
            FileUploadInfoDTO fileUploadInfoDTO = firebaseStorageService.uploadImage(pathToFile,
                    ImageConst.TALBE_FOLDER);
            return ResponseUtils.success(200, "QR thành công", fileUploadInfoDTO);
        } catch (WriterException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseUtils.fail(500, "Không thành công", null);
    }

}
