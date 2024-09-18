package com.microcompany.accountsservice.services;

import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.model.Customer;
import com.microcompany.accountsservice.persistence.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
class IAccountServiceTest {

    @TestConfiguration
    static class AccountServiceConfguration {
        @Bean
        public AccountService accountService () {
            return new AccountService();
        }
    }
    @BeforeEach
    public void SetUp () {

    }
    @Autowired
    AccountService service;
    @MockBean
    AccountRepository accountRepo;

    @Test
    void testBean() {
        assertNotNull(service);
    }

    @Test
    void create() {
        Account newAccount = new Account(null, "Personal", LocalDate.now(), 100, new Customer(null, "Ricardo", "r@r.com"),null);
        service.create(newAccount);
        assertNotNull(newAccount);
        assertTrue(newAccount.getId() > 0);
    }

    @Test
    void getAccounts() {
        List<Account> accounts = service.getAccounts();
        assertNotNull(accounts);
        assertTrue(accounts.size() > 0);
    }

    @Test
    void getAccount() {
        Account account = service.getAccount(1L);
        assertNotNull(account);
        assertEquals(account.getId(), 1L);
    }

    /*@Test
    void getAccountByOwnerId() {
        List<Account> accounts = service.getAccountByOwnerId(1L);
        assertNotNull(accounts);
        assertTrue(accounts.size() > 0);
    }*/

    @Test
    void updateAccount() {
        Account account = new Account();
        account.setType("Company");
        service.updateAccount(1L, account);

        Account updatedAccount = service.getAccount(1L);
        assertNotNull(updatedAccount);
        assertEquals(updatedAccount.getType(), "Company");
    }

    @Test
    void addBalance() {
        Account originalAccount = service.getAccount(1L);
        int amount = 10;

        service.addBalance(1L, amount, 1L);

        Account updatedAccount = service.getAccount(1L);
        assertEquals(updatedAccount.getBalance(), originalAccount.getBalance() + amount);
    }

    /*@Test
    void withdrawBalanceOK() throws Exception {
        Account originalAccount = service.getAccount(1L);
        int amount = 10;

        service.withdrawBalance(1L, amount, 1L);

        Account updatedAccount = service.getAccount(1L);
        assertEquals(updatedAccount.getBalance(), originalAccount.getBalance() - amount);
    }*/

    /*@Test
    void withdrawBalanceNOK() {
        Account originalAccount = service.getAccount(1L);
        int amount = 1000;

        assertThrows(Exception.class, () -> {
            service.withdrawBalance(1L, amount, 1L);
        });
    }*/

    @Test
    void delete() {
        service.delete(1L);
        assertThrows(Exception.class, () -> {
            Account originalAccount = service.getAccount(1L);
        });
    }

    /*@Test
    void deleteAccountsUsingOwnerId() {
        service.deleteAccountsUsingOwnerId(1L);
        List<Account> accounts = service.getAccountByOwnerId(1L);
        assertEquals(accounts.size(), 0);
    }*/
}