package com.terragrouplabs.config; // または適切なパッケージ

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.lang.NonNull;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(@NonNull Throwable throwable, @NonNull Method method, @NonNull Object... obj) {
        logger.error("非同期処理でキャッチされなかった例外が発生しました。", throwable);
        logger.error("メソッド: " + method.getName());
        // パラメータもログに出力する場合は、内容に注意 (個人情報など)
        for (Object param : obj) {
            logger.error("パラメータ: " + param); // toString() が呼ばれる
        }
        // ここで、管理者への通知や、エラー追跡システムへの記録など、
        // 必要なエラー処理を追加できます。
    }
}
