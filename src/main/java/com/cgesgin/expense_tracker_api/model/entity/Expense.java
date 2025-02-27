package com.cgesgin.expense_tracker_api.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "date")
    private LocalDateTime date;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "expense_category",
        joinColumns = @JoinColumn(name = "expense_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
}
