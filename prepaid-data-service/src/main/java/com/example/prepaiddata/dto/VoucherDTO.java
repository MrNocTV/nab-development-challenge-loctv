package com.example.prepaiddata.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherDTO {

    private String code;
    private int dataAmount;
    private String phoneNumber;
    private Boolean applied;

}
