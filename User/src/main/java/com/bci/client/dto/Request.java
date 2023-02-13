package com.bci.client.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class Request {

    private String username;
    @NotBlank(message = "This field is mandatory")
    @Email(message = "Wrong email format. Must be 'aaaaaa@domain.something'",regexp ="^(.+)@(\\S+)$")
    private String email;
    @NotBlank(message = "This field is mandatory")
    @Pattern(message = "Wrong password format, must have only one Capital Letter and only two numbers (not necessarily\n" +
            "consecutive), in combination of lower case letters, maximum length of 12 and minimum 8",
            regexp ="^(?=.*?[A-Z])(?=.*?[\\d].*?[\\d]).{8,12}$")
    private String password;
    private List<PhoneDTO> phones;

}
