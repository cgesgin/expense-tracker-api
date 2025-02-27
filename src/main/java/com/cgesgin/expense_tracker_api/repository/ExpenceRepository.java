package com.cgesgin.expense_tracker_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cgesgin.expense_tracker_api.model.entity.Expense;

public interface ExpenceRepository extends JpaRepository<Expense,Long> {
    
}
