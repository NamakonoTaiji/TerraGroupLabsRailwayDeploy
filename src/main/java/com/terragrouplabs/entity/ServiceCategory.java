// このクラスが属するパッケージを宣言します。
package com.terragrouplabs.entity;

// --- Java Standard Imports ---
// リスト（複数の要素を順序付けて格納するコレクション）を扱うためのインターフェース
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * サービスカテゴリを表すエンティティクラス。 データベースの `service_categories` テーブルに対応します。
 * 1つのカテゴリには複数のサービス (TerraService) が属することができます (1対多の関係)。
 */
@Entity // このクラスがJPAエンティティであることを示す
@Table(name = "service_categories") // 対応するテーブル名を指定
public class ServiceCategory {

    // --- フィールド定義 ---
    @Id // このフィールドが主キーであることを示す
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DBによる自動採番を利用
    private Long id; // カテゴリの一意なID

    @NotBlank(message = "カテゴリ名は必須です")
    @Size(max = 50, message = "カテゴリ名は50文字以内で入力してください")
    @Column(length = 50, nullable = false)
    private String name; // カテゴリ名 (例: "研究開発")

    @Size(max = 200, message = "説明文は200文字以内で入力してください")
    @Column(length = 200)
    private String description; // カテゴリの説明文

    // --- 関連マッピング (リレーションシップ) ---
    /**
     * このカテゴリに属するサービスのリスト。
     *
     * @OneToMany アノテーション: この ServiceCategory エンティティ (1) に対して、 関連する TerraService
     * エンティティ (多) が複数存在することを示します (1対多の関係)。
     * 例えば、「研究開発」カテゴリに「バイオテクノロジー」サービスと「AI研究」サービスが属する、といった関係です。
     *
     * mappedBy = "category" 属性: これは非常に重要です。リレーショナルデータベースのテーブル間の関連付けは、 通常「多」側
     * (この場合は `services` テーブル) が「1」側 ( `service_categories` テーブル) の
     * 主キーを外部キーとして持ちます。 `mappedBy = "category"` は、その関連の所有権（外部キーを持つ側）が、 相手側の
     * `TerraService` エンティティ内にある `category` という名前のフィールド (具体的には `@ManyToOne` と
     * `@JoinColumn` でマッピングされたフィールド) にあることを JPA に伝えます。 これにより、この
     * `ServiceCategory` エンティティが対応する `service_categories` テーブルには、 `TerraService`
     * への外部キーカラムが**作られません**。データベース構造がシンプルになります。 JPA は、この `ServiceCategory`
     * オブジェクトから関連する `services` を取得する必要がある場合、 `TerraService` テーブルを `category`
     * フィールド（に対応する `category_id` カラム）で検索してくれます。
     *
     * (補足) フェッチ戦略 (FetchType) やカスケード (CascadeType) など、関連に関する詳細な設定も
     * @OneToMany アノテーションの属性で指定できますが、ここではデフォルト設定が使われています。 デフォルトでは、関連する
     * `TerraService` は、`getServices()` が実際に呼び出されるまで データベースから読み込まれない「遅延ロード (Lazy
     * Loading)」になることが多いです。
     */
    @OneToMany(mappedBy = "category")
    private List<TerraService> services; // このカテゴリに属するサービスのリスト

    // --- コンストラクタ ---
    /**
     * デフォルトコンストラクタ。 JPA がエンティティをインスタンス化する際に必要です。
     */
    public ServiceCategory() {
    }

    // --- ゲッターとセッター ---
    // 各フィールドの値を取得 (get) および設定 (set) するためのメソッド群。
    // Lombok などのライブラリを使えば自動生成も可能ですが、ここでは明示的に記述されています。
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // このカテゴリに関連付けられたサービスのリストを取得するゲッター
    public List<TerraService> getServices() {
        return services;
    }

    // このカテゴリに関連付けるサービスのリストを設定するセッター
    public void setServices(List<TerraService> services) {
        this.services = services;
    }

    // --- equals() と hashCode() ---
    /**
     * オブジェクトの等価性を比較します。 このエンティティでは、主キーである `id` が null でなく、かつ同じであれば
     * 「論理的に等価」とみなします。
     *
     * @param o 比較対象のオブジェクト
     * @return オブジェクトが等価であれば true、そうでなければ false
     */
    @Override
    public boolean equals(Object o) {
        // (1) 同一インスタンスチェック: 自分自身との比較なら常に true
        if (this == o) {
            return true;
        }

        // (2) nullチェックと型チェック: 比較対象が null または 型が異なる場合は false
        //    getClass() != o.getClass() は、継承関係なども考慮した厳密な型比較です。
        //    (instanceof を使う方法もありますが、対称性を保つために getClass() が推奨されることもあります)
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        // (3) 型キャスト: 比較対象を ServiceCategory 型にキャスト
        ServiceCategory that = (ServiceCategory) o;

        // (4) 主キー(id)による比較:
        //    - id が null の場合、まだ永続化されていない新しいオブジェクトとみなします。
        //      この場合、他のオブジェクトとは（たとえ内容が同じでも）等価ではないと判断します。
        //      (インスタンスが別なら false ということになります)
        //    - id が null でない場合は、Objects.equals() を使って id 同士を比較します。
        //      Objects.equals() は両方の id が null の場合も安全に処理してくれます（ここでは上の条件で id!=null が保証されていますが）。
        return id != null && Objects.equals(id, that.id);
    }

    /**
     * オブジェクトのハッシュコードを返します。 equals() メソッドの契約に従い、equals() が true を返すオブジェクト同士は
     * 同じハッシュコードを返すように実装します。
     *
     * @return オブジェクトのハッシュコード
     */
    @Override
    public int hashCode() {
        // (1) id が null でない場合は、id のハッシュコードを返します。
        //     これにより、「id が同じオブジェクトは同じハッシュコードを持つ」ことが保証されます。
        // (2) id が null の場合 (まだ永続化されていない場合)、
        //     Objects.hash(id) は null に対して一貫した値（通常は 0）を返します。
        //     または、getClass().hashCode() を使うこともあります。これはクラス固有のハッシュコードを返します。
        //     これにより、永続化前の異なるインスタンスがある程度区別されますが、
        //     同じクラスの永続化前インスタンスは同じハッシュコードになる可能性があります。
        //     今回は Objects.hash(id) を使うのがシンプルで一般的です。
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ServiceCategory{"
                + "id=" + id
                + ", name='" + name + '\''
                + // services リスト自体は出力せず、サイズだけにするか、あるいは完全に省略する
                // ", services=[size=" + (services != null ? services.size() : 0) + "]"
                '}';
    }
}
