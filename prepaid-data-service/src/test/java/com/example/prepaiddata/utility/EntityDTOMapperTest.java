package com.example.prepaiddata.utility;

import com.example.prepaiddata.configuration.MapperConfig;
import com.example.prepaiddata.configuration.TestConfig;
import com.example.prepaiddata.dto.VoucherDTO;
import com.example.prepaiddata.entity.Voucher;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import({TestConfig.class, MapperConfig.class})
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class EntityDTOMapperTest {

    @Autowired
    private EntityDTOMapper entityDTOMapper;

    @Autowired
    private Faker faker;

    @Test
    public void it_should_successfully_map_voucher_entity_to_voucher_dto() {
        Voucher voucher = given_we_have_a_voucher();
        VoucherDTO voucherDTO = when_we_map_voucher_to_its_dto(voucher);
        then_the_corresponding_properties_of_voucher_and_its_dto_should_be_the_same(voucher, voucherDTO);
    }


    private Voucher given_we_have_a_voucher() {
        return Voucher.builder()
            .phoneNumber(faker.phoneNumber().phoneNumber())
            .code(UUID.randomUUID().toString())
            .dataAmount(faker.number().randomDigit())
            .build();
    }

    private VoucherDTO when_we_map_voucher_to_its_dto(Voucher voucher) {
        return entityDTOMapper.mapVoucherEntityToVoucherDTO(voucher);
    }

    private void then_the_corresponding_properties_of_voucher_and_its_dto_should_be_the_same(Voucher voucher, VoucherDTO voucherDTO) {
        assertEquals(voucher.getCode(), voucherDTO.getCode());
        assertEquals(voucher.getDataAmount(), voucherDTO.getDataAmount());
        assertEquals(voucher.getPhoneNumber(), voucherDTO.getPhoneNumber());
    }
}
