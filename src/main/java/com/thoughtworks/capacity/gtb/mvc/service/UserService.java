package com.thoughtworks.capacity.gtb.mvc.service;

import com.thoughtworks.capacity.gtb.mvc.domain.User;
import com.thoughtworks.capacity.gtb.mvc.exception.AuthenticationFailedException;
import com.thoughtworks.capacity.gtb.mvc.exception.UsernameExistsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final List<User> users = new ArrayList<>();

    public void add(User user) {
        String username = user.getUsername();
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            throw new UsernameExistsException();
        }
        User copy = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();
        copy.setId(users.size() + 1);
        users.add(copy);
    }

    public User login(String username, String password) {
        Optional<User> optionalUser = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password)).findAny();
        if (optionalUser.isPresent()) {
            User retrieved = optionalUser.get();
            return User.builder()
                    .id(retrieved.getId())
                    .username(retrieved.getUsername())
                    .password(retrieved.getPassword())
                    .email(retrieved.getEmail())
                    .build();
        }
        throw new AuthenticationFailedException();
    }
}
