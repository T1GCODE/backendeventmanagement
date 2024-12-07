package com.restaurant.resataurant.service.Impl;

import com.restaurant.resataurant.dto.request.UserRequest;
import com.restaurant.resataurant.dto.response.SimpleResponse;
import com.restaurant.resataurant.dto.response.UserResponse;
import com.restaurant.resataurant.entity.User;
import com.restaurant.resataurant.repository.UserRepository;
import com.restaurant.resataurant.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public ResponseEntity<Map<String, String>> saveUser(UserRequest userRequest) {
        try {
            Map<String, String> simpleResponse = new HashMap<>();
            log.info("User registration request is received: {}", userRequest);
            if (userRepository.existsByPhoneNo(userRequest.getPhoneNo())) {
                simpleResponse.put("success", "false");
                simpleResponse.put("message", "Phone number already exists");
            } else if (userRepository.existsByEmail(userRequest.getEmail())) {
                simpleResponse.put("success", "false");
                simpleResponse.put("message", "Email already exists");
            } else {
                User user = User.builder()
                        .firstName(userRequest.getFirstName())
                        .lastName(userRequest.getLastName())
                        .email(userRequest.getEmail())
                        .phoneNo(userRequest.getPhoneNo())
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .build();
                User savedUser = userRepository.save(user);
                simpleResponse.put("success", "true");
                simpleResponse.put("id", savedUser.getId().toString());
                simpleResponse.put("message", "User created successfully and logged in");
            }
            return ResponseEntity.ok(simpleResponse);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }

    @Transactional
    public ResponseEntity<SimpleResponse> updateUser(Long id, UserRequest userRequest) {
        try {
            Optional<User> existingUserOptional = userRepository.findById(id);
            if (existingUserOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleResponse(false, "User not found"));
            }
            User existingUser = existingUserOptional.get();

            if (!userRequest.getEmail().equals(existingUser.getEmail()) && userRepository.existsByEmailIgnoreCase(userRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new SimpleResponse(false, "Email already exists"));
            } else if (!userRequest.getPhoneNo().equals(existingUser.getPhoneNo()) &&
                    userRepository.existsByPhoneNoIgnoreCase(userRequest.getPhoneNo())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new SimpleResponse(false, "Phone number already exists"));
            }
            BeanUtils.copyProperties(userRequest, existingUser, getNullPropertyNames(userRequest));
            userRepository.save(existingUser);

            BeanUtils.copyProperties(userRequest, existingUser, getNullPropertyNames(userRequest));
            return ResponseEntity.ok(new SimpleResponse(true, "User updated successfully"));
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SimpleResponse(false, "An error occurred while updating the user"));
        }
    }

    @Transactional
    public ResponseEntity<SimpleResponse> deleteUser(Long id) {
        try {
            Optional<User> existingUserOptional = userRepository.findById(id);
            if (existingUserOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleResponse(false, "User not found"));
            }
            userRepository.deleteById(id);
            return ResponseEntity.ok(new SimpleResponse(true, "User deleted successfully"));
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SimpleResponse(false, "An error occurred while deleting the user"));
        }
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserResponse userResponse = mapToUserResponse(user);
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    private UserResponse mapToUserResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNo(user.getPhoneNo())
                .build();
    }


    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames.toArray(new String[0]);
    }




}
