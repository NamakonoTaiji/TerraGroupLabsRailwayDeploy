package com.terragrouplabs.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.terragrouplabs.entity.TerraService;
import com.terragrouplabs.repository.ServiceRepository;

/**
 * TerraServiceエンティティに関するビジネスロジックを担当するサービスクラスです。
 */
@Service // このクラスがSpringのサービスコンポーネントであることを示す
public class ServiceService {

    // finalキーワードにより、一度インスタンスが設定されると変更不可になります。
    private final ServiceRepository serviceRepository;

    /**
     * コンストラクタインジェクション。 SpringがこのServiceServiceのBeanを生成する際に、
     * ServiceRepositoryのBeanを自動的に注入します。
     *
     * @param serviceRepository 注入されるリポジトリのインスタンス
     */
    // @Autowired はコンストラクタが1つの場合は省略可能
    public ServiceService(@NonNull ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /**
     * すべてのサービスを取得します
     *
     * @return サービスのリスト
     */
    public List<TerraService> getAllServices() {
        // リポジトリのfindAll()メソッドを呼び出して全サービスを取得
        return serviceRepository.findAll();
    }

    /**
     * IDによりサービスを取得します
     *
     * @param id サービスID
     * @return 見つかったサービス、またはnull
     */
    public TerraService getServiceById(Long id) {
        // リポジトリのfindById(id)メソッドは Optional<TerraService> を返します。
        // orElse(null) を使うことで、Optionalの中身が存在すればその値を、
        // 存在しなければ（IDに対応するサービスが見つからなければ）nullを返します。
        return serviceRepository.findById(id).orElse(null);
    }
}
