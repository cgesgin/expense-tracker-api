package com.cgesgin.expense_tracker_api.model.service;

import java.util.List;

import com.cgesgin.expense_tracker_api.model.entity.Category;

public interface ICategoryService {

    Category save(Category category);
    Category update(Category category);
    List<Category> getAll();
    boolean delete(Category category);
    Category getById(Long id);

}
