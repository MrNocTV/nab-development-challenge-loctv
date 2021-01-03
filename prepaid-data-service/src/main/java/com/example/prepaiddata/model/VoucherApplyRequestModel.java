package com.example.prepaiddata.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherApplyRequestModel {

    @NotNull
    private String code;

    @NotNull
    private String phoneNumber;

}
