package com.example.account.utility;

import com.example.account.entity.Account;
import com.example.account.model.CreateAccountRequestModel;
import org.modelmapper.ModelMapper;

public class ModelEntityMapper {

    private ModelMapper modelMapper;

    public ModelEntityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Account mapAccountRequestModelToAccountEntity(CreateAccountRequestModel createAccountRequestModel) {
        Account account = modelMapper.map(createAccountRequestModel, Account.class);
        return account;
    }
}
