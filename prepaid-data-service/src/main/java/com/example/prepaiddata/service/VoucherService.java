package com.example.prepaiddata.service;

import com.example.prepaiddata.dto.ViewTokenDTO;
import com.example.prepaiddata.dto.VoucherDTO;
import com.example.prepaiddata.model.GetTokenRequestModel;
import com.example.prepaiddata.model.VoucherApplyRequestModel;
import com.example.prepaiddata.model.VoucherPurchaseRequestModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VoucherService {

    List<VoucherDTO> getVouchersByPhoneNumber(String phoneNumber, Pageable pageable);

    VoucherDTO applyVoucherForPhoneNumber(VoucherApplyRequestModel voucher);

    VoucherDTO createVoucher(VoucherPurchaseRequestModel voucher);

    ViewTokenDTO getVouchersViewToken(GetTokenRequestModel tokenRequestModel, int timeout);
}
