/**
 * @author Loc Truong
 */

package com.example.voucher.controller;

import com.example.voucher.service.VoucherGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class VoucherController {

    Logger logger = LoggerFactory.getLogger(VoucherController.class);

    private final VoucherGeneratorService voucherGeneratorService;

    public VoucherController(VoucherGeneratorService voucherGeneratorService) {
        this.voucherGeneratorService = voucherGeneratorService;
    }

    /**
     * Simple method trying to generate unique string
     * The returned value might not user-friendly, however it's easy to test and implement
     * @param delayTime: stimulate slow network, it's optional, most of the time is used for testing
     * @return a unique string
     */
    @Operation(summary = "Generate a unique voucher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/voucher", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getVoucher(
        @Parameter(description = "stimulate slow network, it's optional, most of the time is used for testing")
        @RequestParam(value = "delay", required = false, defaultValue = "3") Integer delayTime) throws InterruptedException {

        // stimulate the case of interrupted exception
        if (delayTime == -1)
            throw new InterruptedException();

        Thread.sleep(delayTime * 1000);
        logger.info("Wake up after " + delayTime + " seconds");

        return voucherGeneratorService.generateVoucher();
    }

}
