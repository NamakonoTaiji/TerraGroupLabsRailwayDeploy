package com.terragrouplabs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.terragrouplabs.entity.ServiceCategory;
import com.terragrouplabs.repository.ServiceCategoryRepository;

@Service
public class ServiceCategoryService {

    @Autowired
    private ServiceCategoryRepository categoryRepository;
    
    /**
     * すべてのサービスカテゴリーを取得します
     * @return カテゴリーのリスト
     */
    public List<ServiceCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
}