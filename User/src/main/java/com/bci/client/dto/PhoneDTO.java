package com.bci.client.dto;

import lombok.Data;

@Data
public class PhoneDTO {

    private Long number;
    private Integer citycode;
    private String contrycode;
}
