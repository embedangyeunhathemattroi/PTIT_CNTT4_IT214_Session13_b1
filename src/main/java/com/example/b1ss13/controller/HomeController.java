package com.example.b1ss13.controller;

import java.util.List;

import com.example.b1ss13.dto.VoucherResponse;
import com.example.b1ss13.service.VoucherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final VoucherService voucherService;

    public HomeController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping("/home/vouchers")
    List<VoucherResponse> getHomeVouchers() {
        return voucherService.getFlashVouchers();
    }
}
