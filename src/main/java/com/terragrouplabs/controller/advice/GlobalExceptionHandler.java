package com.terragrouplabs.controller.advice;

import org.slf4j.Logger; // 作成したカスタム例外
import org.slf4j.LoggerFactory; // リクエスト情報の取得用 (任意)
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.terragrouplabs.exception.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

// アプリケーション全体の @Controller を対象とする
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * リソースが見つからなかった場合 (カスタム例外) のハンドラ
     *
     * @param ex 発生した ResourceNotFoundException
     * @param request HTTPリクエスト情報
     * @return 表示するModelAndView (エラーページとモデルデータ)
     */
    @ExceptionHandler(ResourceNotFoundException.class) // この例外を捕捉
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        logger.warn("リソースが見つかりません - URL: {}, エラー: {}", request.getRequestURL(), ex.getMessage());

        ModelAndView mav = new ModelAndView();
        // Model にエラーメッセージなどを追加 (JSPで表示するため)
        mav.addObject("status", 404);
        mav.addObject("error", "Not Found");
        mav.addObject("message", ex.getMessage()); // 例外メッセージを表示
        mav.addObject("path", request.getRequestURL()); // アクセスされたURL

        // 表示するビュー(JSP)の名前を設定
        mav.setViewName("error/404"); // 例: /WEB-INF/views/error/404.jsp
        return mav;
    }

    /**
     * その他の予期せぬ例外全般 (Internal Server Error) のハンドラ
     *
     * @param ex 発生した Exception
     * @param request HTTPリクエスト情報
     * @return 表示するModelAndView (エラーページとモデルデータ)
     */
    @ExceptionHandler(Exception.class) // 他のハンドラで捕捉されなかった全てのExceptionを捕捉
    public ModelAndView handleGenericException(Exception ex, HttpServletRequest request) {
        // 予期せぬエラーは詳細なスタックトレースもログに出力する
        logger.error("予期せぬエラーが発生しました - URL: {}", request.getRequestURL(), ex);

        ModelAndView mav = new ModelAndView();
        mav.addObject("status", 500);
        mav.addObject("error", "Internal Server Error");
        mav.addObject("message", "申し訳ありません。サーバー内部で予期せぬエラーが発生しました。");
        mav.addObject("path", request.getRequestURL());
        // (本番環境では詳細な例外情報はユーザーに見せない方が良い)
        // mav.addObject("exception", ex.getClass().getSimpleName());

        mav.setViewName("error/500"); // 例: /WEB-INF/views/error/500.jsp
        return mav;
    }

    // 必要に応じて、他の特定の例外 (例: DataAccessException, IllegalArgumentException など) の
    // ハンドラも @ExceptionHandler を使って追加できます。
    // より具体的な例外ハンドラが先にマッチします。
}
