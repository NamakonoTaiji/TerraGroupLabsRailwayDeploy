package com.terragrouplabs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TerraGroupLabsLandingPageApplicationTests {

    // JavaMailSender をモック化する
    @MockBean
    private com.terragrouplabs.service.EmailService emailService;

    @Test
    void contextLoads() {
        // このテスト（コンテキストのロード）は JavaMailSender がモック化されているため、
        // メール設定不足で失敗しなくなるはずです。
    }

}
