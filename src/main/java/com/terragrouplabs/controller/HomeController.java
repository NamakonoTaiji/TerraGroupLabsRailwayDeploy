package com.terragrouplabs.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // ★ Model をインポート
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.terragrouplabs.entity.ContactMessage;
import com.terragrouplabs.service.ServiceService;

@Controller
public class HomeController {

    private final ServiceService serviceService;

    @Value("${google.recaptcha.key}")
    private String recaptchaSiteKey;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    public HomeController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @ModelAttribute("isProd")
    public boolean isProd() {
        return !"dev".equals(activeProfile);
    }

    @GetMapping("/")
    public String showHomePage(Model model) { // ★ 引数に Model model を追加
        // フォームオブジェクトを初期化
        model.addAttribute("contactMessage", new ContactMessage());
        // サービス一覧をモデルに追加
        model.addAttribute("services", serviceService.getAllServices());
        // reCAPTCHAサイトキーを追加
        model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
        // ページ識別子を明示的に設定
        model.addAttribute("currentPage", "home");

        // ★★★ pageTitle を Model に追加 ★★★
        model.addAttribute("pageTitle", "地球の未来をテクノロジーで創造する");

        return "index"; // index.jsp を返す
    }

    @GetMapping("/about")
    public String showAboutPage(Model model) { // ★ 引数に Model model を追加
        // ★★★ about ページのタイトルを設定 ★★★
        model.addAttribute("pageTitle", "TerraGroup Labs について");
        model.addAttribute("currentPage", "about");
        return "about"; // about.jsp を返す (もしあれば)
    }

    @GetMapping("/service")
    public String showServicePage(Model model) { // ★ 引数に Model model を追加
        // サービス一覧をモデルに追加
        model.addAttribute("services", serviceService.getAllServices());
        // ★★★ service ページのタイトルを設定 ★★★
        model.addAttribute("pageTitle", "サービス");
        model.addAttribute("currentPage", "service");
        return "service"; // service.jsp を返す (もしあれば)
    }
}