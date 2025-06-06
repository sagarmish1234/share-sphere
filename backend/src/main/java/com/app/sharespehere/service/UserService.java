package com.app.sharespehere.service;

import com.app.sharespehere.dto.UserDto;
import com.app.sharespehere.model.User;
import com.app.sharespehere.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public boolean userExists(String userEmail) {
        return userRepository.existsByEmail(userEmail);
    }

    public void saveUser(UserDto userDto) {
        User user = User.builder()
                .email(userDto.email())
                .name(userDto.name())
                .city(userDto.city())
                .state(userDto.state())
                .address(userDto.address())
                .phone(userDto.phone())
                .build();
        this.saveUser(user);
    }

    public void saveUser(OAuth2User principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        User user = User.builder()
                .email(email)
                .name(name)
                .build();
        this.saveUser(user);
    }

    public void updateUser(UserDto userDto) {
        User user = this.getUser(userDto.email()).orElseThrow(() -> new UsernameNotFoundException(String.format("Specified user not found %s", userDto.email())));
        user.setCity(userDto.city());
        user.setAddress(userDto.address());
        user.setPhone(userDto.phone());
        this.saveUser(user);
    }

    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }


}
