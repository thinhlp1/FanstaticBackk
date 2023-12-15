package com.fanstatic.repository;

import com.fanstatic.model.UserVoucher;
import com.fanstatic.model.Voucher;

import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// import com.fanstatic.dto.model.voucher.VoucherCountCollected;
import com.fanstatic.dto.model.voucher.VoucherDTO;
import com.fanstatic.model.UserVoucher;
import com.fanstatic.model.Voucher;

public interface UserVoucherRepository extends JpaRepository<UserVoucher, Integer> {
        @Query("SELECT uv.voucher FROM UserVoucher uv " +
                        "WHERE uv.user.id = :userId " +
                        "AND  uv.voucher.active = 1 " +
                        "AND :currentDate >=  uv.voucher.startAt AND  :currentDate <= uv.voucher.endAt AND uv.useAt IS NULL")
        List<Voucher> findActiveVouchersForUser(@Param("userId") int userId, @Param("currentDate") Date currentDate);

        @Query("SELECT v, COUNT(uv) AS quantityCollected " +
                        "FROM Voucher v " +
                        "LEFT JOIN UserVoucher uv ON v.id = uv.voucher.id " +
                        "WHERE v.active = 1 " +
                        "AND :currentDate BETWEEN v.startAt AND v.endAt " +
                        "GROUP BY v")
        List<Object[]> findValidVouchersWithCount(@Param("currentDate") Date currentDate);

        @Modifying
        @Transactional
        @Query("DELETE FROM UserVoucher uv WHERE uv.user.id = :userId AND uv.voucher.id = :voucherId")
        void deleteByUserIdAndVoucherId(int userId, int voucherId);

        @Query("SELECT uv FROM UserVoucher uv WHERE uv.user.id = :userId AND uv.voucher.id = :voucherId")
        Optional<UserVoucher> findBbyUserIdAndVoucherId(int userId, int voucherId);
}
