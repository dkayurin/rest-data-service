package com.rest.data.demoservice.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.data.demoservice.persistence.entity.Client;

public interface ClientRepository extends JpaRepository<Client, String> {
}
