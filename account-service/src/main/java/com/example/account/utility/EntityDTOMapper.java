package com.example.account.utility;

import com.example.account.dto.AccountDTO;
import com.example.account.entity.Account;
import org.modelmapper.ModelMapper;

public class EntityDTOMapper {

    private ModelMapper modelMapper;

    public EntityDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AccountDTO mapAccountEntityToAccountDTO(Account account) {
        // still assign the value to a variable for easier debugging
        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
        return accountDTO;
    }

}
