package com.fanstatic.service.firebase;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;

@Service
public class FirebaseStorageService {

    @Value("${firebase.storage.bucket}")
    private String storageBucket;

    public String uploadImage(MultipartFile file, String folder) throws IOException {
        String imageName = folder + "/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-admin-sdk.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(storageBucket)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        BlobInfo blobInfo = StorageClient.getInstance().bucket(storageBucket)
                .create(imageName, file.getBytes(), file.getContentType());

        return blobInfo.getMediaLink();
    }

    public String removeRelativeFileImage(String imageName) {
        try {
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-admin-sdk.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(storageBucket)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            Bucket bucket = StorageClient.getInstance().bucket(storageBucket);
            System.out.println("Bucket: " + bucket.getName());
            Page<Blob> blobs = bucket.list();
            for (Blob blob : blobs.iterateAll()) {
                System.out.println(blob.getName());
                if (blob.getName().contains(imageName)) {
                    blob.delete();
                    return "success";
                }
            }
        } catch (Exception e) {
            return "fail";
        }
             
        return "Not found";
    }
    public String removeAbsoluteFileImage(String folder, String imageName) {
        try {
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-admin-sdk.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(storageBucket)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            Bucket bucket = StorageClient.getInstance().bucket(storageBucket);
            System.out.println("Bucket: " + bucket.getName());
            Page<Blob> blobs = bucket.list();
            for (Blob blob : blobs.iterateAll()) {
                System.out.println(blob.getName());
                if (blob.getName().equals(folder + "/" + imageName)) {
                    blob.delete();
                    return "success";
                }
            }
        } catch (Exception e) {
            return "fail";
        }
             
        return "Not found";
    }
    public void clearAll() {
        try {
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-admin-sdk.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(storageBucket)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            Bucket bucket = StorageClient.getInstance().bucket(storageBucket);

            Page<Blob> blobs = bucket.list();
            for (Blob blob : blobs.iterateAll()) {
                blob.delete();
            }
        } catch (Exception e) {
        }
    }

}
