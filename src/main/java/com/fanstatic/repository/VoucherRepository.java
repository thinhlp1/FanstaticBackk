package com.fanstatic.repository;

import com.fanstatic.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    public Optional<List<Voucher>> findAllByActiveIsTrue();

    public Optional<List<Voucher>> findAllByActiveIsFalse();

    public Optional<Voucher> findByIdAndActiveIsTrue(int id);

    public Optional<Voucher> findByVoucherCodeAndActiveIsTrue(String code);

    public Optional<Voucher> findByIdAndActiveIsFalse(int id);

    public Optional<Voucher> findByNameAndActiveIsTrue(String name);
}
