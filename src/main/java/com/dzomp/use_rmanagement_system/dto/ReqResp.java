package com.dzomp.use_rmanagement_system.dto;

import com.dzomp.use_rmanagement_system.entities.OurUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = false)
public class ReqResp {

    private int statusCode;
    private String message;
    private String error;
    private String refreshToken;
    private String token;
    private String expirationTime;
    private String name;
    private String city;
    private String role;
    private String password;
    private String email;
    private OurUser ourUser;
    private List<OurUser> ourUserList;


}
