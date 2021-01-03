package com.example.prepaiddata.service;

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
import com.example.prepaiddata.utility.EntityDTOMapper;
import com.example.prepaiddata.utility.ModelEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final ModelEntityMapper modelEntityMapper;
    private final JwtTokenManager jwtTokenManager;

    @Override
    public List<VoucherDTO> getVouchersByPhoneNumber(String phoneNumber, Pageable pageable) {
        List<Voucher> voucherEntities = voucherRepository.findByPhoneNumber(phoneNumber, pageable);
        List<VoucherDTO> result = voucherEntities.stream()
            .map(entityDTOMapper::mapVoucherEntityToVoucherDTO)
            .collect(Collectors.toList());

        return result;
    }

    @Override
    @Transactional
    public VoucherDTO applyVoucherForPhoneNumber(VoucherApplyRequestModel voucherApplyRequestModel) {
        Optional<Voucher> optionalVoucher = voucherRepository.findByCode(voucherApplyRequestModel.getCode());
        if (!optionalVoucher.isPresent()) {
            throw new VoucherNotFoundException("There is no existing voucher with that code");
        }

        Voucher voucher = optionalVoucher.get();
        if (voucher.getPhoneNumber() != null && voucher.getApplied() != null && voucher.getApplied()) {
            throw new VoucherAlreadyAppliedException("This voucher is already applied");
        }

        voucher.setPhoneNumber(voucherApplyRequestModel.getPhoneNumber());
        voucher.setApplied(Boolean.TRUE);
        voucherRepository.saveAndFlush(voucher);
        VoucherDTO voucherDTO = entityDTOMapper.mapVoucherEntityToVoucherDTO(voucher);

        return voucherDTO;
    }

    @Override
    @Transactional
    public VoucherDTO createVoucher(VoucherPurchaseRequestModel voucherPurchaseRequestModel) {
        Optional<Voucher> optionalVoucher = voucherRepository.findByCode(voucherPurchaseRequestModel.getCode());
        if (optionalVoucher.isPresent()) {
            throw new VoucherAlreadyExistsException("There is already an existing voucher with that code");
        }

        Voucher voucher = modelEntityMapper.mapVoucherRequestModelToVoucherEntity(voucherPurchaseRequestModel);
        voucher.setApplied(Boolean.FALSE);
        Voucher persistedVoucher = voucherRepository.saveAndFlush(voucher);
        VoucherDTO voucherDTO = entityDTOMapper.mapVoucherEntityToVoucherDTO(persistedVoucher);

        return voucherDTO;
    }

    @Override
    public ViewTokenDTO getVouchersViewToken(GetTokenRequestModel tokenRequestModel, int timeout) {
        ViewTokenDTO tokenDTO = jwtTokenManager.createToken(tokenRequestModel, timeout);
        return tokenDTO;
    }
}
