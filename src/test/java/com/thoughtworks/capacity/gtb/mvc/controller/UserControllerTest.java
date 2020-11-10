package com.thoughtworks.capacity.gtb.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.capacity.gtb.mvc.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

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
    void should_create_user_given_correct_username_and_password_without_email() throws Exception {
        User user = User.builder().username("steven_123").password("abcde12345").build();
        mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    void should_not_allow_create_user_given_user_with_incorrect_email() throws Exception {
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
    void should_create_user_given_user_with_correct_details() throws Exception {
        User user = User.builder().username("steven_123").password("abcde12345").email("steven@tw.com").build();
        mockMvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

}
