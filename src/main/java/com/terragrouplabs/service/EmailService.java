package com.terragrouplabs.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.terragrouplabs.entity.ContactMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // コンストラクタインジェクションに変更
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${contact.admin.email}")
    private String adminEmail;

    public void sendContactNotification(ContactMessage message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(adminEmail);  // 設定ファイルから読み込んだ管理者のメールアドレス
        mailMessage.setSubject("【お問い合わせ】" + message.getName() + "様より");
        mailMessage.setText("名前: " + message.getName() + "\n"
                + "メール: " + message.getEmail() + "\n"
                + "メッセージ:\n" + message.getMessage());

        mailSender.send(mailMessage);
    }
}
