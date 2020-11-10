package com.thoughtworks.capacity.gtb.mvc.service;

import com.thoughtworks.capacity.gtb.mvc.domain.User;
import com.thoughtworks.capacity.gtb.mvc.exception.UsernameExistsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private static final List<User> users = new ArrayList<>();

    public void add(User user) {
        String username = user.getUsername();
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            throw new UsernameExistsException();
        }
        users.add(new User(user.getUsername(), user.getPassword(), user.getEmail()));
    }
}
