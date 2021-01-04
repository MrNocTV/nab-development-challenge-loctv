package com.example.account.utility;

import com.example.account.configuration.TestConfig;
import com.example.account.dto.AccountDTO;
import com.example.account.entity.Account;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles("test")
public class EntityDTOMapperTest {

    @Autowired
    private EntityDTOMapper entityDTOMapper;

    @Autowired
    private Faker faker;

    @Test
    public void it_should_successfully_map_account_entity_to_account_dto() {
        Account account = given_we_have_an_account();
        AccountDTO accountDTO = when_we_map_account_to_its_dto(account);
        then_the_corresponding_properties_of_account_and_its_dto_should_be_the_same(account, accountDTO);
    }

    private Account given_we_have_an_account() {
        return Account.builder()
            .id(1L)
            .phoneNumber(faker.phoneNumber().phoneNumber())
            .address(faker.address().streetAddress())
            .build();
    }

    private AccountDTO when_we_map_account_to_its_dto(Account account) {
        return entityDTOMapper.mapAccountEntityToAccountDTO(account);
    }

    private void then_the_corresponding_properties_of_account_and_its_dto_should_be_the_same(Account account, AccountDTO accountDTO) {
        assertEquals(account.getAddress(), accountDTO.getAddress());
        assertEquals(account.getPhoneNumber(), accountDTO.getPhoneNumber());
    }
}
