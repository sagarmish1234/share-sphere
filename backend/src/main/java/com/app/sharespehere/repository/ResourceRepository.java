package com.app.sharespehere.repository;

import com.app.sharespehere.model.Account;
import com.app.sharespehere.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource,Long> {

    List<Resource> findByAccount(Account account);

}
