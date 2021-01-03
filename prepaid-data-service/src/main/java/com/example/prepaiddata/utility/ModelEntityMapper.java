package com.example.prepaiddata.utility;

import com.example.prepaiddata.entity.Voucher;
import com.example.prepaiddata.model.VoucherPurchaseRequestModel;
import org.modelmapper.ModelMapper;

public class ModelEntityMapper {

    private ModelMapper modelMapper;

    public ModelEntityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Voucher mapVoucherRequestModelToVoucherEntity(VoucherPurchaseRequestModel voucherPurchaseRequestModel) {
        Voucher voucher = modelMapper.map(voucherPurchaseRequestModel, Voucher.class);
        return voucher;
    }

}
