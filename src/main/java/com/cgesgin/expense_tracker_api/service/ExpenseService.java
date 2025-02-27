package com.cgesgin.expense_tracker_api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cgesgin.expense_tracker_api.model.entity.Expense;
import com.cgesgin.expense_tracker_api.model.entity.User;
import com.cgesgin.expense_tracker_api.model.service.IExpenseService;
import com.cgesgin.expense_tracker_api.repository.ExpenceRepository;
import com.cgesgin.expense_tracker_api.repository.UserRepository;

@Service
public class ExpenseService implements IExpenseService {

    private ExpenceRepository expenceRepository;
    private UserRepository userRepository;

    public ExpenseService(ExpenceRepository expenceRepository, UserRepository userRepository) {
        this.expenceRepository = expenceRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public Expense save(Expense expense) {

        User user = getAuthenticatedUser();

        if (expense.getId() != null)
            expense.setId(null);

        if (expense.getCategories() == null)
            expense.setCategories(new ArrayList<>());

        if (user != null) {
            expense.setUser(user);
            System.out.println(expense.toString());
            return expenceRepository.save(expense);
        }
        return null;
    }

    @Override
    public Expense update(Expense expense) {
        User user = getAuthenticatedUser();
        if (user != null && expenceRepository.findById(expense.getId()).isPresent()) {
            expense.setUser(user);
            return expenceRepository.save(expense);
        }
        return null;
    }

    @Override
    public List<Expense> getAll() {
        User user = getAuthenticatedUser();
        if (user != null) {
            return expenceRepository.findAll()
                    .stream()
                    .filter(expense -> expense.getUser().getId().equals(user.getId()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public boolean delete(Expense expense) {
        User user = getAuthenticatedUser();
        if (user != null && expenceRepository.findById(expense.getId()).isPresent()) {
            expenceRepository.delete(expense);
            return true;
        }
        return false;
    }

    @Override
    public Expense getById(Long id) {
        User user = getAuthenticatedUser();
        return expenceRepository.findById(id)
                .filter(expense -> expense.getUser().getId().equals(user.getId()))
                .orElse(null);
    }

}