package com.example.prepaiddata.client;

import com.example.prepaiddata.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.NotEmpty;

@FeignClient(name = "account-service")
public interface AccountServiceClient {

    @GetMapping(value = "/api/account/{phone-number}")
    ResponseEntity<AccountDTO> getAccountByPhoneNumber(@PathVariable(name = "phone-number") @NotEmpty String phoneNumber);
}
