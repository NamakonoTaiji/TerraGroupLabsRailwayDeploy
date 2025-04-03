// このファイルが属するパッケージ名を宣言します。
package com.terragrouplabs.config;

// 必要なクラスをインポートします。
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Spring MVCのWeb関連設定をカスタマイズするクラスです。
 * WebMvcConfigurerを実装し、インターセプターなどを追加します。
 * * @Configuration が付いているので、Springに設定クラスとして認識されます。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // application.properties 等からアクティブプロファイルを取得します。
    // 設定がない場合は "dev" (開発環境) とみなします。
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    /**
     * インターセプターを登録します。
     * インターセプターは、Controllerの処理の前後に共通処理を挟む仕組みです。
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 新しいインターセプター（HandlerInterceptorの匿名実装）を登録します。
        registry.addInterceptor(new HandlerInterceptor() {
            /**
             * Controllerの処理後、ビューのレンダリング前に実行されます。
             * ここでレスポンスヘッダーを追加します。
             */
            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response,
                    Object handler, ModelAndView modelAndView) {

                // --- 基本的なセキュリティヘッダーを設定 ---
                // X-Content-Type-Options: MIMEタイプスニッフィング防止
                response.setHeader("X-Content-Type-Options", "nosniff");
                // X-Frame-Options: クリックジャッキング防止 (iframe埋め込み拒否)
                response.setHeader("X-Frame-Options", "DENY");
                // X-XSS-Protection: 古いブラウザ向けのXSSフィルター有効化 (ブロックモード)
                response.setHeader("X-XSS-Protection", "1; mode=block");
                // Referrer-Policy: Referer送信ポリシー設定 (オリジン間ではオリジンのみ送信)
                response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
                // Permissions-Policy: 機能（カメラ等）へのアクセス許可ポリシー設定 (すべて無効化)
                response.setHeader("Permissions-Policy", "camera=(), microphone=(), geolocation=()");

                // --- Content-Security-Policy (CSP) ヘッダーを設定 ---
                // XSS等の攻撃を防ぐため、読み込めるリソースのソースを制限します。
                // 環境（開発/本番）に応じて設定内容を変更します。
                if ("dev".equals(activeProfile)) {
                    // 開発環境: 比較的緩いCSP (インラインスクリプト/スタイル等を許可)
                    response.setHeader("Content-Security-Policy",
                            "default-src 'self'; " +
                                    "script-src 'self' https: 'unsafe-inline'; " + // 開発用に緩い
                                    "style-src 'self' https: 'unsafe-inline'; " + // 開発用に緩い
                                    "img-src 'self' data: https:; " +
                                    "font-src 'self' data: https:; " +
                                    "frame-src 'self' https:;");
                } else {
                    // 本番環境: より厳格なCSP (許可するドメインを限定)
                    response.setHeader("Content-Security-Policy",
                            "default-src 'self'; " +
                                    "script-src 'self' https://cdn.jsdelivr.net https://www.google.com https://www.gstatic.com; "
                                    +
                                    "style-src 'self' https://cdn.jsdelivr.net https://fonts.googleapis.com; "
                                    +
                                    "img-src 'self' data: https://www.google.com https://www.gstatic.com; " +
                                    "font-src 'self' https://cdn.jsdelivr.net https://fonts.gstatic.com data:; " +
                                    "frame-src 'self' https://www.google.com https://recaptcha.google.com; " + // reCAPTCHA用
                                    "connect-src 'self'; " +
                                    "base-uri 'self';");
                }
            }
        }); // インターセプター登録終了
    } // addInterceptors メソッド終了
} // WebConfig クラス終了