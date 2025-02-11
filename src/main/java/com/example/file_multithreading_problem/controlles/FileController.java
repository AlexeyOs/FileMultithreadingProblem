package com.example.file_multithreading_problem.controlles;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping
public class FileController {

    @PostMapping(value = "/upload-from-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveFromFiles(@RequestParam String type,
                                              @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
