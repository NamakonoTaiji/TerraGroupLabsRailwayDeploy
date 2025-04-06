package com.terragrouplabs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class TerraGroupLabsLandingPageApplicationTests {

    // JavaMailSender をモック化する
    @MockitoBean
    private com.terragrouplabs.service.EmailService emailService;

    @Test
    void contextLoads() {
        // テストの実行
        // ここでは特に何もする必要はありません。
        // Spring Boot がコンテキストを正しくロードできるかどうかを確認するだけです。
    }

}
