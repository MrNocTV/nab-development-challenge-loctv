package com.example.prepaiddata.controller;

import com.example.prepaiddata.client.AccountServiceClient;
import com.example.prepaiddata.client.VoucherServiceClient;
import com.example.prepaiddata.dto.ViewTokenDTO;
import com.example.prepaiddata.dto.VoucherDTO;
import com.example.prepaiddata.model.GetTokenRequestModel;
import com.example.prepaiddata.model.VoucherApplyRequestModel;
import com.example.prepaiddata.model.VoucherPurchaseRequestModel;
import com.example.prepaiddata.security.SecuredEndPoint;
import com.example.prepaiddata.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class PrepaidDataController {

    private final VoucherService voucherService;
    private final VoucherServiceClient voucherServiceClient;
    private final AccountServiceClient accountServiceClient;

    @PostMapping(value = "/prepaid-data/voucher/purchase")
    public ResponseEntity<VoucherDTO> purchaseData(
            @RequestBody @Valid VoucherPurchaseRequestModel request,
            @RequestParam(name = "delay", required = false, defaultValue = "3") Integer delay) {

        accountServiceClient.getAccountByPhoneNumber(request.getPhoneNumber());
        String voucherCode = voucherServiceClient.getVoucher(delay);
        request.setCode(voucherCode);
        VoucherDTO voucherDTO = voucherService.createVoucher(request);

        return new ResponseEntity<>(voucherDTO, HttpStatus.CREATED);
    }

    @PostMapping(value = "/prepaid-data/voucher/apply")
    public ResponseEntity<VoucherDTO> applyVoucher(@RequestBody @Valid VoucherApplyRequestModel request) {

        accountServiceClient.getAccountByPhoneNumber(request.getPhoneNumber());
        VoucherDTO voucherDTO = voucherService.applyVoucherForPhoneNumber(request);

        return new ResponseEntity<>(voucherDTO, HttpStatus.ACCEPTED);
    }

    @SecuredEndPoint
    @GetMapping(value = "/prepaid-data/voucher/{phone-number}")
    public ResponseEntity<List<VoucherDTO>> getVouchersByPhoneNumber(
            HttpServletRequest request,
            @PathVariable(name = "phone-number") String phoneNumber,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

        accountServiceClient.getAccountByPhoneNumber(phoneNumber);
        Pageable pageable = PageRequest.of(page, size);
        List<VoucherDTO> vouchers = voucherService.getVouchersByPhoneNumber(phoneNumber, pageable);

        return new ResponseEntity<>(vouchers, HttpStatus.OK);
    }

    @PostMapping(value = "/prepaid-data/voucher/get-view-token")
    public ResponseEntity<ViewTokenDTO> getJwtTokenToViewVouchers(
            @RequestBody @Valid GetTokenRequestModel getTokenRequestModel,
            @RequestParam(name = "timeout", required = false, defaultValue = "300") Integer timeout) {

        accountServiceClient.getAccountByPhoneNumber(getTokenRequestModel.getPhoneNumber());
        ViewTokenDTO vouchersViewToken = voucherService.getVouchersViewToken(getTokenRequestModel, timeout);

        return new ResponseEntity<>(vouchersViewToken, HttpStatus.OK);
    }

}
