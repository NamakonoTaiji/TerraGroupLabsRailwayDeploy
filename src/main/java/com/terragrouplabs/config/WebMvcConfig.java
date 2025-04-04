// このクラスが属するパッケージを宣言
package com.terragrouplabs.config;

// 必要なクラスをインポート
import org.springframework.context.annotation.Configuration; // このクラスが設定クラスであることを示す
import org.springframework.http.CacheControl; // HTTPキャッシュ制御ヘッダーを構築するためのクラス
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry; // 静的リソースハンドラを登録するためのレジストリ
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // Spring MVCの設定をカスタマイズするためのインターフェース
import org.springframework.lang.NonNull; // 引数がnullでないことを示すアノテーション

import java.util.concurrent.TimeUnit; // 時間単位を表す列挙型 (DAYSなど)

/**
 * Spring MVCのWeb設定、特に静的リソースのハンドリングをカスタマイズするクラスです。
 * 
 * @Configuration アノテーションにより、Spring Boot がこのクラスを読み込み、設定を適用します。
 *                WebMvcConfigurer を実装することで、特定の設定メソッドをオーバーライドできます。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 静的リソースハンドラを追加・設定するためのメソッドをオーバーライドします。
     * ここで、特定のURLパスパターンにアクセスがあった場合に、どの場所からリソースを提供し、
     * どのようなキャッシュ設定を適用するかを定義します。
     * 
     * @param registry リソースハンドラを登録するためのレジストリ (Springが自動的に注入)
     */
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {

        // --- /images/** パターンに対する設定 ---
        registry.addResourceHandler("/images/**") // (1) URLパスパターン: "/images/" で始まるすべてのパス
                .addResourceLocations("classpath:/static/images/") // (2) リソースの場所: クラスパス上の "/static/images/" ディレクトリ
                .setCacheControl( // (3) キャッシュ制御設定
                        CacheControl.maxAge(3, TimeUnit.DAYS) // ブラウザ/プロキシに3日間キャッシュさせる
                                .cachePublic() // 中間のプロキシサーバーでもキャッシュ可能にする
                );

        // --- /css/** パターンに対する設定 ---
        registry.addResourceHandler("/css/**") // (1) URLパスパターン: "/css/" で始まるすべてのパス
                .addResourceLocations("classpath:/static/css/") // (2) リソースの場所: クラスパス上の "/static/css/" ディレクトリ
                .setCacheControl( // (3) キャッシュ制御設定
                        CacheControl.maxAge(3, TimeUnit.DAYS) // ブラウザ/プロキシに3日間キャッシュさせる
                                .cachePublic() // 中間のプロキシサーバーでもキャッシュ可能にする
                );

        // --- /js/** パターンに対する設定 ---
        registry.addResourceHandler("/js/**") // (1) URLパスパターン: "/js/" で始まるすべてのパス
                .addResourceLocations("classpath:/static/js/") // (2) リソースの場所: クラスパス上の "/static/js/" ディレクトリ
                .setCacheControl( // (3) キャッシュ制御設定
                        CacheControl.maxAge(3, TimeUnit.DAYS) // ブラウザ/プロキシに3日間キャッシュさせる
                                .cachePublic() // 中間のプロキシサーバーでもキャッシュ可能にする
                );
    }
}