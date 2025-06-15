package com.app.sharespehere.service;

import com.app.sharespehere.dto.AddressAndPhoneDto;
import com.app.sharespehere.dto.AccountDto;
import com.app.sharespehere.model.Account;
import com.app.sharespehere.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;



    public Account getAccount(OAuth2User userPrincipal) {
        String email = userPrincipal.getAttribute("email");
        return this.getUser(email);
    }


    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public boolean userExists(String userEmail) {
        return accountRepository.existsByEmail(userEmail);
    }

    public void saveAccount(AccountDto accountDto) {
        Account account = Account.builder()
                .email(accountDto.email())
                .name(accountDto.name())
                .city(accountDto.city())
                .state(accountDto.state())
                .address(accountDto.address())
                .phone(accountDto.phone())
                .build();
        this.saveAccount(account);
    }

    public void saveAccount(OAuth2User principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        Account account = Account.builder()
                .email(email)
                .name(name)
                .build();
        this.saveAccount(account);
        log.info("User saved");

    }

    public void updateUser(AccountDto accountDto) {
        Account account = this.getUser(accountDto.email());
        account.setCity(accountDto.city());
        account.setState(account.getAddress());
        account.setAddress(accountDto.address());
        account.setPhone(accountDto.phone());
        this.saveAccount(account);
    }

    public Account getUser(String email) {
        return accountRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException(String.format("Username %s not found",email)));
    }


    public void saveAddressAndPhone(AddressAndPhoneDto addressAndPhoneDto, OAuth2User userPrincipal) {
        Account account = this.getAccount(userPrincipal);
        account.setAddress(addressAndPhoneDto.address());
        account.setCity(addressAndPhoneDto.city());
        account.setState(addressAndPhoneDto.address());
        account.setPhone(addressAndPhoneDto.phone());
        this.saveAccount(account);
    }

    public AccountDto getProfile(OAuth2User userPrincipal){
        Account account = this.getAccount(userPrincipal);
        return AccountDto.builder()
                .email(account.getEmail())
                .phone(account.getPhone())
                .state(account.getState())
                .address(account.getAddress())
                .city(account.getCity())
                .name(account.getName())
                .build();
    }
}
