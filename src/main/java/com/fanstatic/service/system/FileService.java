package com.fanstatic.service.system;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.ImageConst;
import com.fanstatic.dto.firebase.FileUploadInfoDTO;
import com.fanstatic.model.File;
import com.fanstatic.repository.FileRepository;
import com.fanstatic.service.firebase.FirebaseStorageService;
import com.google.firebase.internal.FirebaseService;
import com.google.zxing.WriterException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FirebaseStorageService firebaseStorageService;
    private final ModelMapper modelMapper;
    private final FileRepository fileRepository;

    public File upload(MultipartFile file, String folder) {
        try {

            FileUploadInfoDTO fileUploadInfoDTO = firebaseStorageService.uploadImage(file, folder);
            String name = fileUploadInfoDTO.getImageName();
            long size = file.getSize();
            String extension = getExtensionByStringHandling(file.getName()).orElse("");
            File fileUpload = new File(DataConst.ACTIVE_TRUE, extension, fileUploadInfoDTO.getImageUrl(), name, size,
                    File.FILE_TYPE_IMAGE);

            File fileSaved = fileRepository.saveAndFlush(fileUpload);
            return fileSaved;
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
    }

    public File upload(MultipartFile file, String folder, String fileType) {
        try {

            FileUploadInfoDTO fileUploadInfoDTO = firebaseStorageService.uploadImage(file, folder);
            String name = fileUploadInfoDTO.getImageName();
            long size = file.getSize();
            String extension = getExtensionByStringHandling(file.getName()).orElse("");
            File fileUpload = new File(DataConst.ACTIVE_TRUE, extension, fileUploadInfoDTO.getImageUrl(), name, size,
                    fileType);

            File fileSaved = fileRepository.saveAndFlush(fileUpload);
            return fileSaved;
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
    }

    public File updateFile(MultipartFile file, String folder, File fileUpdate) {
        try {

            FileUploadInfoDTO fileUploadInfoDTO = firebaseStorageService.uploadImage(file, folder);
            String name = fileUploadInfoDTO.getImageName();
            long size = file.getSize();
            String extension = getExtensionByStringHandling(file.getName()).orElse("");

            fileUpdate.setExtension(extension);
            fileUpdate.setLink(fileUploadInfoDTO.getImageUrl());
            fileUpdate.setSize(size);
            fileUpdate.setName(name);

            File fileSaved = fileRepository.saveAndFlush(fileUpdate);
            return fileSaved;
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
    }

    public void deleteFireStore(String name) {
        firebaseStorageService.removeRelativeFileImage(name);
    }


    public void delete(int id) {
        File file = fileRepository.findById(id).orElse(null);
        if (file != null) {
            String name = file.getName();
            firebaseStorageService.removeRelativeFileImage(name);
            fileRepository.deleteById(id);

        }

    }

    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

}
