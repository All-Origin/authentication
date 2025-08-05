package com.Junior.authentication.dto;

import com.Junior.authentication.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserValidationResponse {
    private Long userId;
    private String userName;
    private String email;
    private List<Roles> roles;

}
