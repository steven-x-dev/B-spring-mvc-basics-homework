package com.thoughtworks.capacity.gtb.mvc.exception;

import com.thoughtworks.capacity.gtb.mvc.util.Util;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleInvalidArgumentException(Exception e) {
        String message;
        if (e instanceof MethodArgumentNotValidException) {
            message = Util.getErrorMessage((MethodArgumentNotValidException) e);
        } else if (e instanceof MissingServletRequestParameterException) {
            message = Util.getErrorMessage((MissingServletRequestParameterException) e);
        } else {
            message = Util.getErrorMessage((ConstraintViolationException) e);
        }
        return new Error(HttpStatus.BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(UsernameExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleUsernameExistsException() {
        return new Error(HttpStatus.BAD_REQUEST.value(), "用户名已存在");
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Error handleAuthenticationFailedException() {
        return new Error(HttpStatus.UNAUTHORIZED.value(), "用户名或密码错误");
    }

}
