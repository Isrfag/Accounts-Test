package com.microcompany.accountsservice.services;

import com.microcompany.accountsservice.exception.AccountNotFoundException;
import com.microcompany.accountsservice.exception.CustomerNotFoundException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.model.Customer;
import com.microcompany.accountsservice.persistence.AccountRepository;
import com.microcompany.accountsservice.persistence.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
class IAccountServiceTest {

    @TestConfiguration
    static class AccountServiceConfiguration {
        @Bean
        public AccountService accountService () {
            return new AccountService();
        }
    }
    @BeforeEach
    public void SetUp () {
        Customer acustomer = new Customer(1l, "Reimon","RemonGmail.com");
        List<Account> accounts = List.of(new Account(1l,"Company",LocalDate.now(),1000,1l));
        Mockito.when(customerRepository.findById(1l)).thenReturn(Optional.of(acustomer));
        Mockito.when(accountRepo.findByOwnerId(1l)).thenReturn(accounts);

        List<Account> accountsFailed = List.of(new Account(1l,"Company",LocalDate.now(),1000,10l));
        Mockito.when(customerRepository.findById(10l)).thenReturn(Optional.of(acustomer));
        Mockito.when(accountRepo.findByOwnerId(10l)).thenThrow(new AccountNotFoundException(10l));

    }
    @Autowired
    AccountService service;
    @MockBean
    AccountRepository accountRepo;

    @MockBean
    CustomerRepository customerRepository;

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
    void givenACustomerId_WhenCustomerIdIsValid_ThenReturnAccounts() {
        assertThat(service).isNotNull();
        List<Account> accounts = service.getAllAccountByOwnerId(1l);
        assertThat(accounts).isNotNull().isNotEmpty();

    }

    @Test
    void givenACustomerId_WhenCustomerIdNotExist_ThenReturnException() {
        assertThat(service).isNotNull();
        assertThrows(AccountNotFoundException.class, () -> service.getAllAccountByOwnerId(10l));
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