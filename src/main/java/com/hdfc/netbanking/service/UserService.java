package com.hdfc.netbanking.service;

import org.springframework.stereotype.Service;

import com.hdfc.netbanking.dto.RegisterRequest;
import com.hdfc.netbanking.entity.User;
import com.hdfc.netbanking.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(RegisterRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());

        user.setRole("USER");

        return userRepository.save(user);
    }
}