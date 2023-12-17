package com.fanstatic.repository;

import com.fanstatic.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String> {
}
