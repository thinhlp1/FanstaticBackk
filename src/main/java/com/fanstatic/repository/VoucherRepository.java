package com.fanstatic.repository;

import com.fanstatic.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
}
