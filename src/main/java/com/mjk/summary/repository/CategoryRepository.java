package com.mjk.summary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mjk.summary.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
