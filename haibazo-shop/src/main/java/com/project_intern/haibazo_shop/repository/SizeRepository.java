package com.project_intern.haibazo_shop.repository;

import com.project_intern.haibazo_shop.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SizeRepository extends JpaRepository<Size, Long> {
    Optional<Size> findBySize(String size);
}