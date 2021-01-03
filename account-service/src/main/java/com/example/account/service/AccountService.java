package com.example.account.service;

import com.example.account.dto.AccountDTO;
import com.example.account.model.CreateAccountRequestModel;

public interface AccountService {
    AccountDTO getAccountByPhoneNumber(String phoneNumber);

    AccountDTO createAccount(CreateAccountRequestModel request);
}
