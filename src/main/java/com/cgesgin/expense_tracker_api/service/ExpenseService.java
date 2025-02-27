package com.cgesgin.expense_tracker_api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cgesgin.expense_tracker_api.model.entity.Expense;
import com.cgesgin.expense_tracker_api.model.entity.User;
import com.cgesgin.expense_tracker_api.model.service.IExpenseService;
import com.cgesgin.expense_tracker_api.repository.ExpenseRepository;
import com.cgesgin.expense_tracker_api.repository.UserRepository;

@Service
public class ExpenseService implements IExpenseService {

    private ExpenseRepository expenseRepository;
    private UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
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
            return expenseRepository.save(expense);
        }
        return null;
    }

    @Override
    public Expense update(Expense expense) {
        User user = getAuthenticatedUser();
        if (user != null && expenseRepository.findById(expense.getId()).isPresent()) {
            expense.setUser(user);
            return expenseRepository.save(expense);
        }
        return null;
    }

    @Override
    public List<Expense> getAll() {
        User user = getAuthenticatedUser();
        if (user != null) {
            return expenseRepository.findAll()
                    .stream()
                    .filter(expense -> expense.getUser().getId().equals(user.getId()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public boolean delete(Expense expense) {
        User user = getAuthenticatedUser();
        if (user != null && expenseRepository.findById(expense.getId()).isPresent()) {
            expenseRepository.delete(expense);
            return true;
        }
        return false;
    }

    @Override
    public Expense getById(Long id) {
        User user = getAuthenticatedUser();
        return expenseRepository.findById(id)
                .filter(expense -> expense.getUser().getId().equals(user.getId()))
                .orElse(null);
    }

    @Override
    public List<Expense> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return expenseRepository.findByDateRange(startDate, endDate);
    }

    public List<Expense> getExpensesByPeriod(String period) {
        
        LocalDateTime startDate = null;
        LocalDateTime endDate = LocalDateTime.now();

        if ("past_week".equals(period)) {
            startDate = LocalDateTime.now().minusWeeks(1);
        } else if ("past_month".equals(period)) {
            startDate = LocalDateTime.now().minusMonths(1);
        } else if ("last_3_months".equals(period)) {
            startDate = LocalDateTime.now().minusMonths(3);
        } else {
            return expenseRepository.findAll();
        }

        return expenseRepository.findByDateRange(startDate, endDate);
    }

}