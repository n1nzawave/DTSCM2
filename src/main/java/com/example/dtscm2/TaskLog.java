package com.example.dtscm2;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_log")
public class TaskLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id", nullable = false)
    private Long task_id;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "status_code", nullable = false)
    private int status_code;

    @Column(name = "latency_ms", nullable = false)
    private Long latency_ms;

    @Column(name = "is_success", nullable = false)
    private boolean is_success;

    public TaskLog(Long task_id, LocalDateTime timestamp, int status_code, Long latency_ms, boolean is_success) {
        this.task_id = task_id;
        this.timestamp = timestamp;
        this.status_code = status_code;
        this.latency_ms = latency_ms;
        this.is_success = is_success;
    }

    public TaskLog(){}

    public Long getTask_id() {
        return task_id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus_code() {
        return status_code;
    }

    public Long getLatency_ms() {
        return latency_ms;
    }

    public boolean isIs_success() {
        return is_success;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTask_id(Long task_id) {
        this.task_id = task_id;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public void setLatency_ms(Long latency_ms) {
        this.latency_ms = latency_ms;
    }

    public void setIs_success(boolean is_success) {
        this.is_success = is_success;
    }

}
