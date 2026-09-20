package com.example.b1ss13.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.example.b1ss13.dto.VoucherResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VoucherService {

    private static final Logger log = LoggerFactory.getLogger(VoucherService.class);
    private static final List<VoucherResponse> DEFAULT_VOUCHERS = List.of(
            new VoucherResponse("DEFAULT_FREESHIP", "Ma Freeship 15K", 15_000));

    private final RestTemplate marketingRestTemplate;
    private final String marketingVoucherUrl;

    public VoucherService(
            RestTemplate marketingRestTemplate,
            @Value("${storex.marketing-voucher-url:http://localhost:9999/api/flash-vouchers}") String marketingVoucherUrl) {
        this.marketingRestTemplate = marketingRestTemplate;
        this.marketingVoucherUrl = marketingVoucherUrl;
    }

    @CircuitBreaker(name = "voucherCircuitBreaker", fallbackMethod = "fallbackFlashVouchers")
    public List<VoucherResponse> getFlashVouchers() {
        VoucherResponse[] response = marketingRestTemplate.getForObject(
                marketingVoucherUrl,
                VoucherResponse[].class);

        if (response == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(response);
    }

    public List<VoucherResponse> fallbackFlashVouchers(Throwable throwable) {
        log.warn("Khong lay duoc Flash Voucher tu Marketing-Service, dung voucher mac dinh. Nguyen nhan: {}",
                throwable.toString());

        return DEFAULT_VOUCHERS;
    }
}
