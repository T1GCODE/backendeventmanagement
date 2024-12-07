package com.restaurant.resataurant.controller;

import com.restaurant.resataurant.dto.request.UserRequest;
import com.restaurant.resataurant.dto.response.SimpleResponse;
import com.restaurant.resataurant.dto.response.UserResponse;
import com.restaurant.resataurant.service.UserService;
import com.restaurant.resataurant.util.EndpointURI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping(value = EndpointURI.USER)
    public ResponseEntity<Map<String, String>> saveUser(@RequestBody UserRequest userRequest) {
        return userService.saveUser(userRequest);
    }

    @GetMapping(value = EndpointURI.USERS)
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping(value = EndpointURI.USER_UPDATE)
    public ResponseEntity<SimpleResponse> updateUser(
            @PathVariable("id") Long id, @RequestBody UserRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }

    @DeleteMapping(value = EndpointURI.USER_BY_ID)
    public ResponseEntity<SimpleResponse> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping(value = EndpointURI.GET_USER_BY_ID)
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }
}
