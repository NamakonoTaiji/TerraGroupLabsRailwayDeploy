package com.terragrouplabs;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler; // Executor をインポート
import org.springframework.boot.SpringApplication; // インポート
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync; // ★これをインポート

import com.terragrouplabs.config.CustomAsyncExceptionHandler;

@SpringBootApplication
@EnableAsync
public class TerraGroupLabsLandingPageApplication implements AsyncConfigurer { // ★AsyncConfigurer を実装

    public static void main(String[] args) {
        SpringApplication.run(TerraGroupLabsLandingPageApplication.class, args);
    }

    // ★ 非同期処理の例外ハンドラを指定するメソッドをオーバーライド
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler(); // 作成したカスタムハンドラを返す
    }

    // (オプション) 非同期処理で使うスレッドプールをカスタマイズする場合は
    // getAsyncExecutor() メソッドもオーバーライドして Executor を設定します。
    // デフォルトでも動作しますが、本番環境では設定推奨。
    // @Override
    // public Executor getAsyncExecutor() {
    //     // return new ThreadPoolTaskExecutor(...); // 設定したExecutorを返す
    // }
}
