package com.starline.ocr.controller;

import com.starline.ocr.models.dto.BaseResponse;
import com.starline.ocr.models.dto.ImageToTextRequest;
import com.starline.ocr.service.ImagePreprocessingService;
import com.starline.ocr.service.TesseractService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

@RestController
@RequestMapping("/ocr")
@RequiredArgsConstructor
public class TesseractController {

    private final TesseractService tesseractService;


    private final BeanFactory beanFactory;

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<String> imageToText(@ModelAttribute ImageToTextRequest body) throws IOException, TesseractException {
       return  BaseResponse.<String>builder()
               .code("200")
               .message("Success")
               .data(tesseractService.imageToText(body.getFileInputStream(), body.getVersion())).build();
    }

    @PostMapping(value = "/preprocess", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void preprocessImage(@RequestParam(value = "version", required = false) String version, @RequestPart("file") MultipartFile file, HttpServletResponse response) throws IOException {
        BufferedImage input = ImageIO.read(file.getInputStream());


        BufferedImage processed = getImagePreprocessingService(version).preprocess(input);

        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(processed, "png", os);
        os.flush();
        os.close();
    }

    private ImagePreprocessingService getImagePreprocessingService(String version) {
        version = Optional.ofNullable(version).orElse("preprocessingV2");
        return (ImagePreprocessingService) beanFactory.getBean(version);
    }

}
