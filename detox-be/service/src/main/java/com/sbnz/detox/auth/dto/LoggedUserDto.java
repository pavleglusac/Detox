package com.sbnz.detox.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedUserDto {
    private UUID id;
    private String name;
    private String email;
    private String imageUrl;
    private String role;
}
