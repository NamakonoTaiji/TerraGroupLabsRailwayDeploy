package com.terragrouplabs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.terragrouplabs.entity.ContactMessage;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    // カスタムメソッドが必要であればここに記述
}
