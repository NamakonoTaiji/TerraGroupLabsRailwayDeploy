package com.terragrouplabs.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class RecaptchaService {

    private static final Logger logger = LoggerFactory.getLogger(RecaptchaService.class);
    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    
    private final String recaptchaSecret;
    
    // コンストラクタインジェクションに変更
    public RecaptchaService(@Value("${google.recaptcha.secret}") String recaptchaSecret) {
        this.recaptchaSecret = recaptchaSecret;
    }
    
    public boolean verifyRecaptcha(String recaptchaResponse) {
    	
        if (recaptchaResponse == null || recaptchaResponse.isEmpty()) {
            logger.warn("reCAPTCHA response is null or empty");
            return false;
        }
        
        try {
            String params = "secret=" + recaptchaSecret + "&response=" + recaptchaResponse;
            logger.debug("Secret key: {}", recaptchaSecret.substring(0, 3) + "..." + 
                        (recaptchaSecret.length() > 6 ? recaptchaSecret.substring(recaptchaSecret.length() - 3) : ""));
            
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(RECAPTCHA_VERIFY_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(params))
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.debug("reCAPTCHA API Response: {}", response.body());
            
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            return jsonObject.get("success").getAsBoolean();
        } catch (IOException | InterruptedException e) {
            logger.error("Error during reCAPTCHA verification: {}", e.getMessage(), e);
            return false;
        }
    }
}