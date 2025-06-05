package com.starline.ocr.service;

import net.sourceforge.tess4j.TesseractException;

import java.io.IOException;
import java.io.InputStream;

public interface TesseractService {

    String imageToText(InputStream in, String imageProcessorVersion) throws IOException, TesseractException;
}
