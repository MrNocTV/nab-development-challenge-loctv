package com.example.prepaiddata.utility;

import com.example.prepaiddata.dto.VoucherDTO;
import com.example.prepaiddata.entity.Voucher;
import org.modelmapper.ModelMapper;

public class EntityDTOMapper {

    private ModelMapper modelMapper;

    public EntityDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public VoucherDTO mapVoucherEntityToVoucherDTO(Voucher voucher) {
        // still assign the value to a variable for easier debugging
        VoucherDTO voucherDTO = modelMapper.map(voucher, VoucherDTO.class);
        return voucherDTO;
    }

}
