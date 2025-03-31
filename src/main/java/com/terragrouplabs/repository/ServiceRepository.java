package com.terragrouplabs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.terragrouplabs.entity.TerraService;

public interface ServiceRepository extends JpaRepository<TerraService, Long> {
    // 必要に応じてカスタムメソッドを追加
}