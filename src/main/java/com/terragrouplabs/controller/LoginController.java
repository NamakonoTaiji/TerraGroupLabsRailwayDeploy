package com.terragrouplabs.controller;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String login(@NonNull Model model) {
        model.addAttribute("pageTitle", "ログイン - TerraGroupLabs");
        model.addAttribute("currentPage", "login");
        return "login";
    }
}
