package com.starline.ocr.service;

import java.awt.image.BufferedImage;

public interface ImageUtilityService {

    BufferedImage upscale(BufferedImage input, double scale);

    BufferedImage toGrayscale(BufferedImage input);

    BufferedImage binarize(BufferedImage input);
}
