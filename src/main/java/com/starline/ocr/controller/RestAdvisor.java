package com.starline.ocr.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class RestAdvisor {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("Unhandled exception {}", ex.getMessage(), ex);
        Map<String, Object> map = new HashMap<>();
        map.put("message", ex.getMessage());
        map.put("status", 500);
        return ResponseEntity.status(500).body(map);
    }
}
