package com.cgesgin.expense_tracker_api.model.service;

import java.util.List;

import com.cgesgin.expense_tracker_api.model.entity.Expense;

public interface IExpenseService {
    
    Expense save(Expense expense);
    Expense update(Expense expense);
    List<Expense> getAll();
    boolean delete(Expense expense);
    Expense getById(Long id);

}
