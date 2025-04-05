package com.terragrouplabs.config;

// --- Spring Framework Imports ---
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull; // Nullability アノテーションを追加
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User; // User クラスをインポート
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter; // ヘッダー設定用
import org.springframework.security.web.header.writers.StaticHeadersWriter;     // ヘッダー設定用
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter; // ヘッダー設定用

import jakarta.servlet.DispatcherType;

/**
 * Spring Security の設定クラス。 Webセキュリティ機能の有効化と、HTTPリクエストに対するセキュリティルールの定義を行います。
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 現在アクティブな Spring プロファイル (`dev` または `prod` など) を保持
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    // 環境変数から管理者ユーザー名を取得 (デフォルト: "admin")
    @Value("${ADMIN_USERNAME:admin}") // デフォルト値をコロンの後に追加
    private String adminUsername;

    // 環境変数から管理者パスワードを取得 (デフォルト: "password")
    // 注意: 本番環境では必ず環境変数を設定し、デフォルト値に頼らないこと
    @Value("${ADMIN_PASSWORD:password}") // デフォルト値をコロンの後に追加
    private String adminPassword;

    /**
     * パスワードのハッシュ化に使用する PasswordEncoder (BCrypt) の Bean を定義します。
     *
     * @return PasswordEncoder の実装インスタンス
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * アプリケーションのメインとなるセキュリティフィルターチェーンを定義します。
     * ここで認証・認可ルールや、各種セキュリティ機能、ヘッダー設定などを一元的に構成します。
     *
     * @param http HttpSecurity 設定オブジェクト (Spring が注入)
     * @return 設定済みの SecurityFilterChain
     * @throws Exception 設定中の例外
     */
    @Bean
    public SecurityFilterChain securityFilterChain(@NonNull HttpSecurity http) throws Exception {

        // --- プロファイルに応じた Content Security Policy (CSP) 文字列を決定 ---
        String cspDirectives;
        if ("dev".equals(activeProfile)) {
            // 開発環境用: 比較的緩やかな設定 ('unsafe-inline' を許可)
            cspDirectives = "default-src 'self'; "
                    + "script-src 'self' https: 'unsafe-inline'; " // スクリプト: 自己 + HTTPS + インライン許可
                    + "style-src 'self' https: 'unsafe-inline'; " // スタイル: 自己 + HTTPS + インライン許可
                    + "img-src 'self' data: https:; " // 画像: 自己 + data スキーム + HTTPS
                    + "font-src 'self' data: https:; " // フォント: 自己 + data スキーム + HTTPS
                    + "frame-src 'self' https:;"; // フレーム: 自己 + HTTPS
        } else {
            // 本番環境用: より厳格な設定 ('unsafe-inline' を可能な限り排除)
            cspDirectives = "default-src 'self'; " // デフォルト: 自己のみ
                    + "script-src 'self' https://cdn.jsdelivr.net https://www.google.com https://www.gstatic.com; " // スクリプト: 自己 + CDN + Google関連
                    + "style-src 'self' https://cdn.jsdelivr.net https://fonts.googleapis.com; " // スタイル: 自己 + CDN + Google Fonts (unsafe-inline 削除)
                    + "img-src 'self' data: https://www.google.com https://www.gstatic.com; " // 画像: 自己 + data スキーム + Google関連
                    + "font-src 'self' https://cdn.jsdelivr.net https://fonts.gstatic.com data:; " // フォント: 自己 + CDN + Google Fonts + data スキーム
                    + "frame-src 'self' https://www.google.com https://recaptcha.google.com; " // フレーム: 自己 + Google関連 (reCAPTCHA)
                    + "connect-src 'self'; " // 接続先(Ajax等): 自己のみ
                    + "base-uri 'self';";                          // baseタグのオリジン: 自己のみ
        }

        // --- HttpSecurity によるセキュリティ設定の構成 ---
        http
                // (1) 認可設定: URL ごとのアクセス制御
                .authorizeHttpRequests((requests) -> requests
                // FORWARD や INCLUDE ディスパッチタイプの内部的なリクエストは常に許可
                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE).permitAll()
                // 特定の公開パスは認証なしでアクセス許可
                .requestMatchers(
                        "/", "/index", "/about", "/service", // 主要ページ
                        "/contact/**", "/thankyou", // お問い合わせ関連
                        "/css/**", "/js/**", "/images/**", // 静的リソース
                        "/login", "/error", "/favicon.ico" // ログイン、エラー、ファビコン
                ).permitAll()
                // "/admin/" 以下は ADMIN ロールを持つユーザーのみ許可
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // "/debug-session" は ADMIN ロールを持つユーザーのみ許可
                .requestMatchers("/debug-session").hasRole("ADMIN")
                // 上記以外、全てのりクエストは認証が必要
                .anyRequest().authenticated()
                )
                // (2) フォームログイン設定
                .formLogin((form) -> form
                .loginPage("/login") // カスタムログインページのパス
                .loginProcessingUrl("/login") // ログイン処理を行うパス (POST)
                .defaultSuccessUrl("/admin/messages", true) // ログイン成功時のリダイレクト先 (常に)
                .permitAll() // ログインページ自体は常に許可
                )
                // (3) ログアウト設定
                .logout((logout) -> logout
                .logoutSuccessUrl("/login?logout") // ログアウト成功時のリダイレクト先
                .permitAll() // ログアウト機能自体は常に許可
                )
                // (4) HTTP レスポンスヘッダー設定
                .headers((headers) -> headers
                // Content Security Policy (CSP)
                .contentSecurityPolicy(csp -> csp
                .policyDirectives(cspDirectives) // プロファイルに応じたポリシーを適用
                )
                // X-XSS-Protection (レガシーブラウザ向け XSS 防御)
                .xssProtection(xss -> xss
                .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK) // ブロックモードで有効化
                )
                // X-Content-Type-Options (MIME スニッフィング防止)
                .contentTypeOptions(contentTypeOptions -> {
                }) // デフォルト (nosniff) を適用
                // X-Frame-Options (クリックジャッキング対策)
                .frameOptions(frameOptions -> frameOptions
                .deny() // フレーム内での表示を全面的に拒否
                )
                // Referrer-Policy (リファラー情報の送信制御)
                .referrerPolicy(referrer -> referrer
                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN) // クロスオリジンではオリジンのみ送信
                )
                // Permissions-Policy (ブラウザ機能へのアクセス制御)
                .addHeaderWriter(
                        new StaticHeadersWriter("Permissions-Policy", "camera=(), microphone=(), geolocation=()") // カメラ等を無効化
                )
                ); // headers 設定終了

        // 設定を確定し、SecurityFilterChain を構築して返す
        return http.build();
    }

    /**
     * インメモリ (メモリ上) のユーザー詳細サービス (UserDetailsService) の Bean を定義します。
     * 今回は管理者ユーザーのみを定義しています。
     *
     * @param passwordEncoder パスワードのエンコードに使用する PasswordEncoder
     * @return UserDetailsService の実装インスタンス
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // 管理者ユーザー情報を構築
        UserDetails adminUser = User
                .withUsername(adminUsername) // 環境変数から取得したユーザー名
                .password(passwordEncoder.encode(adminPassword)) // 環境変数から取得しエンコードしたパスワード
                .roles("ADMIN") // "ADMIN" ロールを付与 (実際には "ROLE_ADMIN" として扱われる)
                .build();

        // 作成した管理者ユーザー情報を持つインメモリマネージャーを返す
        // 本番環境では通常、データベース等からユーザー情報を取得する実装に置き換える
        return new InMemoryUserDetailsManager(adminUser);
    }
}
