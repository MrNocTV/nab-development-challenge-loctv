package com.example.prepaiddata.model;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherPurchaseRequestModel {

    private String code;

    @NotNull
    @Min(1)
    private Integer dataAmount;

    @NotNull
    private String phoneNumber;

}
