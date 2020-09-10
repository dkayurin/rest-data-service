package com.rest.data.demoservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.rest.data.demoservice.model.TransactionDto;
import com.rest.data.demoservice.persistence.entity.Account;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    private static final String SOURCE_ACCOUNT_ID = "1";
    private static final String TARGET_ACCOUNT_ID = "2";
    private static final float AMOUNT = 10;

    @Mock
    private AccountService accountService;

    @Spy
    @InjectMocks
    private TransactionService transactionService;

    private TransactionDto transactionDto;
    private Account sourceAccount;
    private Account targetAccount;

    @Before
    public void setUp() {
        transactionDto = new TransactionDto(SOURCE_ACCOUNT_ID, TARGET_ACCOUNT_ID, AMOUNT);
        sourceAccount = new Account();
        targetAccount = new Account();
    }

    @Test
    public void testExecute() {
        //given
        given(accountService.findAccountById(anyString()))
            .willReturn(sourceAccount);
        willReturn(true)
            .given(transactionService).isSourceBalanceEnough(any(), anyFloat());
        willDoNothing()
            .given(transactionService).beginTransaction(any(), any());
        //when
        transactionService.execute(transactionDto);
        //then
        verify(accountService).findAccountById(SOURCE_ACCOUNT_ID);
        verify(transactionService).isSourceBalanceEnough(sourceAccount, AMOUNT);
        verify(transactionService).beginTransaction(sourceAccount, transactionDto);
    }

    @Test
    public void testIsSourceBalanceEnough() {
        //given
        sourceAccount.setBalance(20f);
        //when
        boolean result = transactionService.isSourceBalanceEnough(sourceAccount, AMOUNT);
        //then
        assertThat(result).isTrue();
    }

    @Test
    public void testIsSourceBalanceEnough_False() {
        //given
        sourceAccount.setBalance(5f);
        //when
        Throwable t = catchThrowable(() ->transactionService.isSourceBalanceEnough(sourceAccount, AMOUNT));
        //then
        assertThat(t).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void testBeginTransaction() {
        //given
        willDoNothing()
            .given(transactionService).getMoneyFromSource(any(), anyFloat());
        willDoNothing()
            .given(transactionService).depositMoneyToTarget(any(), anyFloat());
        willDoNothing()
            .given(transactionService).updateTransactionLog(any(), any());
        //when
        transactionService.beginTransaction(sourceAccount, transactionDto);
        //then
        verify(transactionService, times(1)).getMoneyFromSource(sourceAccount, AMOUNT);
        verify(transactionService, times(1)).depositMoneyToTarget(TARGET_ACCOUNT_ID, AMOUNT);
        verify(transactionService, times(1)).updateTransactionLog(transactionDto, sourceAccount);
    }

    @Test
    public void testGetMoneyFromSource() {
        //given
        sourceAccount.setBalance(20f);
        var expectedBalance = 10f;
        willDoNothing()
            .given(transactionService).updateBalance(any(), anyFloat());
        //when
        transactionService.getMoneyFromSource(sourceAccount, AMOUNT);
        //then
        verify(transactionService).updateBalance(sourceAccount, expectedBalance);
    }

    @Test
    public void testDepositMoneyToTarget() {
        //given
        targetAccount.setBalance(20f);
        var expectedBalance = 30f;
        given(accountService.findAccountById(any()))
            .willReturn(targetAccount);
        willDoNothing()
            .given(transactionService).updateBalance(any(), anyFloat());
        //when
        transactionService.depositMoneyToTarget(TARGET_ACCOUNT_ID, AMOUNT);
        //then
        verify(transactionService).updateBalance(targetAccount, expectedBalance);
    }
}
