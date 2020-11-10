package com.thoughtworks.capacity.gtb.mvc.util;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

public class Util {

    public static String getErrorMessage(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldErrors().get(0);
        return String.format("%s%s", getInvalidFieldName(fieldError), getErrorType(fieldError));
    }

    private static String getErrorType(FieldError fieldError) {
        return fieldError.getRejectedValue() == null ? "不能为空" : "不合法";
    }

    private static String getInvalidFieldName(FieldError fieldError) {
        String field = fieldError.getField();
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

