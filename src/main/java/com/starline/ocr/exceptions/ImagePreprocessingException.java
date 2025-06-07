package com.starline.ocr.exceptions;

import java.io.IOException;

public class ImagePreprocessingException extends RuntimeException {

    public ImagePreprocessingException(String s, IOException e) {
        super(s, e);
    }
}
