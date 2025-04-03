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

    } // addInterceptors メソッド終了
} // WebConfig クラス終了