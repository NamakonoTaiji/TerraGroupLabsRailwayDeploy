// entity パッケージは、データベースのテーブルに対応するクラス（エンティティ）を格納します。
package com.terragrouplabs.entity;

import java.util.Objects; // equals と hashCode の実装で使うユーティリティクラス

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * お問い合わせメッセージを表すエンティティクラス。 データベースの `contact_messages` テーブルに対応します。
 * また、フォームからの入力値のバリデーションルールも定義しています。
 *
 * @Entity アノテーションにより、JPA（Java Persistence API）はこのクラスを
 * データベースのテーブルとマッピング可能なエンティティとして認識します。
 * @Table アノテーションで、対応するテーブル名を明示的に指定しています。 指定しない場合はクラス名に基づいてテーブル名が決定されますが、
 * 明示的に指定する方が分かりやすいでしょう。
 */
@Entity
@Table(name = "contact_messages")
public class ContactMessage {

    // --- フィールド定義 ---
    // 各フィールドは、データベーステーブルのカラムに対応します。
    /**
     * メッセージの一意なID（主キー）。
     *
     * @Id アノテーションで、このフィールドがテーブルの主キーであることを示します。
     * @GeneratedValue アノテーションで、主キーの値がどのように生成されるかを指定します。 strategy =
     * GenerationType.IDENTITY は、MySQLのAUTO_INCREMENTのような
     * データベースの自動採番機能を利用することを示します。 データが挿入される際にデータベースが自動的にIDを割り当てます。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * お問い合わせ者の名前。
     *
     * @NotBlank: null、空文字列("")、空白のみの文字列(" ") を許可しません。
     * 入力必須項目であることを示します。message属性でエラーメッセージを指定できます。
     * @Size: 文字列の長さを制限します。ここでは最大100文字までとしています。
     * @Pattern: 指定された正規表現に文字列が一致するかを検証します。 regexp = "^[^<>]*$"
     * は、「<」または「>」の文字が含まれていないことを意味します。 これは、簡単なクロスサイトスクリプティング（XSS）対策として機能します
     * （HTMLタグの入力を防ぐ）。
     */
    @NotBlank(message = "名前は必須です")
    @Size(max = 100, message = "名前は100文字以内で入力してください")
    @Column(length = 100, nullable = false)
    @Pattern(regexp = "^[^<>]*$", message = "名前に不正な文字が含まれています")
    private String name;

    /**
     * お問い合わせ者のメールアドレス。
     *
     * @NotBlank: 入力必須です。
     * @Email: 入力された文字列が標準的なメールアドレスの形式に一致するかを検証します。
     * 単純な形式チェックであり、メールアドレスが実際に存在するかどうかまでは検証しません。
     */
    @NotBlank(message = "メールアドレスは必須です")
    @Size(max = 100, message = "メールアドレスは100文字以内で入力してください")
    @Column(length = 100, nullable = false)
    @Email(message = "有効なメールアドレスを入力してください")
    private String email;

    /**
     * お問い合わせ内容の本文。
     *
     * @NotBlank: 入力必須です。
     * @Size: 最大1000文字まで。
     * @Pattern: 名前と同様に、HTMLタグが含まれていないかをチェックします。
     */
    @NotBlank(message = "メッセージは必須です")
    @Size(max = 1000, message = "メッセージは1000文字以内で入力してください")
    @Pattern(regexp = "^[^<>]*$", message = "メッセージに不正な文字が含まれています")
    private String message;

    // --- コンストラクタ ---
    /**
     * デフォルトコンストラクタ（引数なしのコンストラクタ）。 JPAフレームワークが、データベースから取得したデータを使って
     * このクラスのインスタンスを生成する際に必要となります。 public または protected である必要があります。
     */
    public ContactMessage() {
    }

    // --- ゲッターとセッター ---
    // 各フィールドの値を取得 (get) および設定 (set) するためのメソッド群です。
    // これらはJavaBeansの規約に従っており、外部のクラス（JPA、Spring MVC、JSPなど）が
    // このオブジェクトのフィールドに安全にアクセスするために使用されます。
    // 例えば、JSPで ${contactMessage.name} と書くと、内部的に getName() メソッドが呼び出されます。
    // フォームから送信された値をセットする際にもセッターが利用されます。
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
        // セッター内で簡単なサニタイズ（例:前後の空白除去）を行うことも可能ですが、
        // ここでは行っていません。
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

        // (3) 型キャスト: 比較対象を ContactMessage 型にキャスト
        ContactMessage that = (ContactMessage) o;

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

        // 代替案 (クラスのハッシュコードを使う):
        // return id != null ? Objects.hash(id) : getClass().hashCode();
    }

    // toString() メソッドの実装
    @Override
    public String toString() {
        // message 部分の条件分岐を修正
        String messageSnippet = (message != null)
                ? (message.length() > 20 ? message.substring(0, 20) + "..." : message) // 長さが20文字より大きい場合のみ substring する
                : "null";

        return "ContactMessage{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", email='" + (email != null ? email.replaceAll("(?<=.).(?=[^@]*?@)", "*") : "null") + '\''
                + ", message='" + messageSnippet + '\''
                + '}';
    }
}
