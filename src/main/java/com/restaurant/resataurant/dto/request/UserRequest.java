package com.restaurant.resataurant.dto.request;

import lombok.Data;

@Data
public class UserRequest {

    public String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;

}
