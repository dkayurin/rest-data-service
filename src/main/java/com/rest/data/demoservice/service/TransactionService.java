package com.rest.data.demoservice.service;

import java.sql.Date;
import java.time.Clock;

import org.springframework.stereotype.Service;

import com.rest.data.demoservice.exception.TransactionException;
import com.rest.data.demoservice.model.TransactionDto;
import com.rest.data.demoservice.persistence.entity.Account;
import com.rest.data.demoservice.persistence.entity.Transaction;
import com.rest.data.demoservice.persistence.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;

    public void execute(TransactionDto transaction) {
        final var sourceAccount = accountService.findAccountById(transaction.getSourceAccountId());
        final var amountOfMoney = transaction.getAmount();

        if (isSourceBalanceEnough(sourceAccount, amountOfMoney)) {
            beginTransaction(sourceAccount, transaction);
        }
    }

    boolean isSourceBalanceEnough(Account sourceAccount, float amount) {
        if (Float.compare(sourceAccount.getBalance(), amount) < 0) {
            throw new TransactionException("Not enough money...");
        }
        return true;
    }

    void beginTransaction(Account account, TransactionDto transaction) {
        log.info("Start new transaction for account with id: " + account.getAccountId());
        getMoneyFromSource(account, transaction.getAmount());
        depositMoneyToTarget(transaction.getTargetAccountId(), transaction.getAmount());
        updateTransactionLog(transaction, account);
    }

    void getMoneyFromSource(Account sourceAccount, float amount) {
        log.info("Getting money from account : " + sourceAccount.getAccountId());
        var newBalance = sourceAccount.getBalance() - amount;
        updateBalance(sourceAccount, newBalance);
    }

    void depositMoneyToTarget(String targetAccountId, float amount) {
        log.info("Depositing money to account : " + targetAccountId);
        Account targetAccount = accountService.findAccountById(targetAccountId);
        var newBalance = targetAccount.getBalance() + amount;
        updateBalance(targetAccount, newBalance);
    }

    void updateBalance(Account account, Float balance) {
        account.setBalance(balance);
        accountService.updateAccountBalance(account);
    }

    void updateTransactionLog(TransactionDto transactionDto, Account account) {
        var transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTargetAccountId(transactionDto.getTargetAccountId());
        transaction.setTransactionDate(Date.from(Clock.systemUTC().instant()));
        transactionRepository.save(transaction);
    }

}
