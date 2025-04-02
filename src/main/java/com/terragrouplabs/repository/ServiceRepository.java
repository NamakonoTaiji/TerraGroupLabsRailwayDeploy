package com.terragrouplabs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.terragrouplabs.entity.TerraService;

@Repository
public interface ServiceRepository extends JpaRepository<TerraService, Long> {
    // 必要に応じてカスタムメソッドを追加
}