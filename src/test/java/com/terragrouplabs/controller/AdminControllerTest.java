package com.terragrouplabs.controller;

import java.util.Optional; // カスタム例外

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser; // 認証ユーザーを設定
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model; // Optional を使う
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; // when
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view; // get()

import com.terragrouplabs.repository.ContactMessageRepository; // status(), view(), model()

// AdminController と、関連するSecurity設定などをロード
@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactMessageRepository contactRepository;

    // GlobalExceptionHandler は @ControllerAdvice なので @WebMvcTest により自動で読み込まれるはず
    @Test
    @WithMockUser(roles = "ADMIN") // ADMIN権限を持つモックユーザーでテストを実行
    void viewMessage_whenMessageNotFound_shouldReturn404Page() throws Exception {
        // Arrange (準備) --------------------
        // 1. 存在しないIDを定義
        long nonExistentId = 999L;

        // 2. RepositoryのfindByIdが呼ばれたら、空のOptionalを返すようにモックを設定
        //    これにより、Controller内の .orElseThrow(...) が ResourceNotFoundException をスローする
        when(contactRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert (実行と検証) -----------
        // 3. 存在しないIDで /admin/messages/{id} にGETリクエストを送信
        mockMvc.perform(get("/admin/messages/{id}", nonExistentId)
        // GETリクエストなので通常CSRFは不要だが、念のため付けても害はない
        // .with(csrf()) // 必要なら
        )
                // ↓↓↓ 検証部分の修正 ↓↓↓
                .andExpect(status().isOk()) // ★ エラーページ表示成功なので 200 OK を期待
                .andExpect(view().name("error/404")) // ★ 正しいビュー名か
                .andExpect(forwardedUrl("/WEB-INF/views/error/404.jsp")) // ★ 正しいJSPへフォワードされたか
                .andExpect(model().attributeExists("message")) // ★ モデル属性が存在するか
                .andExpect(model().attribute("status", 404)); // ★ モデル属性の値は正しいか
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void viewMessage_whenUnexpectedError_shouldReturn500Page() throws Exception {
        // Arrange (準備) --------------------
        long existingId = 1L;
        String errorMessage = "テスト用の予期せぬDBエラー";

        // 1. RepositoryのfindByIdが呼ばれたら、RuntimeExceptionをスローするように設定
        when(contactRepository.findById(existingId)).thenThrow(new RuntimeException(errorMessage));

// Act & Assert (実行と検証)
        mockMvc.perform(get("/admin/messages/{id}", existingId))
                // ↓↓↓ 検証部分の修正 ↓↓↓
                .andExpect(status().isOk()) // ★ エラーページ表示成功なので 200 OK を期待
                .andExpect(view().name("error/500")) // ★ 正しいビュー名か
                .andExpect(forwardedUrl("/WEB-INF/views/error/500.jsp")) // ★ 正しいJSPへフォワードされたか
                .andExpect(model().attributeExists("message")) // ★ モデル属性が存在するか
                .andExpect(model().attribute("status", 500)); // ★ モデル属性の値は正しいか
        // 必要なら、特定の例外が補足されたことを示す情報をモデルに追加して検証しても良い
        // .andExpect(model().attribute("exception", "RuntimeException"));
    }

    // 正常系（メッセージが見つかる場合）のテストも書くとより完全
    // @Test
    // @WithMockUser(roles = "ADMIN")
    // void viewMessage_whenMessageFound_shouldReturnDetailView() throws Exception { ... }
}
