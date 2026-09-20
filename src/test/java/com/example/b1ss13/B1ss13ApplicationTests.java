package com.example.b1ss13;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.example.b1ss13.dto.VoucherResponse;
import com.example.b1ss13.service.VoucherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class B1ss13ApplicationTests {

    @Autowired
    private VoucherService voucherService;

    @Test
    void contextLoads() {
    }

    @Test
    void fallbackReturnsDefaultVoucher() {
        List<VoucherResponse> vouchers = voucherService.fallbackFlashVouchers(new RuntimeException("Marketing down"));

        assertThat(vouchers).hasSize(1);
        assertThat(vouchers.getFirst().code()).isEqualTo("DEFAULT_FREESHIP");
    }
}
