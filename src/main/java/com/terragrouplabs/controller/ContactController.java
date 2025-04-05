// パッケージ宣言
package com.terragrouplabs.controller;

// --- Java Standard Imports ---
import org.slf4j.Logger; // LoginController から残っていた？ このクラスでは使われていないようです
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller; // 今回は使っていないが、一般的に使われる
import org.springframework.ui.Model; // Nullability アノテーション
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute; // バリデーション結果を保持
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam; // モデル属性の処理
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus; // リクエストパラメータの取得
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // セッション属性の管理

import com.terragrouplabs.entity.ContactMessage; // セッション属性のクリア
import com.terragrouplabs.service.ContactMessageService; // リダイレクト時の属性渡し
import com.terragrouplabs.service.EmailService;     // お問い合わせメッセージのエンティティ
import com.terragrouplabs.service.RecaptchaService; // メッセージ保存用サービス

import jakarta.validation.Valid;          // メール送信用サービス

/**
 * お問い合わせフォームに関連するリクエストを処理するコントローラ。 入力 -> 確認 -> 送信完了 (Thankyou) の画面遷移を管理します。
 * `@SessionAttributes` を使用して、複数リクエスト間でフォームデータを保持します。
 */
@Controller
@SessionAttributes("contactMessage") // "contactMessage" という名前のモデル属性をセッションで管理するよう指示
public class ContactController {

    // ロガーのセットアップ (ログ出力用)
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    // --- 依存サービスのフィールド ---
    private final ContactMessageService contactMessageService;
    private final EmailService emailService;
    private final RecaptchaService recaptchaService;

    /**
     * コンストラクタインジェクション: 必要なサービスを注入します。
     *
     * @param contactMessageService ContactMessageService
     * @param emailService EmailService
     * @param recaptchaService RecaptchaService
     */
    public ContactController(@NonNull ContactMessageService contactMessageService,
            @NonNull EmailService emailService,
            @NonNull RecaptchaService recaptchaService) {
        this.contactMessageService = contactMessageService;
        this.emailService = emailService;
        this.recaptchaService = recaptchaService;
    }

    /**
     * モデルに "contactMessage" 属性が存在しない場合に、新しい ContactMessage オブジェクトを生成して追加します。
     * これは `@SessionAttributes` と連携し、セッションに初期オブジェクトを提供したり、
     * 直接確認画面などにアクセスされた場合に空のオブジェクトを用意したりする役割があります。
     *
     * @return 新しい ContactMessage インスタンス
     */
    @ModelAttribute("contactMessage")
    public ContactMessage setUpContactMessage() { // メソッド名を setUp... のように変えると意図が分かりやすいかも
        // logger.debug("Initializing ContactMessage attribute in model/session."); // デバッグ用ログ
        return new ContactMessage();
    }

    /**
     * お問い合わせフォーム (index.jsp) からの POST リクエスト ("/contact/confirm") を処理します。
     * 入力値の検証、reCAPTCHA の検証を行い、問題なければ確認画面 (confirm.jsp) を表示します。
     * 問題があれば、エラー情報と共に元のフォームへリダイレクトします。
     *
     * @param contactMessage フォームからの入力値がバインドされた ContactMessage
     * オブジェクト。セッションからも取得・更新される。@Valid でバリデーション実行。
     * @param bindingResult バリデーション結果。@Valid オブジェクトの直後に置く必要がある。
     * @param recaptchaResponse reCAPTCHA ウィジェットから送信されたレスポンス文字列。
     * @param redirectAttributes リダイレクト先に一時的な属性（フラッシュ属性）を渡すためのオブジェクト。
     * @return 検証成功時はビュー名 "confirm"、失敗時はリダイレクト文字列。
     */
    @PostMapping("/contact/confirm")
    public String confirmContactForm(
            @Valid @ModelAttribute("contactMessage") ContactMessage contactMessage,
            @NonNull BindingResult bindingResult,
            @RequestParam(name = "g-recaptcha-response", required = true) String recaptchaResponse, // required=false は意図的？ reCAPTCHA が必須なら true or 別途チェック推奨
            @NonNull RedirectAttributes redirectAttributes) {

        logger.debug("Received POST /contact/confirm");
        logger.debug("reCAPTCHA Response present: {}", (recaptchaResponse != null && !recaptchaResponse.isEmpty()));

        // 1. 入力値のバリデーションチェック (@Valid の結果)
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors found: {}", bindingResult.getAllErrors());
            // エラーがある場合、エラー情報(bindingResult)と入力内容(contactMessage)をフラッシュ属性として設定
            // フラッシュ属性はリダイレクト後の一度だけ読み取れる属性
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contactMessage", bindingResult);
            redirectAttributes.addFlashAttribute("contactMessage", contactMessage);

            // 元のフォームにリダイレクト。パラメータとフラグメント識別子付き。
            return "redirect:/?formError=true#contact";
        }

