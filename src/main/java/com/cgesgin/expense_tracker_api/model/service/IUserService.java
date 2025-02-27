package com.cgesgin.expense_tracker_api.model.service;

import com.cgesgin.expense_tracker_api.model.entity.User;

public interface IUserService {
    
    User save(User user);
    boolean findByUsername(String username);
    
}
