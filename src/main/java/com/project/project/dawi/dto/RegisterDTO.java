package com.project.project.dawi.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterDTO {

    private String firstName;
    private String lastName;
    private String dni;
    private String phoneNumber;
    private LocalDate hireDate;
    private String email;
    private String password;
}
