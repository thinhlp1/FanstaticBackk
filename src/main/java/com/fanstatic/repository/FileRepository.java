package com.fanstatic.repository;

import com.fanstatic.model.File;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FileRepository extends JpaRepository<File, Integer> {
}
