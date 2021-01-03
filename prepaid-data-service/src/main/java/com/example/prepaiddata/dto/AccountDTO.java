package com.example.prepaiddata.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String phoneNumber;
    private String address;
}
