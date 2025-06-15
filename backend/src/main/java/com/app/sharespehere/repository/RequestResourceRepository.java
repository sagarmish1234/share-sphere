package com.app.sharespehere.repository;

import com.app.sharespehere.model.Account;
import com.app.sharespehere.model.RequestResource;
import com.app.sharespehere.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RequestResourceRepository extends JpaRepository<RequestResource, Long> {

    boolean existsByResourceAndBorrower(Resource resource, Account account);

    List<RequestResource> findByOwner(Account owner);

    List<RequestResource> findByBorrower(Account owner);

}
