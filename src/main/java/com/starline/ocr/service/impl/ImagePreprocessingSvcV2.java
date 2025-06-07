package com.starline.ocr.service.impl;

import com.starline.ocr.exceptions.ImagePreprocessingException;
import com.starline.ocr.service.ImagePreprocessingService;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service("preprocessingV2")
@Primary
@Slf4j
public class ImagePreprocessingSvcV2 implements ImagePreprocessingService {

    static {

        org.bytedeco.javacpp.Loader.load(org.bytedeco.opencv.opencv_java.class);
    }

    @Override
    public BufferedImage preprocess(BufferedImage input) {
        log.info("Starting OpenCV preprocessing");
        Mat mat = bufferedImageToMat(input);

        mat = upscale(mat, 1.5);                      // DPI boost (1.5–2.0x recommended)
        mat = toGrayscale(mat);                       // Convert to grayscale
        mat = cleanNoise(mat);                        // Denoising with dilation/erosion
        mat = enhanceContrast(mat);                   // Optional: histogram equalization
        mat = sharpen(mat);                           // Unsharp masking
        mat = binarizeWithOtsu(mat);                 // Binarization

        log.info("Finished preprocessing");
        return matToBufferedImage(mat);
    }

    private Mat upscale(Mat mat, double scale) {
        Mat resized = new Mat();
        Size newSize = new Size(mat.width() * scale, mat.height() * scale);
        Imgproc.resize(mat, resized, newSize, 0, 0, Imgproc.INTER_CUBIC);
        return resized;
    }

    private Mat toGrayscale(Mat mat) {
        Mat gray = new Mat();
        Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
        return gray;
    }

    private Mat cleanNoise(Mat mat) {
        Mat denoised = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, 1));
        Imgproc.dilate(mat, denoised, kernel);
        Imgproc.erode(denoised, denoised, kernel);
        Imgproc.GaussianBlur(denoised, denoised, new Size(3, 3), 0);
        return denoised;
    }

    private Mat enhanceContrast(Mat mat) {
        Mat equalized = new Mat();
        Imgproc.equalizeHist(mat, equalized);
        return equalized;
    }

    private Mat sharpen(Mat mat) {
        Mat sharpened = new Mat();
        Mat kernel = new Mat(3, 3, CvType.CV_32F);
        kernel.put(0, 0,
                0, -1, 0,
                -1, 5, -1,
                0, -1, 0);
        Imgproc.filter2D(mat, sharpened, mat.depth(), kernel);
        return sharpened;
    }

    private Mat adaptiveThreshold(Mat mat) {
        Mat binary = new Mat();
        Imgproc.adaptiveThreshold(
                mat,
                binary,
                255,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY,
                31, // blockSize (must be odd)
                2   // constant subtracted from mean
        );
        return binary;
    }

    // --- Conversion Methods ---
    private Mat bufferedImageToMat(BufferedImage bi) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
            return Imgcodecs.imdecode(new MatOfByte(baos.toByteArray()), Imgcodecs.IMREAD_COLOR);
        } catch (IOException e) {
            throw new ImagePreprocessingException("Failed to convert BufferedImage to Mat", e);
        }
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".png", mat, mob);
        try {
            return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
        } catch (IOException e) {
            throw new ImagePreprocessingException("Failed to convert Mat to BufferedImage", e);
        }
    }

    private Mat binarizeWithOtsu(Mat gray) {
        Mat binary = new Mat();
        Imgproc.threshold(
                gray,
                binary,
                0,
                255,
                Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU
        );
        return binary;
    }

}
