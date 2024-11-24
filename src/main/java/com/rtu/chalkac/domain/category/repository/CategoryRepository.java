package com.rtu.chalkac.domain.category.repository;

import com.rtu.chalkac.domain.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
