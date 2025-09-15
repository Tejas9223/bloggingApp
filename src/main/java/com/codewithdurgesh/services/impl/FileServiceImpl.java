package com.codewithdurgesh.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.codewithdurgesh.services.FileService;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws Exception {
        // 1. Get original filename
        String originalFilename = file.getOriginalFilename();

        // 2. Generate a random unique filename
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId + "_" + originalFilename;

        // 3. Create full path
        String filePath = path + File.separator + fileName;

        // 4. Create folder if not exists
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 5. Save file
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath = path + File.separator + fileName;
        InputStream inputStream = new FileInputStream(fullPath);

        return inputStream;
    }
}
