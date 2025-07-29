package com.springbank.system.repository;

import com.springbank.system.model.accounts.Account;
import com.springbank.system.model.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.primaryOwner.id = :ownerId OR (a.secondaryOwner IS NOT NULL AND a.secondaryOwner.id = :ownerId)")
    List<Account> findAllByOwnerId(@Param("ownerId") Long ownerId);
}