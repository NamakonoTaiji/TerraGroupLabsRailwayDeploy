// service パッケージに属します
package com.terragrouplabs.service;

// --- Spring Framework Imports ---
// application.properties や環境変数などから設定値を注入するためのアノテーション
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.terragrouplabs.entity.ContactMessage;

/**
 * メール送信に関連する機能を提供するサービスクラス。
 */
@Service // このクラスがSpringのサービスBeanであることを示す
public class EmailService {

    // --- 依存性の定義 ---
    // Spring Mail のメール送信機能を提供するインターフェース。
    // 実際のメール送信処理は、application*.properties の spring.mail.* 設定に基づいて
    // Spring Boot が自動設定したこのインターフェースの実装が行う。
    private final JavaMailSender mailSender;

    /**
     * コンストラクタインジェクション。 Spring が JavaMailSender の Bean を自動的に注入します。
     *
     * @param mailSender 注入される JavaMailSender のインスタンス
     */
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // --- 設定値の注入 ---
    /**
     * application*.properties または環境変数から "contact.admin.email" の値を注入します。
     * ここには、お問い合わせ通知メールの宛先となる管理者のメールアドレスが設定されます。
     */
    @Value("${contact.admin.email}")
    private String adminEmail;

    // --- public メソッド ---
    /**
     * お問い合わせ内容をもとに、管理者へ通知メールを送信します。
     *
     * @param message 送信する内容が含まれる ContactMessage オブジェクト
     * @throws org.springframework.mail.MailException メール送信に失敗した場合
     * (認証失敗、サーバー接続不可など)
     * @Async このメソッドは非同期で実行されます。
     */
    @Async
    public void sendContactNotification(ContactMessage message) {
        // 1. SimpleMailMessage オブジェクトを作成
        //    これは、添付ファイルなどがない単純なテキストメールを送るためのクラスです。
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        // 2. メールの基本情報を設定
        //    宛先 (To): @Valueで注入された管理者のメールアドレスを設定
        mailMessage.setTo(adminEmail);
        mailMessage.setSubject("【お問い合わせ】" + message.getName() + "様より");
        mailMessage.setText(
                "名前: " + message.getName() + "\n"
                + "メール: " + message.getEmail() + "\n"
                + "メッセージ:\n" + message.getMessage()
        );
        // (補足) 送信元 (From) はここでは設定していません。
        // Spring Mail は通常、application*.properties の spring.mail.username の値を
        // デフォルトの From アドレスとして使用しようとします。
        // もし明示的に From を設定したい場合は mailMessage.setFrom("from@example.com"); のようにします。

        // 3. メールを送信
        //    設定済みの JavaMailSender を使って、作成したメールメッセージを送信します。
        //    ここで実際に SMTP サーバーとの通信が行われます。
        //    認証エラーや接続エラーなどが発生すると、MailException がスローされる可能性があります。
        mailSender.send(mailMessage);
    }
}
