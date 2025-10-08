package com.sample.codes.controller;

import com.razorpay.RazorpayException;
import com.sample.codes.model.FormModel;
import com.sample.codes.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("form-application")
public class FormController {
    public static final String UPLOAD_DIR = "uploads/";
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    @Autowired
    FormService formService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("File size exceeds 2MB limit");
        }

        String fileName = file.getOriginalFilename();
        if (!(fileName.endsWith(".pdf") || fileName.endsWith(".doc") || fileName.endsWith(".docx"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Only PDF, DOC, or DOCX files are allowed");
        }

        Path path = Paths.get(UPLOAD_DIR);
        if (!Files.exists(path)) Files.createDirectories(path);

        Path filePath = path.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return ResponseEntity.ok("File uploaded successfully: " + fileName);
    }

    @PostMapping()
    public FormModel submitForm(@RequestBody FormModel formModel) throws IOException, RazorpayException {
        return formService.submitForm(formModel);
    }

    @GetMapping()
    public List<FormModel> getAllForms() {
        return formService.getForms();
    }

}
