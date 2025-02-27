package com.cgesgin.expense_tracker_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cgesgin.expense_tracker_api.model.dto.DataResponse;
import com.cgesgin.expense_tracker_api.model.entity.Category;
import com.cgesgin.expense_tracker_api.model.service.ICategoryService;
import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Category", description = "API for category operations")
public class CategoryController {
    
    private ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    public ResponseEntity<DataResponse<Category>> createCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.save(category);
        DataResponse<Category> response = new DataResponse<>();
        if (savedCategory == null) {
            response.setMessage(HttpStatus.BAD_REQUEST.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.setData(savedCategory);
        response.setMessage(HttpStatus.CREATED.toString());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<DataResponse<Category>> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        Category updatedCategory = categoryService.update(category);
        DataResponse<Category> response = new DataResponse<>();
        if (updatedCategory == null) {
            response.setMessage(HttpStatus.NOT_FOUND.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.setData(updatedCategory);
        response.setMessage(HttpStatus.OK.toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories")
    public ResponseEntity<DataResponse<List<Category>>> getAllCategories() {
        List<Category> categories = categoryService.getAll();
        DataResponse<List<Category>> response = new DataResponse<>();
        response.setData(categories);
        response.setMessage(HttpStatus.OK.toString());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<DataResponse<Void>> deleteCategory(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        DataResponse<Void> response = new DataResponse<>();
        if (category == null) {
            response.setMessage(HttpStatus.NOT_FOUND.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        categoryService.delete(category);
        response.setMessage(HttpStatus.NO_CONTENT.toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<DataResponse<Category>> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        DataResponse<Category> response = new DataResponse<>();
        if (category == null) {
            response.setMessage(HttpStatus.NOT_FOUND.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.setData(category);
        response.setMessage(HttpStatus.OK.toString());
        return ResponseEntity.ok(response);
    }
}
