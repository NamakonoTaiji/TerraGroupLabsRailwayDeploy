// パッケージ宣言
package com.terragrouplabs.controller;

// 必要なクラスをインポート
import org.springframework.beans.factory.annotation.Value; // application.properties等の値を注入
import org.springframework.lang.NonNull; // Nullability アノテーション
import org.springframework.stereotype.Controller; // このクラスがMVCコントローラであることを示す
import org.springframework.ui.Model; // ビューに渡すデータを保持するオブジェクト
import org.springframework.web.bind.annotation.GetMapping; // HTTP GETリクエストをマッピング
import org.springframework.web.bind.annotation.ModelAttribute; // メソッドの戻り値をモデル属性として追加

import com.terragrouplabs.entity.ContactMessage; // お問い合わせフォームのデータ用エンティティ
import com.terragrouplabs.service.ServiceService; // サービス情報を取得するサービスクラス

/**
 * Webサイトの主要ページ（ホームページ、アバウト、サービス）の表示を担当するコントローラ。
 */
@Controller
public class HomeController {

    // ServiceService: サービス情報の取得ロジックを持つ (final で不変性を保証)
    private final ServiceService serviceService;

    // google.recaptcha.key の値を application.properties や環境変数から注入
    @Value("${google.recaptcha.key}")
    private String recaptchaSiteKey;

    // 現在アクティブなSpringプロファイル名 (デフォルトは "dev") を注入
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    /**
     * コンストラクタインジェクション: Springが ServiceService のインスタンスを自動的に注入します。
     *
     * @param serviceService 注入される ServiceService インスタンス
     */
    public HomeController(@NonNull ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * このコントローラが処理するすべてのリクエストのモデルに "isProd" という属性を追加します。 値は、アクティブプロファイルが "dev"
     * でない場合に true となります。 JSP側で ${isProd} として参照し、本番環境かどうかの判定に使えます。
     *
     * @return 本番環境であれば true, そうでなければ false
     */
    @ModelAttribute("isProd")
    public boolean isProd() {
        return !"dev".equals(activeProfile);
    }

    /**
     * ルートパス ("/") への GET リクエストを処理し、ホームページを表示します。
     *
     * @param model ビュー (JSP) に渡すデータを格納する Model オブジェクト (Springが自動的に注入)
     * @return 表示するビュー名 ("index")
     */
    @GetMapping("/")
    public String showHomePage(@NonNull Model model) {
        //--- モデルに属性を追加 ---
        // お問い合わせフォーム用に空の ContactMessage オブジェクトを追加 (フォームの modelAttribute と対応)
        model.addAttribute("contactMessage", new ContactMessage());
        // ServiceService を使って全サービス情報を取得し、モデルに追加
        model.addAttribute("services", serviceService.getAllServices());
        // reCAPTCHA のサイトキーをモデルに追加 (JSPの g-recaptcha タグで使用)
        model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
        // 現在のページ識別子をモデルに追加 (ナビゲーションのアクティブ表示用など)
        model.addAttribute("currentPage", "home");
        // HTML の <title> タグなどに使うページタイトルをモデルに追加
        model.addAttribute("pageTitle", "地球の未来をテクノロジーで創造する");

        // 対応するビューの名前 ("index") を返す。
        // MvcConfig の設定により、 "/WEB-INF/views/index.jsp" が解決される。
        return "index";
    }

    /**
     * "/about" パスへの GET リクエストを処理し、アバウトページを表示します。
     *
     * @param model ビューに渡すデータを格納する Model オブジェクト
     * @return 表示するビュー名 ("about")
     */
    @GetMapping("/about")
    public String showAboutPage(@NonNull Model model) {
        // ページタイトルと識別子をモデルに追加
        model.addAttribute("pageTitle", "TerraGroup Labs について");
        model.addAttribute("currentPage", "about");
        // 対応するビューの名前 ("about") を返す -> "/WEB-INF/views/about.jsp"
        return "about";
    }

    /**
     * "/service" パスへの GET リクエストを処理し、サービスページを表示します。
     *
     * @param model ビューに渡すデータを格納する Model オブジェクト
     * @return 表示するビュー名 ("service")
     */
    @GetMapping("/service")
    public String showServicePage(@NonNull Model model) {
        // 全サービス情報を取得してモデルに追加
        model.addAttribute("services", serviceService.getAllServices());
        // ページタイトルと識別子をモデルに追加
        model.addAttribute("pageTitle", "サービス");
        model.addAttribute("currentPage", "service");
        // 対応するビューの名前 ("service") を返す -> "/WEB-INF/views/service.jsp"
        return "service";
    }
}
