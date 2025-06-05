package com.starline.ocr.config;


import lombok.NonNull;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Component;

@Component
public class TesseractFactory {

    private final ThreadLocal<Tesseract> tesseractCache = ThreadLocal.withInitial(() -> {
        Tesseract tess = new Tesseract();
        tess.setDatapath("src/main/resources/tessdata");
        tess.setVariable("user_defined_dpi", "300");

        tess.setLanguage("ind");
        return tess;
    });

    @NonNull
    public Tesseract getTesseract() {
        return tesseractCache.get();
    }

    public void clear() {
        tesseractCache.remove();
    }
}

