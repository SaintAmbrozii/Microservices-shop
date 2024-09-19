package com.example.productservice.exceptionhandler;

import java.util.Map;

public record ErrorResponse(Map<String, String> errors) {
}
