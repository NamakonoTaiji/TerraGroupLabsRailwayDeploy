// ContactMessageService.java の修正
package com.terragrouplabs.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terragrouplabs.entity.ContactMessage;
import com.terragrouplabs.repository.ContactMessageRepository;

@Service
public class ContactMessageService {

    private final ContactMessageRepository contactRepository;
    private final EmailService emailService;
    
    // コンストラクタインジェクションに変更
    public ContactMessageService(ContactMessageRepository contactRepository, EmailService emailService) {
        this.contactRepository = contactRepository;
        this.emailService = emailService;
    }
    
    /**
     * お問い合わせメッセージを保存し、通知メールを送信します
     * @param message 保存するメッセージ
     * @return 保存されたメッセージ
     */
    @Transactional
    public ContactMessage processContactSubmission(ContactMessage message) {
        // メッセージを保存
        ContactMessage savedMessage = contactRepository.save(message);
        
        // 管理者へ通知メールを送信
        emailService.sendContactNotification(message);
        
        return savedMessage;
    }
    
    /**
     * お問い合わせメッセージを保存します
     * @param message 保存するメッセージ
     * @return 保存されたメッセージ
     */
    @Transactional
    public ContactMessage saveMessage(ContactMessage message) {
        return contactRepository.save(message);
    }
    
    /**
     * すべてのお問い合わせメッセージを取得します
     * @return メッセージのリスト
     */
    public Iterable<ContactMessage> getAllMessages() {
        return contactRepository.findAll();
    }
    
    /**
     * IDによりお問い合わせメッセージを取得します
     * @param id メッセージID
     * @return 見つかったメッセージ、またはnull
     */
    public ContactMessage getMessageById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }
}