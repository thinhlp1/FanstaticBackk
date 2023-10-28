package com.fanstatic.service.system;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.dto.firebase.FileUploadInfoDTO;
import com.fanstatic.model.File;
import com.fanstatic.repository.FileRepository;
import com.fanstatic.service.firebase.FirebaseStorageService;
import com.google.firebase.internal.FirebaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FirebaseStorageService firebaseStorageService;

    private final FileRepository fileRepository;

    public File upload(MultipartFile file, String folder) {
        try {

            FileUploadInfoDTO fileUploadInfoDTO = firebaseStorageService.uploadImage(file, folder);
            String name = fileUploadInfoDTO.getImageName();
            long size = file.getSize();
            String extension = getExtensionByStringHandling(file.getName()).orElse("");
            File fileUpload = new File(DataConst.ACTIVE_TRUE, extension, fileUploadInfoDTO.getImageUrl(), name, size);

            File fileSaved = fileRepository.saveAndFlush(fileUpload);
            return fileSaved;
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
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
