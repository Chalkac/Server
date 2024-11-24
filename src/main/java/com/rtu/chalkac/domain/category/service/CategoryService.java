package com.rtu.chalkac.domain.category.service;

import com.rtu.chalkac.domain.category.dto.CategoryResponseDto;
import com.rtu.chalkac.domain.category.dto.CreateCategoryRequestDto;
import com.rtu.chalkac.domain.category.model.Category;
import com.rtu.chalkac.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category getCategory(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
    }

    // 1. 전체 조회
    public List<CategoryResponseDto> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponseDto::new) // DTO 생성자 호출
                .collect(Collectors.toList());
    }

    // 2. 개별 조회
    public CategoryResponseDto findCategoryById(Long id) {
        Category category = getCategory(id);
        return new CategoryResponseDto(category); // DTO 생성자 호출
    }

    // 3. 생성
    @Transactional
    public CategoryResponseDto createCategory(CreateCategoryRequestDto requestDto) {
        Category category = Category.builder()
                .name(requestDto.getName())
                .build();
        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponseDto(savedCategory); // DTO 생성자 호출
    }

    // 4. 삭제
    @Transactional
    public void deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
