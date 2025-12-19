package org.coursework.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAdminRequest {
    private String email;
    private String password;
    private String confirmPassword;
    private String adminCode;
}
