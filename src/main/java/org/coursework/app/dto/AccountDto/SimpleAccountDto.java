package org.coursework.app.dto.AccountDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.coursework.app.enums.Role;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleAccountDto {
    private Long id;
    private String email;
    private Role role;
}
