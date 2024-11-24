package com.rtu.chalkac.domain.category.dto;

import com.rtu.chalkac.domain.category.model.Category;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class CategoryResponseDto {
    private final Long categoryId;
    private final String name;

    public CategoryResponseDto(Category category) {
        this.categoryId = category.getCategoryId();
        this.name = category.getName();
    }
}
