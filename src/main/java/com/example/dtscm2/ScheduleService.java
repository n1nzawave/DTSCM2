package com.example.dtscm2;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);
    private final TaskRepository taskRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;

    public ScheduleService(TaskRepository taskRepository, RabbitTemplate rabbitTemplate, ObjectMapper mapper){
        this.taskRepository = taskRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }


    @Scheduled(fixedRate = 1000)
    public void checkStatus() throws JsonProcessingException {
        List<Task> allTasks = taskRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < allTasks.size(); i++){
            if (allTasks.get(i).getLastCheckedAt() == null || ChronoUnit.SECONDS.between(allTasks.get(i).getLastCheckedAt(), now) >= allTasks.get(i).getIntervalSeconds()){
                TaskJSON task = new TaskJSON(allTasks.get(i).getId(), allTasks.get(i).getUrl());
                String taskJSON = mapper.writeValueAsString(task);
                allTasks.get(i).setLastCheckedAt(now);
                taskRepository.save(allTasks.get(i));
                rabbitTemplate.convertAndSend("task-checking-queue", taskJSON);
                logger.info("Checking url: {}", allTasks.get(i).getUrl());
            }
        }
    }
}
