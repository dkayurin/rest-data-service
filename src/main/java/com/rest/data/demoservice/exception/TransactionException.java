package com.rest.data.demoservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TransactionException extends RuntimeException {

    private final String message;
}
