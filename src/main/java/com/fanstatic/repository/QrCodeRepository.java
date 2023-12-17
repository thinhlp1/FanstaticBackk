package com.fanstatic.repository;

import com.fanstatic.model.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QrCodeRepository extends JpaRepository<QrCode, Integer> {
}
