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

    @Test
    void should_not_create_user_given_username_with_disallowed_characters() throws Exception {
        User user = User.builder().username("steven+123").password("abcde12345").build();
        mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("用户名不合法")));
    }

    @Test
    void should_not_create_user_given_username_with_too_few_characters() throws Exception {
        User user = User.builder().username("st").password("abcde12345").build();
        mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("用户名不合法")));
    }

    @Test
    void should_not_create_user_given_username_with_too_many_characters() throws Exception {
        User user = User.builder().username("steven_12345").password("abcde12345").build();
        mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("用户名不合法")));
    }

    @Test
    void should_not_create_user_given_password_with_too_few_characters() throws Exception {
        User user = User.builder().username("steven_123").password("abcd").build();
        mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("密码不合法")));
    }

    @Test
    void should_not_create_user_given_password_with_too_many_characters() throws Exception {
        User user = User.builder().username("steven_123").password("abcde12345xyz").build();
        mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("密码不合法")));
    }

    @Test
    void should_not_create_user_given_user_with_incorrect_email() throws Exception {
        User user = User.builder().username("steven_123").password("abcde12345").email("12345").build();
        mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("邮箱地址不合法")));
    }

    @Test
    void should_not_create_user_given_null_username() throws Exception {
        User user = User.builder().password("abcde12345").build();
        mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("用户名不能为空")));
    }

    @Test
    void should_not_create_user_given_null_password() throws Exception {
        User user = User.builder().username("steven_123").build();
        mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("密码不能为空")));
    }

    @Test
    @Order(101)
    void should_create_user_given_correct_username_and_password_without_email() throws Exception {
        User user = User.builder().username("steven_123").password("abcde12345").build();
        mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andDo(result -> registeredUsers.add(user));
    }

    @Test
    @Order(102)
    void should_create_user_given_correct_username_and_password_with_correct_email() throws Exception {
        User user = User.builder().username("steven_456").password("abcde12345").email("steven@tw.com").build();
        mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andDo(result -> registeredUsers.add(user));
    }

    @Test
    @Order(103)
    void should_not_create_user_given_existing_username() throws Exception {
        User user = User.builder().username("steven_123").password("abcde12345").build();
        mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("用户名已存在")));
    }

    @Test
    void should_not_login_given_username_with_disallowed_characters() throws Exception {
        User user = User.builder().username("steven+123").password("abcde12345").build();
        mockMvc.perform(get("/login")
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("用户名不合法")));
    }

    @Test
    void should_not_login_given_username_with_too_few_characters() throws Exception {
        User user = User.builder().username("st").password("abcde12345").build();
        mockMvc.perform(get("/login")
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("用户名不合法")));
    }

    @Test
    void should_not_login_given_username_with_too_many_characters() throws Exception {
        User user = User.builder().username("steven_12345").password("abcde12345").build();
        mockMvc.perform(get("/login")
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("用户名不合法")));
    }

    @Test
    void should_not_login_given_password_with_too_few_characters() throws Exception {
        User user = User.builder().username("steven_123").password("abcd").build();
        mockMvc.perform(get("/login")
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("密码不合法")));
    }

    @Test
    void should_not_login_given_password_with_too_many_characters() throws Exception {
        User user = User.builder().username("steven_123").password("abcde12345xyz").build();
        mockMvc.perform(get("/login")
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("密码不合法")));
    }

    @Test
    void should_not_login_given_null_username() throws Exception {
        User user = User.builder().password("abcde12345").build();
        mockMvc.perform(get("/login")
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("用户名不能为空")));
    }

    @Test
    void should_not_login_given_null_password() throws Exception {
        User user = User.builder().username("steven_123").build();
        mockMvc.perform(get("/login")
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(status().isBadRequest())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("密码不能为空")));
    }

    @Test
    void should_not_login_given_wrong_credentials() throws Exception {
        User user = User.builder().username("steven_789").password("xyz789").build();
        mockMvc.perform(get("/login")
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.code", is(HttpStatus.UNAUTHORIZED.value())))
                .andExpect(jsonPath("$.message", is("用户名或密码错误")));
    }

    @Test
    @Order(201)
    void should_login_given_correct_credentials() throws Exception {
        User user = registeredUsers.get(0);
        mockMvc.perform(get("/login")
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(status().isOk())
                .andExpect(content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(jsonPath("$.id", any(Integer.class)))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

}
