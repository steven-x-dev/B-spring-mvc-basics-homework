package com.thoughtworks.capacity.gtb.mvc.controller;

import com.thoughtworks.capacity.gtb.mvc.domain.User;
import com.thoughtworks.capacity.gtb.mvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@RestController
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void create(@RequestBody @Valid User user) {
        userService.add(user);
    }

    @GetMapping(path = "/login")
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public User login(@RequestParam @Pattern(regexp = "^[A-Za-z0-9_]{3,10}$") String username,
                      @RequestParam @Size(min = 5, max = 12) String password) {
        return userService.login(username, password);
    }

}
