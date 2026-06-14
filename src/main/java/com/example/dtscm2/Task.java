package com.example.dtscm2;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private double intervalSeconds;
    private boolean status;
    private LocalDateTime lastCheckedAt;

    public Task(){}

    public Task(Long id, String url, double intervalSeconds, boolean status, LocalDateTime lastCheckedAt){
        this.id = id;
        this.url = url;
        this.intervalSeconds = intervalSeconds;
        this.status = status;
        this.lastCheckedAt = lastCheckedAt;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public double getIntervalSeconds(){
        return intervalSeconds;
    }

    public void setIntervalSeconds(double intervalSeconds){
        this.intervalSeconds = intervalSeconds;
    }

    public boolean getStatus(){
        return status;
    }

    public void setStatus(boolean status){
        this.status = status;
    }

    public LocalDateTime getLastCheckedAt(){
        return lastCheckedAt;
    }

    public void setLastCheckedAt(LocalDateTime lastCheckedAt){
        this.lastCheckedAt = lastCheckedAt;
    }
}
