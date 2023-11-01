package com.fanstatic.repository;

import com.fanstatic.model.File;
import com.google.common.base.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer> {
    public Optional<File> findByFileTypeAndActiveIsTrue(String fileType);
}
