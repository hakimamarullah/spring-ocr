
# Spring OCR API 🚀

A minimal Spring Boot application that performs Optical Character Recognition (OCR) using **Tesseract** and **OpenCV** for image preprocessing. Built to extract text from images with enhanced accuracy via image scaling, denoising, and thresholding.

---

## 🔧 Features

- 📸 Image preprocessing with OpenCV (resize, grayscale, denoise, adaptive threshold)
- 🧠 OCR using Tesseract
- 📘 Swagger UI for easy API testing

---

## 🐳 Quick Start with Docker

Get up and running instantly using the prebuilt Docker image from Docker Hub.

```bash
docker run -p 8080:8080 hakimamarullah/starline-ocr:${TAG}
````

---

## 🔗 Access the API

Once the container is running, open your browser and go to:

📚 **Swagger UI**:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🧱 Tech Stack

* Java 21
* Spring Boot
* Tesseract OCR
* OpenCV (via nu.pattern)
* Docker
