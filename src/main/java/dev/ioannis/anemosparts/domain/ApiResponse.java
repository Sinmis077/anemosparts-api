package dev.ioannis.anemosparts.domain;

import lombok.Builder;

@Builder
public class ApiResponse<T> {
    private Boolean success;
    private String message;
    private T data;
}
