package com.starline.ocr.service;

import java.awt.image.BufferedImage;

public interface ImagePreprocessingService {

    BufferedImage preprocess(BufferedImage input);
}
