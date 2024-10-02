package com.project_intern.haibazo_shop.repository;

import com.project_intern.haibazo_shop.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color, Long> {
    Optional<Color> findByColor(String color);
}