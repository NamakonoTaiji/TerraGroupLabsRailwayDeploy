package com.terragrouplabs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Model をインポート
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.terragrouplabs.repository.ContactMessageRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ContactMessageRepository contactRepository;

    public AdminController(ContactMessageRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @GetMapping("/messages")
    public String listMessages(Model model) { // Model を引数に追加
        model.addAttribute("messages", contactRepository.findAll());
        model.addAttribute("currentPage", "adminMessages");
        model.addAttribute("pageTitle", "お問い合わせ管理");
        return "admin/messages";
    }

    @GetMapping("/messages/{id}")
    public String viewMessage(@PathVariable("id") Long id, Model model) { // Model を引数に追加
        model.addAttribute("message", contactRepository.findById(id).orElse(null));
        model.addAttribute("currentPage", "adminMessages");
        model.addAttribute("pageTitle", "お問い合わせ詳細");
        return "admin/message-detail"; // (もし message-detail.jsp があれば)
    }
}