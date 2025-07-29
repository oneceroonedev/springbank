package com.springbank.system.model.users;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {
    public Admin() {}

    public Admin(String name) {
        super(name);
    }
}