package com.example.prepaiddata.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "voucher-service")
public interface VoucherServiceClient {

    @GetMapping(value = "/api/voucher")
    String getVoucher(@RequestParam(name = "delay", required = false) Integer delay);
}
