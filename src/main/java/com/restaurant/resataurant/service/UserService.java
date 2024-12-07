package com.restaurant.resataurant.service;


import com.restaurant.resataurant.dto.request.UserRequest;
import com.restaurant.resataurant.dto.response.SimpleResponse;
import com.restaurant.resataurant.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<Map<String, String>> saveUser(UserRequest userRequest);

    ResponseEntity<List<UserResponse>> getAllUsers();

    ResponseEntity<SimpleResponse> updateUser(Long id, UserRequest userRequest);

    ResponseEntity<SimpleResponse> deleteUser(Long id);

    ResponseEntity<UserResponse> getUserById(Long id);

}
