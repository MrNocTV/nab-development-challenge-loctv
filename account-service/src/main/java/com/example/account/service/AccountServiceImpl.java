package com.example.account.service;

import com.example.account.dto.AccountDTO;
import com.example.account.entity.Account;
import com.example.account.exception.AccountAlreadyExistException;
import com.example.account.exception.AccountNotFoundException;
import com.example.account.model.CreateAccountRequestModel;
import com.example.account.repository.AccountRepository;
import com.example.account.utility.EntityDTOMapper;
import com.example.account.utility.ModelEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final ModelEntityMapper modelEntityMapper;

    @Override
    public AccountDTO getAccountByPhoneNumber(String phoneNumber) {
        Optional<Account> optionalAccount = accountRepository.findByPhoneNumber(phoneNumber);
        if (!optionalAccount.isPresent()) {
            throw new AccountNotFoundException("There is no existing account having that phone number");
        }

        return entityDTOMapper.mapAccountEntityToAccountDTO(optionalAccount.get());
    }

    @Override
    public AccountDTO createAccount(CreateAccountRequestModel request) {
        Optional<Account> persistedAccount = accountRepository.findByPhoneNumber(request.getPhoneNumber());
        if (persistedAccount.isPresent()) {
            throw new AccountAlreadyExistException("There is already an account having that phone number");
        }

        Account account = modelEntityMapper.mapAccountRequestModelToAccountEntity(request);
        AccountDTO accountDTO = entityDTOMapper.mapAccountEntityToAccountDTO(accountRepository.saveAndFlush(account));
        return accountDTO;
    }
}
