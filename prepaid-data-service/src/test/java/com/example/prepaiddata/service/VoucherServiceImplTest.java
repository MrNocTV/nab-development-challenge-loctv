package com.example.prepaiddata.service;

import com.example.prepaiddata.configuration.TestConfig;
import com.example.prepaiddata.dto.ViewTokenDTO;
import com.example.prepaiddata.dto.VoucherDTO;
import com.example.prepaiddata.entity.Voucher;
import com.example.prepaiddata.exception.VoucherAlreadyAppliedException;
import com.example.prepaiddata.exception.VoucherAlreadyExistsException;
import com.example.prepaiddata.exception.VoucherNotFoundException;
import com.example.prepaiddata.model.GetTokenRequestModel;
import com.example.prepaiddata.model.VoucherApplyRequestModel;
import com.example.prepaiddata.model.VoucherPurchaseRequestModel;
import com.example.prepaiddata.repository.VoucherRepository;
import com.example.prepaiddata.security.jwt.JwtTokenManager;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
public class VoucherServiceImplTest {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Autowired
    private Faker faker;

    @BeforeEach
    public void set_up() {
        voucherRepository.deleteAll();
    }

    @Test
    public void it_should_return_a_list_of_VoucherDTO_when_get_vouchers_by_phone_number() {
        Voucher voucher = given_we_have_a_voucher();
        List<VoucherDTO> voucherDTOs = when_we_call_getVouchersByPhoneNumber(voucher.getPhoneNumber());
        then_the_list_of_VoucherDTO_should_contain_one_element(voucherDTOs);
    }

    @Test
    public void it_should_throw_VoucherNotFoundException_when_apply_voucher_with_non_existing_voucher() {
        assertThrows(VoucherNotFoundException.class, () -> when_applyVoucherForPhoneNumber_with_non_existing_phone_number());
    }

    @Test
    public void it_should_throw_VoucherAlreadyAppliedException_when_apply_voucher_that_has_been_already_applied() {
        given_we_already_have_an_applied_voucher();
        assertThrows(VoucherAlreadyAppliedException.class, () -> when_applyVoucherForPhoneNumber_with_voucher_that_already_applied());
    }

    @Test
    public void it_should_successfully_apply_voucher_for_phone_number_if_voucher_is_not_applied() {
        given_we_have_a_voucher_that_has_not_been_applied();
        when_we_apply_that_voucher_for_a_phone_number();
        then_the_voucher_should_be_successfully_applied();
    }

    @Test
    public void it_should_throw_VoucherAlreadyExistsException_when_creating_voucher_that_is_already_existed() {
        Voucher voucher = given_we_have_a_voucher();
        assertThrows(VoucherAlreadyExistsException.class, () -> when_we_create_a_voucher_that_is_already_existed(voucher.getCode()));
    }

    @Test
    public void it_should_successfully_create_voucher_if_it_does_not_exist_yet() {
        when_we_create_a_new_voucher();
    }

    private void when_we_create_a_new_voucher() {
        voucherService.createVoucher(VoucherPurchaseRequestModel.builder()
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .code(UUID.randomUUID().toString())
                .dataAmount(faker.number().randomDigit())
                .build());
    }

    @Test
    public void it_should_return_a_jwt_token_when_getting_view_token_for_vouchers() {
        ViewTokenDTO viewTokenDTO = when_we_get_view_token_for_vouchers();
        assert_that_the_token_is_valid(viewTokenDTO.getJwtToken());
    }

    private ViewTokenDTO when_we_get_view_token_for_vouchers() {
        return voucherService.getVouchersViewToken(GetTokenRequestModel.builder()
                .otp(faker.crypto().md5())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .build(), faker.number().randomDigit());
    }

    private void assert_that_the_token_is_valid(String jwtToken) {
        jwtTokenManager.authenticate(jwtToken);
    }

    private void when_we_create_a_voucher_that_is_already_existed(String voucherCode) {
        voucherService.createVoucher(VoucherPurchaseRequestModel.builder()
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .code(voucherCode)
                .dataAmount(faker.number().randomDigit())
                .build());
    }

    private void given_we_have_a_voucher_that_has_not_been_applied() {
        voucherRepository.saveAndFlush(Voucher.builder()
                .code("not-applied-voucher")
                .dataAmount(faker.number().randomDigit())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .applied(Boolean.FALSE)
                .build());
    }

    private void when_we_apply_that_voucher_for_a_phone_number() {
        voucherService.applyVoucherForPhoneNumber(VoucherApplyRequestModel.builder()
                .code("not-applied-voucher")
                .phoneNumber("not-applied-voucher")
                .build());
    }

    private void then_the_voucher_should_be_successfully_applied() {
        Pageable pageable = PageRequest.of(0, 10);
        List<VoucherDTO> voucherDTOs = voucherService.getVouchersByPhoneNumber("not-applied-voucher", pageable);
        assertThat(voucherDTOs.size()).isEqualTo(1);
        assertThat(voucherDTOs.get(0).getApplied()).isTrue();
    }

    private void given_we_already_have_an_applied_voucher() {
        Voucher voucher = Voucher.builder()
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .code("applied-voucher")
                .applied(Boolean.TRUE)
                .dataAmount(faker.number().randomDigit())
                .build();

        voucherRepository.saveAndFlush(voucher);
    }

    private void when_applyVoucherForPhoneNumber_with_voucher_that_already_applied() {
        voucherService.applyVoucherForPhoneNumber(VoucherApplyRequestModel.builder()
                .code("applied-voucher")
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .build());
    }

    private void when_applyVoucherForPhoneNumber_with_non_existing_phone_number() {
        voucherService.applyVoucherForPhoneNumber(VoucherApplyRequestModel.builder()
                .code(UUID.randomUUID().toString())
                .phoneNumber("non-existing-phone-number").build());
    }

    private List<VoucherDTO> when_we_call_getVouchersByPhoneNumber(String phoneNumber) {
        Pageable pageable = PageRequest.of(0, faker.number().numberBetween(1, 10));
        return voucherService.getVouchersByPhoneNumber(phoneNumber, pageable);
    }

    private Voucher given_we_have_a_voucher() {
        return voucherRepository.saveAndFlush(Voucher.builder()
                .code(UUID.randomUUID().toString())
                .dataAmount(faker.number().randomDigit())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .applied(Boolean.FALSE)
                .build());
    }

    private void then_the_list_of_VoucherDTO_should_contain_one_element(List<VoucherDTO> voucherDTOs) {
        assertThat(voucherDTOs.size()).isEqualTo(1);
    }
}
