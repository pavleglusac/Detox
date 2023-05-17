package com.sbnz.admin.auth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    @Email
    private String email;
    @NotBlank
    @Size(min = 12)
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String role;
}
