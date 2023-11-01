package com.fanstatic.service.system;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.ImageConst;
import com.fanstatic.dto.firebase.FileUploadInfoDTO;
import com.fanstatic.model.File;
import com.fanstatic.model.QrCode;
import com.fanstatic.repository.FileRepository;
import com.fanstatic.repository.QrCodeRepository;
import com.fanstatic.service.firebase.FirebaseStorageService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    private static final int HIGHT = 200;
    private static final int WIDTH = 200;
    private static final int MARGIN = 2;
    private static final String FORMAT = "png";

    @Autowired
    @Lazy
    private final FileService fileService;
    private final FirebaseStorageService firebaseStorageService;
    private final FileRepository fileRepository;
    private final QrCodeRepository qrCodeRepository;
    private final SystemService systemService;

    public QrCode saveQrCodeTable(String content, String nameQr) {
        try {

            Path pathToFile = createQR(content , nameQr);

            FileUploadInfoDTO fileUploadInfoDTO = firebaseStorageService.uploadImage(pathToFile,
                    ImageConst.TALBE_FOLDER);
            String name = fileUploadInfoDTO.getImageName();
            String extension = FORMAT;
            File fileUpload = new File(DataConst.ACTIVE_TRUE, extension, fileUploadInfoDTO.getImageUrl(), name, 0);

            File fileSaved = fileRepository.saveAndFlush(fileUpload);

            QrCode qrCode = new QrCode();
            qrCode.setActive(DataConst.ACTIVE_TRUE);
            qrCode.setCreateAt(new Date());
            qrCode.setCreateBy(systemService.getUserLogin());

            qrCode.setImage(fileSaved);
            qrCode.setContent("QR Code bàn " + nameQr);

            QrCode qrCodeSaved = qrCodeRepository.saveAndFlush(qrCode);

            Files.delete(pathToFile);


            return qrCodeSaved;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    public Path createQR(String data, String nameQr)
            throws WriterException, IOException {

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, MARGIN);

        BitMatrix matrix = new MultiFormatWriter().encode(
                data,
                BarcodeFormat.QR_CODE, WIDTH, HIGHT, hints);

        Path pathToDirectory = Paths.get("src", "main", "resources", "image-qr");

        Path pathToFile = pathToDirectory.resolve(nameQr + "." + FORMAT);

        MatrixToImageWriter.writeToPath(matrix, FORMAT, pathToFile);
        return pathToFile;

    }

}
