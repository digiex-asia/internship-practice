package com.demo.services;

import com.demo.controllers.model.response.StudentResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface FileService {
     ByteArrayInputStream createTemplateStudent() throws IOException;

    ByteArrayInputStream readFile(MultipartFile file) throws IOException, ParseException;

     ByteArrayInputStream exportExcelAllStudent() throws IOException;
    ByteArrayInputStream createFileError(List<String> errors) throws IOException;
}
