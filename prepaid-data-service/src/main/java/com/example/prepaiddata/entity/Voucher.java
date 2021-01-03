package com.example.prepaiddata.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "voucher", indexes = @Index(name = "pn_index", columnList = "phone_number"))
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "data_amount")
    private Integer dataAmount;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "applied", columnDefinition = "boolean default false", nullable = false)
    private Boolean applied;
}
