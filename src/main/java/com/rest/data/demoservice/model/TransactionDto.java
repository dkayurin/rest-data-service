package com.rest.data.demoservice.model;

import org.springframework.lang.NonNull;

import lombok.Data;

@Data
public class TransactionDto {

    @NonNull
    private String sourceAccountId;

    @NonNull
    private String targetAccountId;

    @NonNull
    private float amount;
}
