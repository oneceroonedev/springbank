package com.springbank.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbank.system.model.Address;
import com.springbank.system.model.users.AccountHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAccountHolder_ValidRequest_ReturnsCreatedHolder() throws Exception {
        AccountHolder holder = new AccountHolder();
        holder.setName("Test User");
        holder.setDateOfBirth(LocalDate.of(2000, 1, 1));
        holder.setPrimaryAddress(new Address("Main St", "City", "12345", "Spain"));

        mockMvc.perform(post("/admin/account-holders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(holder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"));
    }
}