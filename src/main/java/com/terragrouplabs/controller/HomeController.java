package com.terragrouplabs.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    
    // コンストラクタインジェクションに変更
    public HomeController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }
    
    @ModelAttribute("isProd")
    public boolean isProd() {
        return !"dev".equals(activeProfile);
    }
    
    @GetMapping("/")
    public String showHomePage(Model model) {
        // フォームオブジェクトを初期化
        model.addAttribute("contactMessage", new ContactMessage());
        
        // サービス一覧をモデルに追加
        model.addAttribute("services", serviceService.getAllServices());
        
        // reCAPTCHAサイトキーを追加
        model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
        
        // ページ識別子を明示的に設定
        model.addAttribute("currentPage", "home");
        
        return "index";
    }
    
    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }
    
    @GetMapping("/service")
    public String showServicePage(Model model) {
        // サービス一覧をモデルに追加
        model.addAttribute("services", serviceService.getAllServices());
        return "service";
    }
}