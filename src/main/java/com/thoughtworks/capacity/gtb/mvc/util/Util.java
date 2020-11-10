package com.thoughtworks.capacity.gtb.mvc.util;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

public class Util {

    public static String getInvalidFieldName(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String field = fieldErrors.get(0).getField();
        switch (field) {
            case "username":
                return "用户名";
            case "password":
                return "密码";
            case "email":
                return "邮箱地址";
            default:
                return "用户信息";
        }
    }

    private Util() {}
}

