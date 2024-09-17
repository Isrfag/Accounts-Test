package com.microcompany.accountsservice.persistence;

import com.microcompany.accountsservice.model.Account;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest()
@Sql(value = "classpath:data_testing.sql")
class AccountRepositoryTest {

    @Autowired
    AccountRepository repository;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    @Order(1)
    void createAccounts_WhenAccountIsValid(){
        Account account = new Account(null, "Personal", LocalDate.now(), 100, 1L);
        assertThat(repository.save(account)).isNotNull();
    }

    @Test
    void createAccounts_WhenAccountIsNotValid(){
        Account account = null;
        assertThrows(Exception.class,() ->
            repository.save(account)
        );
    }

    @Test
    void givenAccount_WhenAidIsNotNull(){
        assertThat(repository.findById(1L).get()).isNotNull();
    }

    @Test
    void givenAccount_WhenAidIsNull(){
        assertThrows(Exception.class,() ->
                repository.findById(100L).get()
        );
    }

    @Test
    void givenAccountsByOwnerId_WhenAidIsNotNull(){
        List<Account> accounts = repository.findByOwnerId(1L);
        assertThat(accounts).isNotNull().isNotEmpty();
    }

    @Test
    void givenAccountsByOwnerId_WhenAidIsNull(){
        List<Account> accounts = null;
        assertThat(accounts).isNull();
    }

    @Test
    void givenAccountsByOwnerId_WhenAidIsEmpty(){
        List<Account> accounts = repository.findByOwnerId(5L);
        assertThat(accounts).isEmpty();
    }

    @Test
    void updateAccountByOwnerId_WhenAccountIsNotNull(){
        Account account = repository.findById(1L).get();
        account.setType("Company");
        assertThat(repository.save(account)).isNotNull();
    }

    @Test
    void updateAccountByOwnerId_WhenAccountIsNull(){
        Account account = null;
        assertThrows(Exception.class,() ->
            repository.save(account)
        );
    }

    @Test
    void updateAccountByOwnerId_WhenAccountToUpdateDoesExist(){
        assertThrows(Exception.class,() ->
           repository.findById(40L).get()
        );
    }

    @Test
    void updateAccountByOwnerId_WhenCustomerIsNull(){
        Long customerId = null;
        assertThrows(Exception.class,() ->
            customerRepository.findById(customerId)
        );
    }

    @Test
    void removeAccountByOwnerId_WhenAccountIsNotNull(){
        assertTrue(repository.findById(1L).isPresent());
        repository.deleteById(1L);
        assertFalse(repository.findById(1L).isPresent());
    }

    @Test
    void removeAccountsByOwnerId_WhenAccountIsNotNull(){
        assertThat(repository.findByOwnerId(1L).size()).isGreaterThan(0);
        repository.deleteByOwnerId(1L);
        assertThat(repository.findByOwnerId(1L).size()).isLessThan(1);
    }

    @Test
    void removeAccountByOwnerId_WhenAccountIsNull(){
        Long account = null;
        assertThrows(Exception.class,() ->
            repository.findById(account)
        );
    }

    @Test
    void removeAccountsByOwnerId_WhenCustomerIsNull(){
        assertThrows(Exception.class,() ->
           customerRepository.findById(40L).get()
        );
    }

}