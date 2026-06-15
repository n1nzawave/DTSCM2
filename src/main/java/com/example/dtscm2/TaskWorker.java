package com.example.dtscm2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class TaskWorker {
    private static final Logger logger = LoggerFactory.getLogger(TaskWorker.class);
    private final ObjectMapper mapper;
    private final HttpClient client;
    private final TaskLogRepository taskLogRepository;
    private final TelegramNotificationService telegramNotificationService;

    public TaskWorker(ObjectMapper mapper, TaskLogRepository taskLogRepository, TelegramNotificationService telegramNotificationService){
        this.mapper = mapper;
        this.client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
        this.taskLogRepository = taskLogRepository;
        this.telegramNotificationService = telegramNotificationService;
    }

    @RabbitListener(queues = "task-checking-queue")
    public void sendRequest(String message){
        Long taskId = null;
        TaskJSON task = null;
        int status_code = 0;
        Long difference = null;
        try {
            task = mapper.readValue(message, TaskJSON.class);
            taskId = task.id;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(task.url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            long startTime = System.currentTimeMillis();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            difference = System.currentTimeMillis() - startTime;

            status_code = response.statusCode();

            boolean is_success = (status_code >= 200 && status_code < 300);

            TaskLog log = new TaskLog(taskId, LocalDateTime.now(), status_code, difference, is_success);
            taskLogRepository.save(log);

            logger.info("WORKER RECEIVED task. ID: {}, URL: {}", task.id, task.url);
        } catch (Exception e){
            logger.error("Error:" + e);

            if (taskId != null){
                TaskLog errorLog = new TaskLog(taskId, LocalDateTime.now(), 0, 0L, false);
                taskLogRepository.save(errorLog);
                String errorMessage = "🚨 *Alert!* Website [" + task.url + "]" + " feeling bad\n" + "Status code: " + status_code + "\nAnswer time: " + difference;
                telegramNotificationService.sendMessage("714830569", errorMessage);
            }

        }
    }
}
