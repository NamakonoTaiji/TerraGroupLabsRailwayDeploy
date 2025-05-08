package com.terragrouplabs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// この例外が投げられたら HTTP 404 Not Found を返すように示す (任意)
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message); // エラーメッセージを受け取るコンストラクタ
    }

    // 必要であれば他のコンストラクタやフィールドを追加
}
