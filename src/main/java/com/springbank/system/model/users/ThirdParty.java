package com.springbank.system.model.users;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class ThirdParty extends User {

    private String hashedKey;

    public ThirdParty() {}

    public ThirdParty(String name, String hashedKey) {
        super(name);
        this.hashedKey = hashedKey;
    }
}