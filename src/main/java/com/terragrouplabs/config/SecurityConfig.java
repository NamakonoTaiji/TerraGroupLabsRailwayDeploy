// このファイルが属するパッケージ名を宣言します。
package com.terragrouplabs.config;

import org.springframework.beans.factory.annotation.Value;
// 必要なクラスをインポートします。
import org.springframework.context.annotation.Bean; // Bean: Spring IoCコンテナに管理されるオブジェクト（Bean）を定義することを示すアノテーション。
import org.springframework.context.annotation.Configuration; // Configuration: このクラスがSpringの設定クラスであることを示すアノテーション。
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // HttpSecurity: HTTPベースのセキュリティ設定を構成するためのクラス。
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // EnableWebSecurity: Spring SecurityのWebセキュリティサポートを有効にし、基本的な設定を提供するためのアノテーション。
import org.springframework.security.core.userdetails.UserDetails; // UserDetails: ユーザーのコア情報を表すインターフェース（ユーザー名、パスワード、権限など）。
import org.springframework.security.core.userdetails.UserDetailsService; // UserDetailsService: UserDetailsをロードするためのインターフェース。
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // BCryptPasswordEncoder: BCryptアルゴリズムを使用してパスワードをハッシュ化（エンコード）するためのPasswordEncoder実装。
import org.springframework.security.crypto.password.PasswordEncoder; // PasswordEncoder: パスワードのエンコードと検証を行うためのインターフェース。
import org.springframework.security.provisioning.InMemoryUserDetailsManager; // InMemoryUserDetailsManager: ユーザー情報をメモリ内に保持するシンプルなUserDetailsService実装。テストやデモ向き。
import org.springframework.security.web.SecurityFilterChain; // SecurityFilterChain: HTTPリクエストに対するセキュリティフィルターのチェーンを定義するインターフェース。
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter; // XXssProtectionHeaderWriter: レガシーなX-XSS-Protectionヘッダーを設定するためのクラス。
import jakarta.servlet.DispatcherType; // DispatcherType: サーブレットのディスパッチャータイプを表す列挙型（リクエスト、フォワードなど）。

