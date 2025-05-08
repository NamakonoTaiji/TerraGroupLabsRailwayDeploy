package com.terragrouplabs.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post; // BindingResultも扱う
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash; // any() マッチャー
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; // when()
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view; // post() など

import com.terragrouplabs.entity.ContactMessage;
import com.terragrouplabs.service.ContactMessageService; // status(), view(), model() など
import com.terragrouplabs.service.EmailService;
import com.terragrouplabs.service.RecaptchaService;

// ContactControllerのみをテスト対象とするWebMvcTest
@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired // MockMvcをインジェクト
    private MockMvc mockMvc;

    // Controllerが依存するServiceをモックBeanとして登録
    @MockitoBean
    private ContactMessageService contactMessageService;
    @MockitoBean
    private EmailService emailService;
    @MockitoBean
    private RecaptchaService recaptchaService;

    // ContactControllerは@SessionAttributes("contactMessage")を使っているので、
    // セッションにオブジェクトがあることをシミュレートする必要がある場合がある
    @Test
    @WithMockUser
    void confirmContactForm_whenValidInputAndRecaptchaSuccess_shouldShowConfirmView() throws Exception {
        // Arrange (準備) --------------------
        // 1. reCAPTCHA検証が成功するようモックを設定
        when(recaptchaService.verifyRecaptcha(any(String.class))).thenReturn(true);

        // 2. テスト用のフォームデータを作成 (ContactMessage)
        ContactMessage testMessage = new ContactMessage();
        testMessage.setName("テストユーザー");
        testMessage.setEmail("test@example.com");
        testMessage.setMessage("テストメッセージ");

        // Act & Assert (実行と検証) -----------
        // 3. POSTリクエストを /contact/confirm に送信
        mockMvc.perform(post("/contact/confirm") // POSTリクエスト
                .param("name", "テストユーザー") // formパラメータ name=テストユーザー
                .param("email", "test@example.com") // formパラメータ email=test@example.com
                .param("message", "テストメッセージ") // formパラメータ message=テストメッセージ
                .param("g-recaptcha-response", "dummy-recaptcha-response") // reCAPTCHAレスポンス
                // @ModelAttribute("contactMessage") にバインドされるオブジェクトを設定
                // (バリデーションを通過する想定なので、エラーがないオブジェクト)
                .flashAttr("contactMessage", testMessage)
                .with(csrf())
        //.sessionAttr("contactMessage", new ContactMessage()) // 必要に応じてセッション属性も設定
        )
                // 4. 結果を検証
                .andExpect(status().isOk()) // HTTPステータスが200 OKであること
                .andExpect(view().name("confirm")) // 返されるビュー名が "confirm" であること
                .andExpect(model().attributeExists("pageTitle")) // modelに pageTitle が存在すること
                .andExpect(model().attribute("contactMessage", testMessage)) // modelの contactMessage が testMessage と一致すること
                .andExpect(model().hasNoErrors()); // バリデーションエラーがないこと (BindingResultにエラーがないこと)

        // 5. モックの呼び出しを検証 (任意だが推奨)
        verify(recaptchaService, times(1)).verifyRecaptcha("dummy-recaptcha-response");
    }

    @Test
    @WithMockUser
    void confirmContactForm_whenInvalidInput_shouldRedirectToFormWithError() throws Exception {
        // Arrange (準備) --------------------
        // 1. reCAPTCHAは呼ばれないはず (バリデーションで弾かれるため)
        // 2. 不正な入力を持つデータ (例: nameが空)
        ContactMessage invalidMessage = new ContactMessage();
        invalidMessage.setEmail("test@example.com");
        invalidMessage.setMessage("テストメッセージ");
        invalidMessage.setName(""); // 名前が空 (バリデーションエラーになるはず)

        // Act & Assert (実行と検証) -----------
        mockMvc.perform(post("/contact/confirm")
                .param("name", "") // 空の名前を送信
                .param("email", "test@example.com")
                .param("message", "テストメッセージ")
                .param("g-recaptcha-response", "dummy-recaptcha-response")
                // @ModelAttribute("contactMessage") にバインドされるオブジェクト
                .flashAttr("contactMessage", invalidMessage)
                .with(csrf())
        )
                .andExpect(status().is3xxRedirection()) // HTTPステータスがリダイレクト(3xx)であること
                .andExpect(redirectedUrl("/#contact")) // リダイレクト先が /#contact であること
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.contactMessage")) // Flash属性にBindingResult(エラー情報)が存在すること
                .andExpect(flash().attributeExists("contactMessage")); // Flash属性に入力内容が戻されていること
    }

    // 他のテストケース:
    // - reCAPTCHA検証が失敗した場合
    // - handleContactForm (POST /contact) のテスト (DB保存とメール送信モックのverify)
    // - backToForm (POST /contact/back) のテスト
    // - showThankYouPage (GET /thankyou) のテスト
    // など...
}
