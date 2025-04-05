package com.terragrouplabs.service;

import org.slf4j.Logger; // ObjectProvider をインポート
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider; // MailException をインポート
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender; // Logger をインポート
import org.springframework.stereotype.Service; // LoggerFactory をインポート

import com.terragrouplabs.entity.ContactMessage;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    // JavaMailSender を直接注入する代わりに、ObjectProvider を注入する
    private final ObjectProvider<JavaMailSender> mailSenderProvider;

    @Value("${contact.admin.email}") // デフォルト値は削除 (設定必須とする)
    private String adminEmail;

    // コンストラクタで JavaMailSender の ObjectProvider を受け取る
    public EmailService(ObjectProvider<JavaMailSender> mailSenderProvider) {
        this.mailSenderProvider = mailSenderProvider;
    }

    public void sendContactNotification(ContactMessage message) {
        // ObjectProvider を使って JavaMailSender Bean が利用可能か確認
        JavaMailSender mailSender = mailSenderProvider.getIfAvailable(); // Beanがあれば取得、なければ null

        if (mailSender != null) {
            // JavaMailSender が利用可能な場合のみメール送信を試みる
            try {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(adminEmail);
                mailMessage.setSubject("【お問い合わせ】" + message.getName() + "様より");
                mailMessage.setText("名前: " + message.getName() + "\n"
                        + "メール: " + message.getEmail() + "\n"
                        + "メッセージ:\n" + message.getMessage());

                logger.info("Attempting to send contact notification email to {}", adminEmail);
                mailSender.send(mailMessage);
                logger.info("Contact notification email sent successfully.");

            } catch (MailException e) { // より具体的な MailException をキャッチ
                logger.error("Failed to send contact notification email. MailException: {}", e.getMessage(), e);
                // ここでさらにエラーハンドリングが必要な場合もあるが、今回はログ出力のみ
            } catch (Exception e) {
                logger.error("An unexpected error occurred during email sending: {}", e.getMessage(), e);
            }
        } else {
            // JavaMailSender が利用できない（設定されていない）場合は警告ログを出力
            logger.warn("Skipping email sending because JavaMailSender is not configured or available.");
        }
    }
}
