package com.rest.data.demoservice.service;

import java.util.List;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.rest.data.demoservice.persistence.entity.Account;
import com.rest.data.demoservice.persistence.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account findAccountById(String accountId) {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found by provided id: " + accountId));
    }

    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    void updateAccountBalance(Account account) {
        accountRepository.save(account);
    }

}
