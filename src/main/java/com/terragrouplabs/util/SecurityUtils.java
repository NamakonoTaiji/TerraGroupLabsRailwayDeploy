package com.terragrouplabs.util;

import org.springframework.web.util.HtmlUtils;

public class SecurityUtils {
    
    public static String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return HtmlUtils.htmlEscape(input);
    }
    
    // URLエンコードなど他のエスケープメソッドも追加可能
}
