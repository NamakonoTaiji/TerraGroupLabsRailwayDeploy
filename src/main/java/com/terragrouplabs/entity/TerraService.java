// このクラスが属するパッケージを宣言します。
package com.terragrouplabs.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 提供するサービスを表すエンティティクラス。 データベースの `services` テーブルに対応します。 各サービスは必ず1つのサービスカテゴリ
 * (ServiceCategory) に属します (多対1の関係)。
 */
@Entity // このクラスがJPAエンティティであることを示す
@Table(name = "services") // 対応するテーブル名を指定
public class TerraService {

    // --- フィールド定義 ---
    @Id // このフィールドが主キーであることを示す
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DBによる自動採番を利用
    private Long id; // サービスの一意なID

    @NotBlank(message = "タイトルは必須です")
    @Size(max = 100, message = "タイトルは100文字以内で入力してください")
    @Column(length = 100, nullable = false)
    private String title; // サービスのタイトル (例: "バイオテクノロジー")

    private String description; // サービスの詳細説明

    @Size(max = 50, message = "アイコン名は50文字以内で入力してください")
    @Column(name = "icon_name", length = 50)
    private String iconName; // 表示に使用するアイコンのクラス名など (例: "bi-fingerprint")

    // --- 関連マッピング (リレーションシップ) ---
    /**
     * このサービスが属するカテゴリ。
     *
     * @ManyToOne アノテーション: この TerraService エンティティ (多) が、1つの ServiceCategory
     * エンティティ (1) に 関連付くことを示します (多対1の関係)。 複数のサービスが同じ1つのカテゴリに属することができます。
     * 例えば、「バイオテクノロジー」サービスと「AI研究」サービスの両方が「研究開発」カテゴリに属する、といった関係です。
     *
     * @JoinColumn アノテーション: この関連をデータベース上で実現するための**外部キーカラム**を指定します。 name =
     * "category_id": この TerraService エンティティが対応する `services` テーブル内に、
     * `category_id` という名前のカラムが作られ、これが外部キーとして機能することを示します。 この `category_id`
     * カラムには、関連する `ServiceCategory` の主キー (`id`) の値が格納されます。
     *
     * (補足) こちら側 (`TerraService`) が `@JoinColumn` を持って外部キーを管理するため、 こちらが関連の「所有側
     * (Owning Side)」となります。 相手側の `ServiceCategory` では `@OneToMany(mappedBy =
     * "category")` と指定することで、 外部キーの管理をこちら側に任せていることを示していましたね。
     *
     * (補足2) デフォルトでは、関連する `ServiceCategory` は、この `TerraService` が
     * データベースから読み込まれる際に一緒に読み込まれる「即時ロード (Eager Loading)」になることが多いです。 (`@ManyToOne`
     * のデフォルトは Eager, `@OneToMany` のデフォルトは Lazy)
     */
    @ManyToOne
    @JoinColumn(name = "category_id") // `services` テーブルの外部キーカラム名を指定
    private ServiceCategory category; // このサービスが属するカテゴリのオブジェクト

    // --- コンストラクタ ---
    /**
     * デフォルトコンストラクタ。 JPA がエンティティをインスタンス化する際に必要です。
     */
    public TerraService() {
    }

    // --- ゲッターとセッター ---
    // 各フィールドの値を取得 (get) および設定 (set) するためのメソッド群。
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    // このサービスが属するカテゴリ (ServiceCategory オブジェクト) を取得するゲッター
    public ServiceCategory getCategory() {
        return category;
    }

    // このサービスが属するカテゴリ (ServiceCategory オブジェクト) を設定するセッター
    // このメソッドを使ってカテゴリをセットし、JPAで保存すると、
    // 自動的に `services` テーブルの `category_id` カラムに適切な値が設定されます。
    public void setCategory(ServiceCategory category) {
        this.category = category;
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

        // (3) 型キャスト: 比較対象を TerraService 型にキャスト
        TerraService that = (TerraService) o;

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
        return "TerraService{"
                + "id=" + id
                + ", title='" + title + '\''
                + // category オブジェクトの toString() を呼ぶのではなく、IDだけを出力
                ", categoryId=" + (category != null ? category.getId() : "null")
                + '}';
    }
}
