package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.microcompany.accountsservice.persistence.AccountRepository;
import com.microcompany.accountsservice.services.AccountService;
import java.util.List;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
public class AccountControllerWebMvc {

    @BeforeEach
    public void setUp() {

        List<Account> accounts = List.of(new Account(null,"Company", LocalDate.now(), 100, 1L));
        Mockito.when(accountService.getAllAccountByOwnerId(1L)).thenReturn(accounts);

        //Caso null
        /*List<Account> accounts = List.of();
        Mockito.when(accountService.getAllAccountByOwnerId(1L)).thenReturn(accounts);*/

    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    public void givenAccounts_whenGetAccount_thenStatus200() throws Exception {
        MvcResult result = mockMvc.perform(get("/account/all/1").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[*].type", hasItem("Company")))
                .andReturn();
    }

    @Test
    public void givenEmptyAccounts_whenGetAccount_thenIsEmpty() throws Exception {
        MvcResult result = mockMvc.perform(get("/account/all/1").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("[]"))
                .andReturn();
    }



}