        // 2. reCAPTCHA の検証
        boolean verified = recaptchaService.verifyRecaptcha(recaptchaResponse);
        if (!verified) {
            logger.warn("reCAPTCHA verification failed.");
            // reCAPTCHA 検証失敗の場合、BindingResult にエラーを追加
            // 第1引数はフィールド名(今回は特定フィールドではないのでグローバルエラー扱い or フォーム全体を示すキー), 第2引数はエラーコード(省略可), 第3引数はメッセージ
            bindingResult.reject("recaptcha.error", "reCAPTCHA検証に失敗しました。ロボットではないことを確認してください。"); // エラーコードを具体的にしても良い

            // エラー情報と入力内容をフラッシュ属性として設定
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contactMessage", bindingResult);
            redirectAttributes.addFlashAttribute("contactMessage", contactMessage);

            // 元のフォームにリダイレクト
            return "redirect:/?formError=true#contact";
        }

        // 3. 検証成功：確認画面へ
        logger.debug("Validation and reCAPTCHA successful. Proceeding to confirmation view.");
        // "contactMessage" は @SessionAttributes によりセッションに保持されているので、
        // そのまま確認画面のビュー名を返すだけで良い。
        return "confirm"; // -> /WEB-INF/views/confirm.jsp
    }

    /**
     * 確認画面 (confirm.jsp) からの「戻る」ボタンによる POST リクエスト ("/contact/back") を処理します。
     * 入力内容 (セッションに保持されている contactMessage) を保持したまま、元のフォームへリダイレクトします。
     *
     * @return 元のフォームへのリダイレクト文字列。
     */
    // 引数に @ModelAttribute と RedirectAttributes を追加
    @PostMapping("/contact/back")
    public String backToForm(
            @ModelAttribute("contactMessage") ContactMessage contactMessage, // セッションから取得
            @NonNull RedirectAttributes redirectAttributes) { // RedirectAttributes を注入
        logger.debug("Received POST /contact/back. Adding contactMessage to flash attributes and redirecting to form.");

        // セッションから取得した contactMessage をフラッシュ属性として追加
        redirectAttributes.addFlashAttribute("contactMessage", contactMessage);

        // (オプション) もし確認画面でエラーが発生するような特殊ケースがあれば、
        // BindingResult もフラッシュ属性で渡すことを検討できますが、通常「戻る」ではないでしょう。
        // 元のフォームへリダイレクト
        return "redirect:/?formError=true#contact";
    }

    /**
     * 確認画面 (confirm.jsp) からの最終送信処理 (POST "/contact") を担当します。
     * セッションからデータを取得し、データベースへの保存とメール送信を行います。
     * 処理完了後、セッションデータをクリアし、サンキューページへリダイレクトします。
     *
     * @param contactMessage セッションから取得した ContactMessage オブジェクト。
     * @param sessionStatus @SessionAttributes で管理されている属性の処理ステータスを管理するオブジェクト。
     * @param redirectAttributes リダイレクト先にフラッシュ属性を渡すためのオブジェクト。
     * @return 成功時はサンキューページへのリダイレクト文字列、エラー時はフォームへのリダイレクト文字列。
     */
    @PostMapping("/contact")
    public String handleContactForm(
            @ModelAttribute("contactMessage") ContactMessage contactMessage, // セッションから取得
            @NonNull SessionStatus sessionStatus,
            @NonNull RedirectAttributes redirectAttributes) {

        logger.info("Received POST /contact. Processing contact submission for email: {}", contactMessage.getEmail());

        try {
            // 1. データベースへメッセージを保存
            ContactMessage savedMessage = contactMessageService.saveMessage(contactMessage);
            logger.info("Contact message saved successfully. ID: {}", savedMessage.getId());

            // 2. 管理者へ通知メールを送信 (失敗しても処理は続行)
            try {
                emailService.sendContactNotification(contactMessage);
                logger.info("Contact notification email sent successfully.");
            } catch (Exception emailError) {
                // メール送信エラーはログに残すが、ユーザーへのエラー表示はせず、処理は成功とみなす
                logger.error("Failed to send contact notification email, but message was saved. Error: {}", emailError.getMessage(), emailError);
            }

            // 3. セッション属性 ("contactMessage") をクリア
            // これを呼ばないと、同じブラウザで再度フォームを開いたときに前回の入力が残ってしまう
            sessionStatus.setComplete();
            logger.debug("Session attribute 'contactMessage' cleared.");

            // 4. 成功メッセージをフラッシュ属性に追加
            redirectAttributes.addFlashAttribute("successMessage", "お問い合わせありがとうございました。");

            // 5. サンキューページへリダイレクト
            return "redirect:/thankyou";

        } catch (Exception e) {
            // データベース保存などで予期せぬエラーが発生した場合
            logger.error("Error processing contact form submission: {}", e.getMessage(), e);
            // エラーメッセージをフラッシュ属性に追加
            redirectAttributes.addFlashAttribute("errorMessage", "処理中にエラーが発生しました。しばらくしてからもう一度お試しください。");
            // フォームへリダイレクト
            return "redirect:/?formError=true#contact";
        }
    }

    /**
     * サンキューページ ("/thankyou") への GET リクエストを処理します。
     *
     * @param model ビューに渡すデータを格納する Model オブジェクト。
     * @return 表示するビュー名 ("thankyou")。
     */
    @GetMapping("/thankyou")
    public String showThankYouPage(@NonNull Model model) {
        logger.debug("Displaying thank you page.");
        model.addAttribute("pageTitle", "お問い合わせありがとうございました");
        model.addAttribute("currentPage", "contact"); // contact 関連ページとして
        return "thankyou"; // -> /WEB-INF/views/thankyou.jsp
    }
}
