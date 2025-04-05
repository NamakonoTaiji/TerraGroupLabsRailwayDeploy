package com.terragrouplabs.controller;

import java.util.Enumeration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

/**
 * ログイン画面表示およびデバッグ用エンドポイントを提供するコントローラ。
 */
@Controller
public class LoginController {

    /**
     * "/login" パスへの GET リクエストを処理し、ログイン画面を表示します。
     *
     * @return 表示するビュー名 ("login") -> /WEB-INF/views/login.jsp
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 【デバッグ用】現在のセッション情報を表示するエンドポイント。
     *
     * @param session 現在の HttpSession オブジェクト (Springが注入)
     * @return セッションIDと属性を含む文字列 (HTMLとして表示される)
     */
    @GetMapping("/debug-session")
    @ResponseBody // 戻り値がビュー名ではなくレスポンスボディそのもの
    public String debugSession(HttpSession session) {
        StringBuilder sb = new StringBuilder();
        sb.append("Session ID: ").append(session.getId()).append("<br>");

        // セッション属性のキーと値を列挙
        Enumeration<String> attrs = session.getAttributeNames();
        while (attrs.hasMoreElements()) {
            String name = attrs.nextElement();
            sb.append(name).append(": ").append(session.getAttribute(name)).append("<br>");
        }

        return sb.toString();
    }
}
