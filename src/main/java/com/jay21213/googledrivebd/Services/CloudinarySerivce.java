package com.jay21213.googledrivebd.Services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

import com.cloudinary.Transformation;

@Slf4j
@Service
public class CloudinarySerivce {
    @Value("${cloudinary.cloud-name}")
    private String cloudName;
    private Cloudinary cloudinary;

    @Autowired
    public CloudinarySerivce(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    private String humanReadableSize(long bytes) {
        double kb = bytes / 1024.0;
        double mb = kb / 1024.0;
        double gb = mb / 1024.0;

        if (gb >= 1)
            return String.format("%.2f GB", gb);
        else if (mb >= 1)
            return String.format("%.2f MB", mb);
        else if (kb >= 1)
            return String.format("%.2f KB", kb);
        else
            return bytes + " B";
    }

    public String uploadImageFromUrl(String imageUrl) {
        try {
            Map uploadResult = cloudinary.uploader().upload(imageUrl, ObjectUtils.asMap(
                    "folder", "user-avatars",  // Optional: set folder
                    "overwrite", true
            ));
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // or throw custom exception
        }
    }

        public Map<String, String> uploadFile(MultipartFile file) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto") // auto-detect type
                );

                return Map.of(
                        "public_id", uploadResult.get("public_id").toString(),
                        "secure_url", uploadResult.get("secure_url").toString(),
                        "resource_type", uploadResult.get("resource_type").toString(),
                        "size",  uploadResult.get("bytes").toString()

                );
            } catch (IOException e) {
                throw new RuntimeException("Error uploading file to Cloudinary", e);
            }
        }

        public void deleteFile(String publicId, String resourceType) {
            try {
                cloudinary.uploader().destroy(
                        publicId,
                        ObjectUtils.asMap("resource_type", resourceType)
                );
            } catch (Exception e) {
                log.error("Error deleting file from Cloudinary", e);
                throw new RuntimeException("Error deleting file from Cloudinary", e);
            }
        }


}