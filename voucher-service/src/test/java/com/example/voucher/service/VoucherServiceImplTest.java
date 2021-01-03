package com.example.voucher.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class VoucherServiceImplTest {

    @Autowired
    private VoucherGeneratorService voucherGeneratorService;

    @Test
    public void it_should_return_uuid_when_no_exception_happens() {
        // @given there is no exception
        // @when calling generateVoucher method
        String result = voucherGeneratorService.generateVoucher();
        // @then it should return a UUID
        assertTrue(isUUIDString(result));
    }

    private boolean isUUIDString(String s) {
        if (s == null)
            return false;
        return s.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
    }

}
