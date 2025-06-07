package com.starline.ocr.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Data
public class ImageToTextRequest {

    @Schema(
            description = "Preprocessing version",
            allowableValues = {"v1", "v2", "none"},
            defaultValue = "none"
    )
    private String version;
    private MultipartFile file;


    @JsonIgnore
    public InputStream getFileInputStream() throws IOException {
        return file.getInputStream();
    }
}
