package com.example.prepaiddata.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
public class GetTokenRequestModel {

    @NotEmpty
    private String phoneNumber;

    @NotEmpty
    private String otp;

}
