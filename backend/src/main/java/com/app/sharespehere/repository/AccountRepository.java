package com.app.sharespehere.repository;

import com.app.sharespehere.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    boolean existsByEmail(String email);

    Optional<Account> findByEmail(String email);
}
