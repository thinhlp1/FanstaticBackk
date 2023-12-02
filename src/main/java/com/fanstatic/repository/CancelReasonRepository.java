package com.fanstatic.repository;

import com.fanstatic.model.CancelReason;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CancelReasonRepository extends JpaRepository<CancelReason, Integer> {
}
