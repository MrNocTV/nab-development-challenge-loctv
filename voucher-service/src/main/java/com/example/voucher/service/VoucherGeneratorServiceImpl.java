package com.example.voucher.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VoucherGeneratorServiceImpl implements VoucherGeneratorService {
    @Override
    public String generateVoucher() {
        return UUID.randomUUID().toString();
    }
}
