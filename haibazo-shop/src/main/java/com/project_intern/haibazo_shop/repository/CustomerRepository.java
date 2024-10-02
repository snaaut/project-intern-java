package com.project_intern.haibazo_shop.repository;

import com.project_intern.haibazo_shop.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
