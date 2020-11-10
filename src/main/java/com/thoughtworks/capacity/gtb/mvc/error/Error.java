package com.thoughtworks.capacity.gtb.mvc.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error {

    private int code;

    private String message;

}
