package com.example.account.controller;

import com.example.account.dto.AccountDTO;
import com.example.account.model.CreateAccountRequestModel;
import com.example.account.service.AccountService;
import com.example.account.service.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final Environment environment;

    Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @PostMapping(value = "/account")
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody CreateAccountRequestModel request) {
        logger.info("Account Service " + environment.getProperty("local.server.port"));
        AccountDTO accountDTO = accountService.createAccount(request);
        return new ResponseEntity<>(accountDTO, HttpStatus.CREATED);
    }

    @GetMapping(value = "/account/{phone-number}")
    public ResponseEntity<AccountDTO> getAccountByPhoneNumber(@PathVariable(name = "phone-number") @NotEmpty String phoneNumber) {
        AccountDTO account = accountService.getAccountByPhoneNumber(phoneNumber);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

}
