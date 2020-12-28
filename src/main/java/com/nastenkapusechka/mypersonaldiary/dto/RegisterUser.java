package com.nastenkapusechka.mypersonaldiary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUser {

    @NotBlank(message = "Email is empty!")
    @Email(message = "Must be a well-formed email address")
    private String email;

    @NotBlank(message = "Password is empty!")
    @Size(min = 8, message = "Too short. <8 symbols")
    private String password;

    @NotBlank(message = "Password is empty!")
    @Size(min = 8, message = "Too short. <8 symbols")
    private String repeatPassword;

    @NotBlank(message = "First name is empty!")
    private String firstName;

    @NotBlank(message = "Last name is empty!")
    private String lastName;

    private String gender;

}
