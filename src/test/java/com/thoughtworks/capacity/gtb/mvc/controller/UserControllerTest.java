package com.thoughtworks.capacity.gtb.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.capacity.gtb.mvc.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    private static final List<User> registeredUsers = new ArrayList<>();

    @BeforeAll
    private static void setUp() {
        objectMapper = new ObjectMapper();
    }

    private Performer register(User user) {
        return () -> mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)));
    }

    private Performer login(String username, String password) {
        return () -> mockMvc.perform(get("/login")
                .param("username", username)
                .param("password", password)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name()));
    }

    private Validator validateError(HttpStatus httpStatus, String errorMessage) {
        return resultActions -> resultActions
                    .andExpect(status().is(httpStatus.value()))
                    .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                    .andExpect(jsonPath("$.code", is(httpStatus.value())))
                    .andExpect(jsonPath("$.message", is(errorMessage)));
    }

    private Validator validateRegisterSuccess() {
        return resultActions -> resultActions.andExpect(status().is(HttpStatus.CREATED.value()));
    }

    private Validator validateLoginSuccess(User user) {
        return resultActions -> resultActions
                .andExpect(status().isOk())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.id", any(Integer.class)))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void should_not_create_user_given_username_with_disallowed_characters() throws Exception {
        User user = User.builder().username("steven+123").password("abcde12345").build();
        register(user).andThen(validateError(HttpStatus.BAD_REQUEST, "用户名不合法"));
    }

    @Test
    void should_not_create_user_given_username_with_too_few_characters() throws Exception {
        User user = User.builder().username("st").password("abcde12345").build();
        register(user).andThen(validateError(HttpStatus.BAD_REQUEST, "用户名不合法"));
    }

    @Test
    void should_not_create_user_given_username_with_too_many_characters() throws Exception {
        User user = User.builder().username("steven_12345").password("abcde12345").build();
        register(user).andThen(validateError(HttpStatus.BAD_REQUEST, "用户名不合法"));
    }

    @Test
    void should_not_create_user_given_password_with_too_few_characters() throws Exception {
        User user = User.builder().username("steven_123").password("abcd").build();
        register(user).andThen(validateError(HttpStatus.BAD_REQUEST, "密码不合法"));
    }

    @Test
    void should_not_create_user_given_password_with_too_many_characters() throws Exception {
        User user = User.builder().username("steven_123").password("abcde12345xyz").build();
        register(user).andThen(validateError(HttpStatus.BAD_REQUEST, "密码不合法"));
    }

    @Test
    void should_not_create_user_given_user_with_incorrect_email() throws Exception {
        User user = User.builder().username("steven_123").password("abcde12345").email("12345").build();
        register(user).andThen(validateError(HttpStatus.BAD_REQUEST, "邮箱地址不合法"));
    }

    @Test
    void should_not_create_user_given_null_username() throws Exception {
        User user = User.builder().password("abcde12345").build();
        register(user).andThen(validateError(HttpStatus.BAD_REQUEST, "用户名不能为空"));
    }

    @Test
    void should_not_create_user_given_null_password() throws Exception {
        User user = User.builder().username("steven_123").build();
        register(user).andThen(validateError(HttpStatus.BAD_REQUEST, "密码不能为空"));
    }

    @Test
    @Order(101)
    void should_create_user_given_correct_username_and_password_without_email() throws Exception {
        User user = User.builder().username("steven_123").password("abcde12345").build();
        register(user)
                .andThen(validateRegisterSuccess())
                .andDo(result -> registeredUsers.add(user));
    }

    @Test
    @Order(102)
    void should_create_user_given_correct_username_and_password_with_correct_email() throws Exception {
        User user = User.builder().username("steven_456").password("abcde12345").email("steven@tw.com").build();
        register(user)
                .andThen(validateRegisterSuccess())
                .andDo(result -> registeredUsers.add(user));
    }

    @Test
    @Order(103)
    void should_not_create_user_given_existing_username() throws Exception {
        User user = User.builder().username("steven_123").password("abcde12345").build();
        register(user).andThen(validateError(HttpStatus.BAD_REQUEST, "用户名已存在"));
    }

    @Test
    void should_not_login_given_username_with_disallowed_characters() throws Exception {
        login("steven+123", "abcde12345").andThen(validateError(HttpStatus.BAD_REQUEST, "用户名不合法"));
    }

    @Test
    void should_not_login_given_username_with_too_few_characters() throws Exception {
        login("st", "abcde12345").andThen(validateError(HttpStatus.BAD_REQUEST, "用户名不合法"));
    }

    @Test
    void should_not_login_given_username_with_too_many_characters() throws Exception {
        login("steven_12345", "abcde12345").andThen(validateError(HttpStatus.BAD_REQUEST, "用户名不合法"));
    }

    @Test
    void should_not_login_given_password_with_too_few_characters() throws Exception {
        login("steven_123", "abcd").andThen(validateError(HttpStatus.BAD_REQUEST, "密码不合法"));
    }

    @Test
    void should_not_login_given_password_with_too_many_characters() throws Exception {
        login("steven_123", "abcde12345xyz").andThen(validateError(HttpStatus.BAD_REQUEST, "密码不合法"));
    }

    @Test
    void should_not_login_given_null_username() throws Exception {
        login(null, "abcde12345xyz").andThen(validateError(HttpStatus.BAD_REQUEST, "用户名不能为空"));
    }

    @Test
    void should_not_login_given_null_password() throws Exception {
        login("steven_123", null).andThen(validateError(HttpStatus.BAD_REQUEST, "密码不能为空"));
    }

    @Test
    void should_not_login_given_wrong_credentials() throws Exception {
        login("steven_789", "xyz789").andThen(validateError(HttpStatus.UNAUTHORIZED, "用户名或密码错误"));
    }

    @Test
    @Order(201)
    void should_login_given_correct_credentials() throws Exception {
        User user = registeredUsers.stream().filter(u -> u.getEmail() != null).collect(Collectors.toList()).get(0);
        login(user.getUsername(), user.getPassword()).andThen(validateLoginSuccess(user));
    }

}
