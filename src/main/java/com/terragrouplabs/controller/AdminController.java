package com.terragrouplabs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.terragrouplabs.repository.ContactMessageRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ContactMessageRepository contactRepository;
    
    // コンストラクタインジェクションに変更
    public AdminController(ContactMessageRepository contactRepository) {
        this.contactRepository = contactRepository;
    }
    
    @GetMapping("/messages")
    public String listMessages(Model model) {
        model.addAttribute("messages", contactRepository.findAll());
        return "admin/messages";
    }
    
    @GetMapping("/messages/{id}")
    public String viewMessage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("message", contactRepository.findById(id).orElse(null));
        return "admin/message-detail";
    }
}