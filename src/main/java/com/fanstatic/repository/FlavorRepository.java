package com.fanstatic.repository;

import com.fanstatic.model.Flavor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FlavorRepository extends JpaRepository<Flavor, Integer> {
}
