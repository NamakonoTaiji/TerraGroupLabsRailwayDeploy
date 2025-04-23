// src/main/java/com/terragrouplabs/service/RecaptchaService.java
package com.terragrouplabs.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;         // Java 11 標準の HTTP クライアント
import java.net.http.HttpRequest;        // HTTP リクエストを表現
import java.net.http.HttpResponse;       // HTTP レスポンスを表現
import java.time.Duration;                // SLF4j ロガー

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // プロパティ値の注入
import org.springframework.beans.factory.annotation.Value;             // Service Bean であることを示す
import org.springframework.stereotype.Service;     // Google の Gson ライブラリ (JSON 操作用)

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Google reCAPTCHA v2 の検証を行うサービスクラス。 フォームから送信された reCAPTCHA レスポンスを Google の API
 * に問い合わせ、 人間による操作かどうかを確認します。
 */
@Service
public class RecaptchaService {

    // ロガーのインスタンスを取得
    private static final Logger logger = LoggerFactory.getLogger(RecaptchaService.class);
    // Google reCAPTCHA の検証用 API エンドポイント URL (定数)
    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    // application*.properties や環境変数から reCAPTCHA のシークレットキーを注入
    // final をつけて不変にしているのが良いですね。
    private final String recaptchaSecret;

    /**
     * コンストラクタインジェクション。 Spring が @Value アノテーションを解決し、対応するプロパティ値
     * (google.recaptcha.secret) を このコンストラクタの引数に注入します。
     *
     * @param recaptchaSecret application*.properties または環境変数から注入される reCAPTCHA
     * シークレットキー
     */
    public RecaptchaService(@Value("${google.recaptcha.secret}") String recaptchaSecret) {
        this.recaptchaSecret = recaptchaSecret;
    }

    /**
     * reCAPTCHA のレスポンストークンを Google の API に送信して検証します。
     *
     * @param recaptchaResponse フロントエンドの reCAPTCHA ウィジェットから送信されたレスポンストークン文字列
     * @return 検証に成功した場合は true、失敗またはエラーの場合は false
     */
    public boolean verifyRecaptcha(String recaptchaResponse) {

        // --- (1) 入力チェック ---
        // レスポンストークンが null または空文字列の場合は、検証するまでもなく false を返す
        if (recaptchaResponse == null || recaptchaResponse.isEmpty()) {
            logger.warn("reCAPTCHA response is null or empty");
            return false;
        }

        try {
            // --- (2) Google API へのリクエスト準備 ---
            // 送信するパラメータ文字列を作成 (secret キーと response トークン)
            // application/x-www-form-urlencoded 形式
            String params = "secret=" + recaptchaSecret + "&response=" + recaptchaResponse;

            // デバッグログ: シークレットキー全体をログに出力しないように配慮されている点が良いです。
            logger.debug("Secret key: {}", recaptchaSecret.substring(0, 3) + "..."
                    + (recaptchaSecret.length() > 6 ? recaptchaSecret.substring(recaptchaSecret.length() - 3) : ""));

            // Java 11 標準の HttpClient を使用してリクエストを送信
            HttpClient client = HttpClient.newHttpClient(); // 新しいクライアントインスタンスを作成
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(RECAPTCHA_VERIFY_URL)) // API の URI を指定
                    .header("Content-Type", "application/x-www-form-urlencoded") // コンテントタイプを指定
                    .POST(HttpRequest.BodyPublishers.ofString(params)) // POST メソッドでパラメータを送信
                    .timeout(Duration.ofSeconds(10))
                    .build(); // HttpRequest オブジェクトを構築

            // --- (3) リクエスト送信とレスポンス受信 ---
            // 同期的にリクエストを送信し、レスポンスボディを文字列として受け取る
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // デバッグログ: Google API からのレスポンス内容を出力
            logger.debug("reCAPTCHA API Response: {}", response.body());

            // --- (4) レスポンス (JSON) の解析 ---
            // Gson ライブラリを使ってレスポンス文字列を JSON オブジェクトにパース
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            // JSON オブジェクトから "success" フィールドの値 (boolean) を取得して返す
            return jsonObject.get("success").getAsBoolean();

        } catch (IOException | InterruptedException e) {
            // --- (5) エラーハンドリング ---
            // HTTP 通信中のエラー (IOException) や割り込み (InterruptedException) が発生した場合
            logger.error("Error during reCAPTCHA verification: {}", e.getMessage(), e);
            // エラーが発生した場合は検証失敗とみなし false を返す
            return false;
        }
    }
}
