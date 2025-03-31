package com.terragrouplabs.controller;

import java.util.Enumeration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    
    // 単純明快なマッピング
    @GetMapping("/login")
    public String login() {
        return "login"; // WEB-INF/views/login.jspを指す
    }
    
    // デバッグ用の追加機能
    @GetMapping("/debug-session")
    @ResponseBody
    public String debugSession(HttpSession session) {
        StringBuilder sb = new StringBuilder();
        sb.append("Session ID: ").append(session.getId()).append("<br>");
        
        // セッション属性を取得して表示
        Enumeration<String> attrs = session.getAttributeNames();
        while (attrs.hasMoreElements()) {
            String name = attrs.nextElement();
            sb.append(name).append(": ").append(session.getAttribute(name)).append("<br>");
        }
        
        return sb.toString();
    }
}
