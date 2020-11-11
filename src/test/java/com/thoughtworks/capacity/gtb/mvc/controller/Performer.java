package com.thoughtworks.capacity.gtb.mvc.controller;

import org.springframework.test.web.servlet.ResultActions;

@FunctionalInterface
public interface Performer {

    ResultActions perform() throws Exception;

    default ResultActions andThen(Validator after) throws Exception {
        ResultActions resultActions = perform();
        after.validate(resultActions);
        return resultActions;
    }
}
