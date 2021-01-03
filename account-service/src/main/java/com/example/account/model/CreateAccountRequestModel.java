package com.example.account.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountRequestModel {

    @NotEmpty
    private String phoneNumber;

    @NotEmpty
    private String address;
}
