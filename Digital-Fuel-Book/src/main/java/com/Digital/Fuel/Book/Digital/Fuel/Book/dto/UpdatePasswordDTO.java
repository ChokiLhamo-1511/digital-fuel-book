package com.Digital.Fuel.Book.Digital.Fuel.Book.dto;

import lombok.Data;

@Data

public class UpdatePasswordDTO {

    private String email;
    private String newPassword;
    private String confirmPassword;
}