package com.entradas_cine.ffe.rest.users.services;

import com.entradas_cine.ffe.rest.users.dto.UserInfoResponse;
import com.entradas_cine.ffe.rest.users.dto.UserRequest;
import com.entradas_cine.ffe.rest.users.dto.UserResponse;
import com.entradas_cine.ffe.rest.users.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UsersService {

    Page<UserResponse> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable);

    UserInfoResponse findById(Long id);

    UserResponse save(UserRequest userRequest);

    UserResponse update(Long id, UserRequest userRequest);

    void deleteById(Long id);

    List<User> findAllActiveUsers();

    Optional<User> findByUsername(String username);
    void save(User user);
}
