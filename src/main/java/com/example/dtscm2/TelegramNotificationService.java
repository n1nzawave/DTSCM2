package com.example.dtscm2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramNotificationService {
    @Value("${spring.telegram.bot.token:}")
    private String botToken;
    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String chatId, String text){
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        requestBody.put("text", text);
        requestBody.put("parse_mode", "Markdown");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        restTemplate.postForObject(url, entity, String.class);
    }
}
