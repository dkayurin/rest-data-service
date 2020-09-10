package com.rest.data.demoservice.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.data.demoservice.persistence.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
