package com.cgesgin.expense_tracker_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cgesgin.expense_tracker_api.model.entity.Category;

public interface CategoryRepository extends JpaRepository<Category,Long>{
    
}
