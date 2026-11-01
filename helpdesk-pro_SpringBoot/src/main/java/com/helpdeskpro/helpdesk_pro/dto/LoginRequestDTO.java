package com.helpdeskpro.helpdesk_pro.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDTO {

    private String email;
    private String password;
}
