package com.starline.ocr.service.impl;

import com.starline.ocr.service.ImagePreprocessingService;
import com.starline.ocr.service.ImageUtilityService;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service("preprocessingV1")
public class ImageUtilitySvc implements ImageUtilityService, ImagePreprocessingService {

    @Override
    public BufferedImage preprocess(BufferedImage input) {
        BufferedImage upscale = upscale(input, 2.0);
        BufferedImage gray = toGrayscale(upscale);
        return binarize(gray);
    }

    @Override
    public BufferedImage upscale(BufferedImage input, double scale) {
        int newWidth = (int) (input.getWidth() * scale);
        int newHeight = (int) (input.getHeight() * scale);
        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(input, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return resized;
    }

    @Override
    public BufferedImage toGrayscale(BufferedImage input) {
        BufferedImage gray = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = gray.createGraphics();
        g.drawImage(input, 0, 0, null);
        g.dispose();
        return gray;
    }

    @Override
    public BufferedImage binarize(BufferedImage input) {
        BufferedImage binary = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g = binary.createGraphics();
        g.drawImage(input, 0, 0, null);
        g.dispose();
        return binary;
    }
}
