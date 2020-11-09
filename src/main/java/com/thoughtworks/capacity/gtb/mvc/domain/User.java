package com.thoughtworks.capacity.gtb.mvc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Pattern(regexp = "^[A-Za-z0-9_]{3,10}$")
    @NotNull
    private String username;

    @Size(min = 5, max = 12)
    @NotNull
    private String password;

    @Email
    private String email;

}
