package com.springbank.system.model.users;

import com.springbank.system.model.Address;
import com.springbank.system.model.accounts.Account;
import com.springbank.system.repository.AccountHolderRepository;
import com.springbank.system.repository.AccountRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
public class AccountHolder extends User {

    private LocalDate dateOfBirth;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "primary_street")),
            @AttributeOverride(name = "city", column = @Column(name = "primary_city")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "primary_postal_code")),
            @AttributeOverride(name = "country", column = @Column(name = "primary_country"))
    })
    private Address primaryAddress;

    @Embedded
    private Address mailingAddress;

    public AccountHolder() {}

    public AccountHolder(String name, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress) {
        super(name);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }
}