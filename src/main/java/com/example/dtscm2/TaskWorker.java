package com.example.dtscm2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class TaskWorker {
    private static final Logger logger = LoggerFactory.getLogger(TaskWorker.class);
    private final ObjectMapper mapper;

    public TaskWorker(ObjectMapper mapper){
        this.mapper = mapper;
    }

    @RabbitListener(queues = "task-checking-queue")
    public void sendRequest(String message){
        try {
            TaskJSON task = mapper.readValue(message, TaskJSON.class);
            logger.info("WORKER RECEIVED task. ID: {}, URL: {}", task.id, task.url);
        } catch (Exception e){
            logger.error("Error:" + e);
        }
    }
}
