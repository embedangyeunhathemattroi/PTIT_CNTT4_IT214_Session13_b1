package com.example.b1ss13.dto;

public record VoucherResponse(
        String code,
        String title,
        int discountAmount) {
}
