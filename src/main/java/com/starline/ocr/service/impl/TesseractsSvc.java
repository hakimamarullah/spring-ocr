package com.starline.ocr.service.impl;

import com.starline.ocr.config.TesseractFactory;
import com.starline.ocr.service.ImagePreprocessingService;
import com.starline.ocr.service.TesseractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TesseractsSvc implements TesseractService {

    private final TesseractFactory tesseractFactory;


    private final BeanFactory beanFactory;

    @Override
    public String imageToText(InputStream in, String version) throws IOException, TesseractException {
        log.info("Converting preprocessedImage to text");
        ImagePreprocessingService imagePreprocessor = getImagePreprocessor(version);
        Tesseract tesseract = tesseractFactory.getTesseract();
        if (Objects.isNull(imagePreprocessor)) {
            log.info("Skip Preprocessing");
            return tesseract.doOCR(ImageIO.read(in));
        }

        String result = tesseract.doOCR(imagePreprocessor.preprocess(ImageIO.read(in)));
        log.info("Finished converting preprocessedImage to text");
        return result;
    }

    private ImagePreprocessingService getImagePreprocessor(String version) {
        Map<String, String> versions = Map.of("v1", "preprocessingV1", "v2", "preprocessingV2");
        version = versions.get(Optional.ofNullable(version).orElse(""));
        if (Objects.isNull(version)) {
            return null;
        }
        return (ImagePreprocessingService) beanFactory.getBean(version);
    }

}
