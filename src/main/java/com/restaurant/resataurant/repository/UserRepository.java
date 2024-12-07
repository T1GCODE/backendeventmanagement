package com.restaurant.resataurant.repository;

import com.restaurant.resataurant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNo(String phoneNo);

    boolean existsByPhoneNoIgnoreCase(String nicNo);
}
