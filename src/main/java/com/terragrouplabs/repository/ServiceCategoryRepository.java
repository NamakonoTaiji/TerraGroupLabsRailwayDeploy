package com.terragrouplabs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.terragrouplabs.entity.ServiceCategory;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
    // 必要に応じてカスタムメソッドを追加
}