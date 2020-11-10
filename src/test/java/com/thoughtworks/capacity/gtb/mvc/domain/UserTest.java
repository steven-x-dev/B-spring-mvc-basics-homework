package com.thoughtworks.capacity.gtb.mvc.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    private static Validator validator;

    @BeforeAll
    private static void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void should_not_allow_username_given_disallowed_characters() {
        User user = User.builder().username("steven+123").password("abcde12345").build();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("username", constraintViolations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void should_not_allow_username_given_too_few_characters() {
        User user = User.builder().username("st").password("abcde12345").build();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("username", constraintViolations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void should_not_allow_username_given_too_many_characters() {
        User user = User.builder().username("steven_12345").password("abcde12345").build();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("username", constraintViolations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void should_not_allow_password_given_too_few_characters() {
        User user = User.builder().username("steven_123").password("abcd").build();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("password", constraintViolations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void should_not_allow_password_given_too_many_characters() {
        User user = User.builder().username("steven_123").password("abcde12345xyz").build();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("password", constraintViolations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void should_create_user_given_correct_username_and_password_without_email() {
        User user = User.builder().username("steven_123").password("abcde12345").build();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    void should_not_allow_email_given_incorrect_email() {
        User user = User.builder().username("steven_123").password("abcde12345").email("12345").build();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("email", constraintViolations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void should_create_user_given_correct_username_and_password_with_correct_email() {
        User user = User.builder().username("steven_123").password("abcde12345").email("steven@tw.com").build();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    void should_not_allow_username_given_null_username() {
        User user = User.builder().password("abcde12345").build();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("username", constraintViolations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void should_not_allow_password_given_null_password() {
        User user = User.builder().username("steven_123").build();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("password", constraintViolations.iterator().next().getPropertyPath().toString());
    }

}
