package com.example.prepaiddata.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Builder
@Embeddable
public class VoucherId implements Serializable {
    private String code;
    private String phoneNumber;
}
