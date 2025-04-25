package com.terragrouplabs.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.terragrouplabs.entity.ServiceCategory;
import com.terragrouplabs.repository.ServiceCategoryRepository;

/**
 * サービスカテゴリに関するビジネスロジックを担当するサービスクラスです。
 */
@Service // このクラスがSpringのサービスコンポーネントであることを示す
public class ServiceCategoryService {

    // ServiceCategoryRepositoryのインスタンスをSpringが自動的に注入します。
    private final ServiceCategoryRepository categoryRepository;

    /**
     * コンストラクタインジェクション: Springが起動時にServiceCategoryRepositoryのBeanを引数に渡して
     * このServiceCategoryServiceのインスタンスを生成します。
     *
     * @param categoryRepository 注入されるリポジトリのインスタンス (NonNullでnullでないことを明示)
     */
    // @Autowired はコンストラクタが1つの場合は省略可能
    public ServiceCategoryService(@NonNull ServiceCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * すべてのサービスカテゴリーをデータベースから取得します。
     *
     * @return ServiceCategoryエンティティのリスト
     */
    public List<ServiceCategory> getAllCategories() {
        // 注入されたリポジトリのfindAll()メソッドを呼び出し、
        // データベースからすべてのServiceCategoryレコードを取得してリストとして返します。
        return categoryRepository.findAll();
    }
}
