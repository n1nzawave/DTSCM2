package com.example.dtscm2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @PostMapping("/tasks")
    public Task addTask(@RequestBody Task task){
        return taskRepository.save(task);
    }

    @GetMapping("/tasks")
    public List<Task> getAllTask(){
        return taskRepository.findAll();
    }
}