/**
 * Spring Securityの設定を行うクラスです。
 * 
 * @Configuration: このクラスが設定クラスであることを示します。
 * @EnableWebSecurity: Spring SecurityのWebセキュリティ機能を有効にします。
 *                     これにより、基本的なセキュリティ設定が適用され、
 *                     HttpSecurityを使ったカスタマイズが可能になります。
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        /**
         * パスワードエンコーダーのBean定義です。
         * パスワードを安全に保存・比較するために使用されます。
         * 
         * @Bean: このメソッドが返すオブジェクトをBeanとしてコンテナに登録します。
         * @return BCryptPasswordEncoderのインスタンス。BCryptは現在推奨される強力なハッシュアルゴリズムです。
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                // BCryptPasswordEncoderのインスタンスを生成して返します。
                // これにより、パスワードはハッシュ化されて保存され、元のパスワードを復元することはできません。
                return new BCryptPasswordEncoder();
        }

        /**
         * セキュリティフィルターチェーンのBean定義です。
         * HTTPリクエストに対するセキュリティルール（認証、認可、ヘッダー設定など）をここで構成します。
         * 
         * @param http HttpSecurityオブジェクト。セキュリティ設定を fluent API (メソッドチェーン) で記述するために使います。
         * @return 設定済みのSecurityFilterChainオブジェクト。
         * @throws Exception 設定中に発生する可能性のある例外。
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // authorizeHttpRequests: HTTPリクエストに対する認可設定を開始します。
                                .authorizeHttpRequests((requests) -> requests
                                                // FORWARD ディスパッチタイプのすべてのリクエストを許可するルールを追加
                                                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                                                // requestMatchers(...).permitAll():
                                                // 指定されたURLパターンに対するリクエストをすべて許可します（認証不要）。
                                                // 静的リソース（CSS, JS, 画像）、トップページ、アバウトページ、サービスページ、
                                                // お問い合わせ関連ページ、ログインページ、エラーページへのアクセスを許可しています。
                                                .requestMatchers(
                                                                "/", "/index", "/about", "/service",
                                                                "/contact/**", "/thankyou", "/css/**",
                                                                "/js/**", "/images/**", "/login", "/error",
                                                                "/favicon.ico", "/fragments/**")
                                                .permitAll()
                                                // requestMatchers("/admin/**").hasRole("ADMIN"): "/admin/"
                                                // で始まるURLパターンへのアクセスは、
                                                // "ADMIN" ロールを持つユーザーのみに許可します。
                                                // 注意: hasRoleは自動的に "ROLE_" プレフィックスを期待するため、実際には "ROLE_ADMIN" 権限が必要になります。
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                // anyRequest().permitAll(): 上記のルールにマッチしない、その他のすべてのリクエストを許可します。
                                                // 通常、本番環境では .anyRequest().authenticated() などとして、
                                                // 未定義のURLへのアクセスは少なくとも認証が必要とする場合が多いですが、
                                                // このランディングページでは意図的にすべて許可しているようです。
                                                .anyRequest().authenticated())
                                // formLogin: フォームベース認証の設定を開始します。
                                .formLogin((form) -> form
                                                // loginPage("/login"): カスタムログインページのURLを指定します。
                                                .loginPage("/login")
                                                // loginProcessingUrl("/login"): ログインフォームのPOST先URLを指定します。
                                                // このURLへのPOSTリクエストはSpring Securityが自動的に処理します。
                                                .loginProcessingUrl("/login")
                                                // defaultSuccessUrl("/admin/messages", true): ログイン成功後のリダイレクト先URLを指定します。
                                                // 第2引数 true は、常にこのURLにリダイレクトすることを意味します。
                                                .defaultSuccessUrl("/admin/messages", true)
                                                // permitAll(): ログインページ自体へのアクセスは常に許可します。
                                                .permitAll())
                                // logout: ログアウト設定を開始します。
                                .logout((logout) -> logout
                                                // logoutSuccessUrl("/login?logout"): ログアウト成功後のリダイレクト先URLを指定します。
                                                // クエリパラメータ "?logout" は、ログインページで「ログアウトしました」というメッセージを表示するために使われることがあります。
                                                .logoutSuccessUrl("/login?logout")
                                                // permitAll(): ログアウト機能自体へのアクセスは常に許可します。
                                                .permitAll())
                                // headers: セキュリティ関連のHTTPレスポンスヘッダー設定を開始します。
                                .headers((headers) -> headers
                                                // contentSecurityPolicy: Content Security Policy (CSP) ヘッダーを設定します。
                                                // XSS攻撃などを緩和するために、ブラウザが読み込めるリソースを制限します。
                                                .contentSecurityPolicy((csp) -> csp
                                                                // policyDirectives: CSPディレクティブ（ルール）を文字列で指定します。
                                                                // default-src 'self': デフォルトでは自分自身のオリジンからのみリソースを許可。
                                                                // script-src 'self' ...:
                                                                // スクリプトは自分自身、Google関連ドメイン、CDN、インラインスクリプトを許可。
                                                                // style-src 'self' ...: スタイルは自分自身、Google
                                                                // Fonts、CDN、インラインスタイルを許可。
                                                                // img-src 'self' data:: 画像は自分自身、data URIスキームを許可。
                                                                // font-src 'self' ...: フォントは自分自身、Google Fonts、CDNを許可。
                                                                // frame-src 'self' https://www.google.com:
                                                                // フレームは自分自身、Googleドメインを許可（reCAPTCHA用）。
                                                                .policyDirectives("default-src 'self'; " +
                                                                                "script-src 'self' https://www.google.com https://www.gstatic.com https://cdn.jsdelivr.net 'unsafe-inline'; "
                                                                                +
                                                                                "style-src 'self' https://fonts.googleapis.com https://cdn.jsdelivr.net 'unsafe-inline'; "
                                                                                +
                                                                                "img-src 'self' data:; " +
                                                                                "font-src 'self' https://fonts.gstatic.com https://cdn.jsdelivr.net; "
                                                                                +
                                                                                "frame-src 'self' https://www.google.com"))
                                                // xssProtection: レガシーな X-XSS-Protection ヘッダーを設定します。
                                                // CSPが推奨されますが、古いブラウザ向けに設定されることもあります。
                                                .xssProtection((xss) -> xss
                                                                // headerValue(...): ヘッダー値を設定。ENABLED_MODE_BLOCK
                                                                // はXSS検出時にページのレンダリングを停止します。
                                                                .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                                                // contentTypeOptions: X-Content-Type-Options ヘッダーを設定します (デフォルトで
                                                // 'nosniff'
                                                // が設定されます)。
                                                // MIMEタイプスニッフィング攻撃を防ぎます。
                                                .contentTypeOptions(contentTypeOptions -> {
                                                }) // デフォルト設定を適用
                                                   // frameOptions: X-Frame-Options ヘッダーを設定します。
                                                   // deny(): 他のサイトからのフレーム埋め込みを完全に拒否し、クリックジャッキング攻撃を防ぎます。
                                                .frameOptions(frameOptions -> frameOptions.deny()));

                // 設定されたHttpSecurityオブジェクトに基づいてSecurityFilterChainを構築し、返します。
                return http.build();
        }

        /**
         * 環境変数 ADMIN_USERNAME から管理者ユーザー名を取得します。
         * 環境変数が設定されていない場合のデフォルト値として "admin" を使用します。
         */
        @Value("${ADMIN_USERNAME}") // <- 環境変数名:デフォルト値
        private String adminUsername;

        /**
         * 環境変数 ADMIN_PASSWORD から管理者パスワードを取得します。
         * 環境変数が設定されていない場合のデフォルト値として "password" を使用します。
         * 注意: デフォルト値も本番環境では安全なものにするか、環境変数設定を必須にすべきです。
         */
        @Value("${ADMIN_PASSWORD}") // <- 環境変数名:デフォルト値
        private String adminPassword;

        /**
         * UserDetailsServiceのBean定義です。
         * ユーザーアカウント情報（ユーザー名、パスワード、ロール）をどのように取得するかを定義します。
         * 
         * @param passwordEncoder パスワードをエンコードするために注入されるPasswordEncoder Bean。
         * @return UserDetailsServiceの実装インスタンス。
         */
        @Bean
        public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
                // UserDetailsオブジェクト（ユーザー情報）を作成します。
                UserDetails user = org.springframework.security.core.userdetails.User
                                // 環境変数から取得したユーザー名を使用します。
                                .withUsername(adminUsername)
                                // 環境変数から取得したパスワードをエンコードして使用します。
                                .password(passwordEncoder.encode(adminPassword))
                                // roles: ユーザーに付与するロール（権限）を設定します。
                                // ここで "ADMIN" と指定すると、実際には "ROLE_ADMIN" という権限が付与されます。
                                // これは、上記の .hasRole("ADMIN") と対応します。
                                .roles("ADMIN")
                                // build: UserDetailsオブジェクトを構築します。
                                .build();

                // InMemoryUserDetailsManager: ユーザー情報をメモリ上に保持するUserDetailsService。
                // テストや非常に単純なアプリケーションに適していますが、ユーザーが増えたり、永続化が必要な本番環境には向きません。
                // 本番環境では通常、データベースからユーザー情報を読み込む実装 (例: JdbcDaoImpl, カスタム実装) を使用します。
                return new InMemoryUserDetailsManager(user);
        }
}