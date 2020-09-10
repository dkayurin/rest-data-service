package com.rest.data.demoservice.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rest.data.demoservice.model.TransactionDto;
import com.rest.data.demoservice.persistence.entity.Account;
import com.rest.data.demoservice.persistence.entity.Client;
import com.rest.data.demoservice.service.AccountService;
import com.rest.data.demoservice.service.ClientService;
import com.rest.data.demoservice.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final ClientService clientService;
    private final AccountService accountService;
    private final TransactionService transactionService;


    @GetMapping("/clients")
    @ResponseStatus(HttpStatus.OK)
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/clients/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public Client getClient(@PathVariable("clientId") String clientId) {
        return clientService.findClientById(clientId);
    }

    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAccounts() {
        return accountService.findAllAccounts();
    }

    @GetMapping("/accounts/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccount(@PathVariable("accountId") String accountId) {
        return accountService.findAccountById(accountId);
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void executeTransaction(@RequestBody TransactionDto transaction) {
        transactionService.execute(transaction);
    }
}
