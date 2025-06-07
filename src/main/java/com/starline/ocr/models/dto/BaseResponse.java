package com.starline.ocr.models.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "data"})
public class BaseResponse<T> {

    private String code;
    private String message;
    private T data;
}
