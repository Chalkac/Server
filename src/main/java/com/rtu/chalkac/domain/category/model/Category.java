package com.rtu.chalkac.domain.category.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Category")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(name = "name")
    private String name;
}
