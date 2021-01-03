package com.example.account.controller;

import com.example.account.configuration.TestConfig;
import com.example.account.dto.AccountDTO;
import com.example.account.entity.Account;
import com.example.account.model.CreateAccountRequestModel;
import com.example.account.repository.AccountRepository;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestConfig.class)
public class AccountControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private Faker faker;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void set_up() {
        RestAssured.port = port;
        accountRepository.deleteAll();
    }

    @Test
    public void it_should_return_bad_request_http_code_if_the_account_already_exists_when_creating_account() {
        Account existingAccount = given_we_have_an_account();
        Response response = when_we_call_api_end_point_to_create_an_account(existingAccount);
        then_the_http_status_code_should_be_400(response);
    }

    @Test
    public void it_should_successfully_create_new_account() {
        Account account = Account.builder()
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .address(faker.address().streetAddress())
                .build();

        Response response = when_we_call_api_end_point_to_create_an_account(account);
        then_the_http_status_code_should_be_201(response);
    }

    @Test
    public void it_should_return_404_http_code_if_the_account_does_not_exist_when_getting_account_by_phone_number() {
        Response response = when_call_api_to_get_non_existing_account_by_phone_number(faker.phoneNumber().phoneNumber());
        then_the_http_status_code_should_be_404(response);
    }

    @Test
    public void it_should_successfully_return_an_account_if_it_exists_when_getting_account_by_phone_number() {
        Account existingAccount = given_we_have_an_account();
        Response response = when_call_api_to_get_an_existing_account_by_phone_number(existingAccount.getPhoneNumber());
        then_the_http_status_code_should_be_200(response);
        and_the_returned_account_is_correct(response, existingAccount);
    }

    private Account given_we_have_an_account() {
        Account account = Account.builder()
                .address(faker.address().streetAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .build();
        return accountRepository.saveAndFlush(account);
    }

    private Response when_we_call_api_end_point_to_create_an_account(Account account) {
        return given()
                .contentType(ContentType.JSON)
                .body(CreateAccountRequestModel.builder()
                        .address(account.getAddress())
                        .phoneNumber(account.getPhoneNumber())
                        .build())
                .when()
                .post("/api/account");
    }

    private Response when_call_api_to_get_non_existing_account_by_phone_number(String phoneNumber) {
        return when()
                .get("/api/account/" + faker.phoneNumber().phoneNumber());
    }

    private Response when_call_api_to_get_an_existing_account_by_phone_number(String phoneNumber) {
        return when()
                .get("/api/account/" + phoneNumber);
    }

    private void then_the_http_status_code_should_be_200(Response response) {
        response.then().statusCode(HttpStatus.OK.value());
    }

    private void and_the_returned_account_is_correct(Response response, Account existingAccount) {
        AccountDTO accountDTO = response.body().as(AccountDTO.class);
        assertEquals(accountDTO.getAddress(), existingAccount.getAddress());
        assertEquals(accountDTO.getPhoneNumber(), existingAccount.getPhoneNumber());
    }

    private void then_the_http_status_code_should_be_400(Response response) {
        response.then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private void then_the_http_status_code_should_be_201(Response response) {
        response.then().statusCode(HttpStatus.CREATED.value());
    }

    private void then_the_http_status_code_should_be_404(Response response) {
        response.then().statusCode(HttpStatus.NOT_FOUND.value());
    }
}
