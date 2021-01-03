package com.example.prepaiddata.repository;

import com.example.prepaiddata.entity.Voucher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    List<Voucher> findByPhoneNumber(String phoneNumber, Pageable pageable);

    Optional<Voucher> findByCode(String code);
}
