package com.bci.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Response {

    private UUID id;
    private String username;
    private String email;
    private String password;
    private String created;
    private List<PhoneDTO> phones;
    private Boolean isActive;
    private String lastLogin;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

}
