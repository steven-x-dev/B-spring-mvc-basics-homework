package com.thoughtworks.capacity.gtb.mvc.controller;

import org.springframework.test.web.servlet.ResultActions;

@FunctionalInterface
public interface Validator {

    void validate(ResultActions resultActions) throws Exception;

}
