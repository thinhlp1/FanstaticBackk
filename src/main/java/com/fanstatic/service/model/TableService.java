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
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.table.TableCompactRequestDTO;
import com.fanstatic.dto.model.table.TableDTO;
import com.fanstatic.dto.model.table.TableRequestDTO;
import com.fanstatic.dto.model.table.TableTypeRequestDTO;
import com.fanstatic.model.File;
import com.fanstatic.model.Table;
import com.fanstatic.model.TableType;
import com.fanstatic.repository.TableRepository;
import com.fanstatic.repository.TableTypeRepository;
import com.fanstatic.repository.TableTypeRepository;
import com.fanstatic.service.system.FileService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;

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

    public ResponseDTO checkExits(int number) {
        Table table = tableRepository.findByNumberTableAndActiveIsTrue(number).orElse(null);
        if (table != null) {
            return ResponseUtils.fail(401, "Bàn đã tồn tại", modelMapper.map(table, TableDTO.class));
        }
        return ResponseUtils.success(200, "Bàn chưa tồn tại", null);
    }

    public ResponseDTO save(@Valid TableRequestDTO tableRequestDTO) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

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
                    tableRepository.save(table);
                }
            }

            // Xóa các bản ghi có id trong danh sách deleteTables
            if (tableRequestDTO.getDeleteTables() != null && !tableRequestDTO.getDeleteTables().isEmpty()) {
                for (Integer id : tableRequestDTO.getDeleteTables()) {
                    System.out.println("DELETE" + id);
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
                    if (table != null) {
                        if (tableRepository.findByNumberTableAndActiveIsTrue(updateTable.getNumberTable())
                                .orElse(null) == null) {
                            transactionManager.rollback(transactionStatus);

                            return ResponseUtils.fail(404, "Bàn không tồn tại", null);
                        }

                        table.setNumberTable(updateTable.getNumberTable());

                        TableType tableType = tableTypeRepository.findByIdAndActiveIsTrue(updateTable.getTableType())
                                .orElse(null);
                        if (tableType == null) {
                            transactionManager.rollback(transactionStatus);

                            return ResponseUtils.fail(404, "Loại bàn không tồn tại", null);
                        }

                        table.setNumberTable(updateTable.getNumberTable());
                        table.setUpdateAt(new Date());
                        table.setUpdateBy(systemService.getUserLogin());
                        tableRepository.save(table);
                    }
                }
            }

            // Commit giao dịch
            transactionManager.commit(transactionStatus);
            return ResponseUtils.success(200, "Lưu bàn thành công", null);
        } catch (Exception e) {
            // Nếu có lỗi, rollback giao dịch
            transactionManager.rollback(transactionStatus);
            e.printStackTrace();
            return ResponseUtils.fail(500, "Lưu bàn không thành công", null);
        }
    }

    // public ResponseDTO saveLayout(MultipartFile tableLayout) {
    //     File file = fileService.upload(tableLayout, ImageConst.TALBE_FOLDER);
        

    // }

}
