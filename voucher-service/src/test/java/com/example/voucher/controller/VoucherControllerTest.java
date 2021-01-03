package com.example.voucher.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class VoucherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void it_should_return_a_uuid_after_a_specific_amount_of_time() throws Exception {
        long startTime = System.nanoTime();
        when_we_call_api_to_generate_voucher();
        long endTime = System.nanoTime();
        assert_that_the_period_to_get_the_voucher_should_be_predictable(startTime, endTime);
    }

    @Test
    public void it_should_return_status_500_when_the_delay_time_is_negative() throws Exception {
        when_we_get_voucher_with_negative_delay();
    }

    private void when_we_call_api_to_generate_voucher() throws Exception {
        mockMvc.perform(
                get("/api/voucher")
                .param("delay", "2"))
                .andExpect(status().isOk());
    }

    private void assert_that_the_period_to_get_the_voucher_should_be_predictable(long startTime, long endTime) {
        long duration = (endTime - startTime) / 1000000;
        assertTrue(duration >= 2000 && duration <= 2500);
    }

    private void when_we_get_voucher_with_negative_delay() throws Exception {
        mockMvc.perform(
                get("/api/voucher")
                .param("delay", "-1")) // stimulate un-happy case
                .andExpect(status().isInternalServerError());
    }

}
