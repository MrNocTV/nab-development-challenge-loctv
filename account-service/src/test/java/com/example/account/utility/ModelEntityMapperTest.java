package com.example.account.utility;

import com.example.account.configuration.TestConfig;
import com.example.account.entity.Account;
import com.example.account.model.CreateAccountRequestModel;
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
public class ModelEntityMapperTest {

    @Autowired
    private ModelEntityMapper modelEntityMapper;

    @Autowired
    private Faker faker;

    @Test
    public void it_should_successfully_map_create_account_request_model_to_account_entity() {
        CreateAccountRequestModel createAccountRequestModel = given_we_have_a_create_account_request_model();
        Account account = when_we_convert_it_to_account_entity(createAccountRequestModel);
        then_assert_that_the_corresponding_properties_are_the_same(createAccountRequestModel, account);
    }

    private CreateAccountRequestModel given_we_have_a_create_account_request_model() {
        return CreateAccountRequestModel.builder()
                .address(faker.address().streetAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .build();
    }

    private Account when_we_convert_it_to_account_entity(CreateAccountRequestModel accountRequestModel) {
        return modelEntityMapper.mapAccountRequestModelToAccountEntity(accountRequestModel);
    }

    private void then_assert_that_the_corresponding_properties_are_the_same(
            CreateAccountRequestModel accountRequestModel, Account account) {
        assertEquals(accountRequestModel.getAddress(), account.getAddress());
        assertEquals(accountRequestModel.getPhoneNumber(), account.getPhoneNumber());
    }

}
