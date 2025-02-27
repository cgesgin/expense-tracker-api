package com.cgesgin.expense_tracker_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cgesgin.expense_tracker_api.model.dto.DataResponse;
import com.cgesgin.expense_tracker_api.model.entity.Category;
import com.cgesgin.expense_tracker_api.model.entity.Expense;
import com.cgesgin.expense_tracker_api.model.service.ICategoryService;
import com.cgesgin.expense_tracker_api.model.service.IExpenseService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Expense", description = "API for expense operations")
public class ExpenseController {

    private final IExpenseService expenseService;
    private final ICategoryService categoryService;

    public ExpenseController(IExpenseService expenseService, ICategoryService categoryService) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
    }

    @PostMapping("/expenses")
    public ResponseEntity<DataResponse<Expense>> createExpense(@RequestBody Expense expense) {
        Expense savedExpense = expenseService.save(expense);
        DataResponse<Expense> response = new DataResponse<>();
        if (savedExpense == null) {
            response.setMessage(HttpStatus.BAD_REQUEST.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.setData(savedExpense);
        response.setMessage(HttpStatus.CREATED.toString());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/expenses/{id}")
    public ResponseEntity<DataResponse<Expense>> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        expense.setId(id);
        Expense updatedExpense = expenseService.update(expense);
        DataResponse<Expense> response = new DataResponse<>();
        if (updatedExpense == null) {
            response.setMessage(HttpStatus.NOT_FOUND.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.setData(updatedExpense);
        response.setMessage(HttpStatus.OK.toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expenses")
    public ResponseEntity<DataResponse<List<Expense>>> getAllExpenses(
            @RequestParam(value = "period", required = false) String period,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<Expense> expenses;
        DataResponse<List<Expense>> response = new DataResponse<>();

        if (period != null) {
            expenses = expenseService.getExpensesByPeriod(period);
        } else if (startDate != null && endDate != null) {
            expenses = expenseService.findByDateRange(startDate, endDate);
        }else{
            expenses = expenseService.getAll();
        }
        response.setData(expenses);
        response.setMessage(HttpStatus.OK.toString());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<DataResponse<Void>> deleteExpense(@PathVariable Long id) {
        Expense expense = expenseService.getById(id);
        DataResponse<Void> response = new DataResponse<>();
        if (expense == null) {
            response.setMessage(HttpStatus.NOT_FOUND.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        expenseService.delete(expense);
        response.setMessage(HttpStatus.NO_CONTENT.toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expenses/{id}")
    public ResponseEntity<DataResponse<Expense>> getExpenseById(@PathVariable Long id) {
        Expense expense = expenseService.getById(id);
        DataResponse<Expense> response = new DataResponse<>();
        if (expense == null) {
            response.setMessage(HttpStatus.NOT_FOUND.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.setData(expense);
        response.setMessage(HttpStatus.OK.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/expenses/{expenseId}/categories/{categoryId}")
    public ResponseEntity<DataResponse<Expense>> addCategoryToExpense(@PathVariable Long expenseId,
            @PathVariable Long categoryId) {
        Expense expense = expenseService.getById(expenseId);
        Category category = categoryService.getById(categoryId);
        DataResponse<Expense> response = new DataResponse<>();
        if (expense == null || category == null) {
            response.setMessage(HttpStatus.NOT_FOUND.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<Category> list = new ArrayList<>();
        list.add(category);

        expense.setCategories(list);

        Expense updatedExpense = expenseService.update(expense);
        response.setData(updatedExpense);
        response.setMessage(HttpStatus.OK.toString());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/expenses/{expenseId}/categories/{categoryId}")
    public ResponseEntity<DataResponse<Expense>> removeCategoryFromExpense(@PathVariable Long expenseId,
            @PathVariable Long categoryId) {
        Expense expense = expenseService.getById(expenseId);
        DataResponse<Expense> response = new DataResponse<>();
        if (expense == null || expense.getCategories() == null) {
            response.setMessage(HttpStatus.NOT_FOUND.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        expense.getCategories().removeIf(category -> category.getId().equals(categoryId));
        Expense updatedExpense = expenseService.update(expense);
        response.setData(updatedExpense);
        response.setMessage(HttpStatus.OK.toString());
        return ResponseEntity.ok(response);
    }
}
