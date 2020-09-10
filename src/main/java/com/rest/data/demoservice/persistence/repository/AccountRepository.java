package com.rest.data.demoservice.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.data.demoservice.persistence.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
}
