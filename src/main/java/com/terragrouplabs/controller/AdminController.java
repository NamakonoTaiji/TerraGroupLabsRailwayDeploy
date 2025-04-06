// パッケージ宣言
package com.terragrouplabs.controller;

// --- Spring Framework Imports ---
import org.springframework.lang.NonNull; // Nullability アノテーション
import org.springframework.stereotype.Controller; // このクラスがMVCコントローラであることを示す
import org.springframework.ui.Model; // ビューに渡すデータを保持するオブジェクト
import org.springframework.web.bind.annotation.GetMapping; // HTTP GETリクエストをマッピング
import org.springframework.web.bind.annotation.PathVariable; // URLパスの一部を引数として受け取る
import org.springframework.web.bind.annotation.RequestMapping; // クラスレベルでのURLパスのマッピング

import com.terragrouplabs.repository.ContactMessageRepository; // お問い合わせメッセージのリポジトリ

/**
 * 管理画面に関連するリクエスト (/admin/**) を処理するコントローラ。 お問い合わせメッセージの一覧表示や詳細表示などを担当します。
 */
@Controller
@RequestMapping("/admin") // このクラス内のマッピングは "/admin" がベースパスとなる
public class AdminController {

    // ContactMessageRepository: DBからお問い合わせメッセージを取得するためのリポジトリ (final で不変性を保証)
    private final ContactMessageRepository contactRepository;

    /**
     * コンストラクタインジェクション: Spring が ContactMessageRepository のインスタンスを注入します。
     *
     * @param contactRepository 注入される ContactMessageRepository インスタンス
     */
    public AdminController(@NonNull ContactMessageRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /**
     * ベースパス ("/admin") への GET リクエストを処理します。 機能的な画面であるメッセージ一覧 ("/admin/messages")
     * へリダイレクトさせます。
     *
     * @return メッセージ一覧へのリダイレクト文字列
     */
    @GetMapping // @RequestMapping("/admin") と合わせて "/admin" への GET にマッピング
    public String redirectToMessages() {
        // /admin に直接アクセスされた場合、/admin/messages に飛ばすのが親切
        return "redirect:/admin/messages";
    }

    /**
     * "/admin/messages" パスへの GET リクエストを処理し、お問い合わせメッセージの一覧を表示します。
     *
     * @param model ビューに渡すデータを格納する Model オブジェクト
     * @return 表示するビュー名 ("admin/messages")
     */
    @GetMapping("/messages") // -> /admin/messages への GET
    public String listMessages(@NonNull Model model) {
        // リポジトリを使って全ての ContactMessage を取得
        model.addAttribute("messages", contactRepository.findAll());
        // ページ識別子とタイトルをモデルに追加 (ヘッダー等での利用想定)
        model.addAttribute("currentPage", "adminMessages");
        model.addAttribute("pageTitle", "お問い合わせ管理");
        // 対応するビューの名前 -> /WEB-INF/views/admin/messages.jsp
        return "admin/messages";
    }

    /**
     * "/admin/messages/{id}" パスへの GET リクエストを処理し、指定されたIDのお問い合わせメッセージ詳細を表示します。
     * {id} の部分は動的な値（メッセージのID）が入ります。
     *
     * @param id URLパスから抽出されたメッセージID (@PathVariable で指定)
     * @param model ビューに渡すデータを格納する Model オブジェクト
     * @return 表示するビュー名 ("admin/message-detail")
     */
    @GetMapping("/messages/{id}") // 例: /admin/messages/123
    public String viewMessage(@PathVariable("id") Long id, @NonNull Model model) {
        // @PathVariable("id") で URL の {id} 部分の値を受け取る
        // contactRepository を使ってIDでメッセージを検索
        // findById は Optional を返すため、orElse(null) で見つからない場合は null をセット
        model.addAttribute("message", contactRepository.findById(id).orElse(null));
        // ページ識別子とタイトルをモデルに追加
        model.addAttribute("currentPage", "adminMessages"); // 一覧画面と同じ識別子で良いか？ 詳細画面用の識別子が良いかも
        model.addAttribute("pageTitle", "お問い合わせ詳細");
        // 対応するビューの名前 -> /WEB-INF/views/admin/message-detail.jsp
        return "admin/message-detail";
    }
}
