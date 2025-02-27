package com.cgesgin.expense_tracker_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cgesgin.expense_tracker_api.model.entity.Category;
import com.cgesgin.expense_tracker_api.model.service.ICategoryService;
import com.cgesgin.expense_tracker_api.repository.CategoryRepository;

@Service
public class CategoryService implements ICategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category save(Category category) {

        if (category.getId() != null)
            category.setId(null);

        if (category.getExpenses() == null)
            category.setExpenses(new ArrayList<>());

        return this.categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        Category existCategory = categoryRepository.findById(category.getId())
                .orElse(null);

        if (existCategory == null)
            return null;

        if (existCategory.getExpenses() == null)
            existCategory.setExpenses(new ArrayList<>());

        existCategory.setName(category.getName());
        return categoryRepository.save(existCategory);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public boolean delete(Category category) {

        if (categoryRepository.existsById(category.getId())) {

            categoryRepository.deleteById(category.getId());
            return true;
        }
        return false;
    }

    @Override
    public Category getById(Long id) {
        var category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return null;
        }
        return category;
    }

}
