package com.fanstatic.repository;

import com.fanstatic.model.Size;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SizeRepository extends JpaRepository<Size, Integer> {
     public Optional<Size> findByIdAndActiveIsFalse(int id);

     public Optional<Size> findByIdAndActiveIsTrue(int id);

}
