package com.cgesgin.expense_tracker_api.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cgesgin.expense_tracker_api.model.entity.User;
import com.cgesgin.expense_tracker_api.model.service.IUserService;
import com.cgesgin.expense_tracker_api.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements IUserService{

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {

        if (user.getId()==null) {
            user.setId(null);
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var savedUser = userRepository.save(user);

        savedUser.setPassword("********");

        return savedUser;   
    }

    @Override
    public boolean findByUsername(String username) {

        Optional<User> byUsername = userRepository.findByUsername(username);
        
        if(byUsername.isPresent()) {
            return true;
        }
        return false;
    }
    
}
