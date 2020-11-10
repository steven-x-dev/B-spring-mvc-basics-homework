package com.thoughtworks.capacity.gtb.mvc.util;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;

public class Util {

    private static final String CANNOT_BE_NULL = "不能为空";

    private static final String INVALID = "不合法";

    public static String getErrorMessage(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldErrors().get(0);
        return String.format("%s%s", getInvalidFieldName(fieldError.getField()), getErrorType(fieldError));
    }

    public static String getErrorMessage(MissingServletRequestParameterException e) {
        return String.format("%s%s", getInvalidFieldName(e.getParameterName()), CANNOT_BE_NULL);
    }

    public static String getErrorMessage(ConstraintViolationException e) {
        String pathString = e.getConstraintViolations().iterator().next().getPropertyPath().toString();
        return String.format("%s%s",
                getInvalidFieldName(pathString.substring(pathString.lastIndexOf('.') + 1)),
                INVALID);
    }

    private static String getErrorType(FieldError fieldError) {
        return fieldError.getRejectedValue() == null ? CANNOT_BE_NULL : INVALID;
    }

    private static String getInvalidFieldName(String field) {
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

