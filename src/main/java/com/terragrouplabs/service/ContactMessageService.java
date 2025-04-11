// このクラスが属するパッケージを宣言します。
// service パッケージは、ビジネスロジックや
// 複数のリポジトリやサービスを組み合わせた処理を担当するクラスを格納します。
package com.terragrouplabs.service;

// --- Spring Framework Imports ---
// このクラスがサービス層のコンポーネント (Bean) であることを示すアノテーション。
// Spring のコンポーネントスキャンの対象となり、DI (依存性注入) が可能になります。
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terragrouplabs.entity.ContactMessage;
import com.terragrouplabs.repository.ContactMessageRepository;

/**
 * お問い合わせメッセージに関するビジネスロジックを担当するサービスクラス。 データベースへの保存、取得、および関連する通知処理などを行います。
 */
@Service // このクラスがSpringのサービスコンポーネントであることを示す
public class ContactMessageService {

    // --- 依存性の定義 (final で宣言) ---
    // @Autowired を使わず、final フィールドとコンストラクタインジェクションを使うのが推奨される方法です。
    // お問い合わせメッセージのデータアクセスを担当するリポジトリ
    private final ContactMessageRepository contactRepository;

    /**
     * コンストラクタインジェクション。 Spring がこのクラスのインスタンス (Bean) を生成する際に、 必要な依存オブジェクト
     * (ContactMessageRepositoryのBean) を 自動的に引数に渡してくれます (注入してくれます)。
     * これにより、このクラス内で `contactRepository` が利用可能になります。
     *
     * @param contactRepository 注入される ContactMessageRepository のインスタンス
     */
    public ContactMessageService(ContactMessageRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /**
     * お問い合わせメッセージをデータベースに保存するシンプルなメソッド。 (現在の ContactController はこちらを呼び出しています)
     *
     * @Transactional アノテーション: この save 処理自体もトランザクション内で実行されることを保証します。
     *
     * @param message 保存する ContactMessage オブジェクト
     * @return 保存され、IDが採番された ContactMessage オブジェクト
     */
    @Transactional
    public ContactMessage saveMessage(ContactMessage message) {
        // リポジトリの save メソッドを呼び出してデータベースに保存
        return contactRepository.save(message);
    }

    /**
     * すべてのお問い合わせメッセージをデータベースから取得します。
     *
     * (補足) @Transactional(readOnly = true) を付けることも可能です。
     * これは読み取り専用の操作であることを示し、JPA/Hibernate が 若干のパフォーマンス最適化を行う可能性があります（必須ではありません）。
     *
     * @return データベース内の全 ContactMessage の Iterable (反復可能なコレクション) Iterable の代わりに
     * List<ContactMessage> を返すことも一般的です。
     */
    public Iterable<ContactMessage> getAllMessages() {
        // リポジトリの findAll メソッドを呼び出して全件取得
        return contactRepository.findAll();
    }

    /**
     * 指定された ID に対応するお問い合わせメッセージをデータベースから取得します。
     *
     * (補足) こちらも @Transactional(readOnly = true) を付けることが可能です。
     *
     * @param id 取得したいメッセージの ID
     * @return 見つかった ContactMessage オブジェクト。見つからなかった場合は null。
     */
    public ContactMessage getMessageById(Long id) {
        // リポジトリの findById メソッドを呼び出す。これは Optional<ContactMessage> を返す。
        // orElse(null) を使って、Optional の中身が存在すればその値を、
        // 存在しなければ (IDが見つからなければ) null を返すようにしている。
        return contactRepository.findById(id).orElse(null);
    }
}
