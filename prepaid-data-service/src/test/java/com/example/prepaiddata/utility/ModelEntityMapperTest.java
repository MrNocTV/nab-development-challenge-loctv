package com.example.prepaiddata.utility;

import com.example.prepaiddata.configuration.MapperConfig;
import com.example.prepaiddata.configuration.TestConfig;
import com.example.prepaiddata.entity.Voucher;
import com.example.prepaiddata.model.VoucherPurchaseRequestModel;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@Import({TestConfig.class, MapperConfig.class})
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class ModelEntityMapperTest {

    @Autowired
    private ModelEntityMapper modelEntityMapper;

    @Autowired
    private Faker faker;

    @Test
    public void it_should_successfully_map_voucher_request_model_to_voucher_entity() {
        VoucherPurchaseRequestModel voucherPurchaseRequestModel = given_we_have_a_voucher_request_model();
        Voucher voucher = when_we_map_it_to_voucher_entity(voucherPurchaseRequestModel);
        then_the_corresponding_value_should_be_identical(voucherPurchaseRequestModel, voucher);
    }

    private VoucherPurchaseRequestModel given_we_have_a_voucher_request_model() {
        return VoucherPurchaseRequestModel.builder()
            .code(UUID.randomUUID().toString())
            .dataAmount(faker.number().randomDigit())
            .build();
    }

    private Voucher when_we_map_it_to_voucher_entity(VoucherPurchaseRequestModel voucherPurchaseRequestModel) {
        Voucher voucher = modelEntityMapper.mapVoucherRequestModelToVoucherEntity(voucherPurchaseRequestModel);
        return voucher;
    }

    private void then_the_corresponding_value_should_be_identical(VoucherPurchaseRequestModel voucherPurchaseRequestModel, Voucher voucher) {
        Assertions.assertEquals(voucher.getCode(), voucherPurchaseRequestModel.getCode());
    }
}
