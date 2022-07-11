package com.javagoogle2.google2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

//esto para hacer que se ejecute todas las noches
@Configuration
@EnableScheduling
public class ScheduleService {
    
    @Autowired
    private SpiderService spiderService;


    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleIndexWebPages(){
        spiderService.indexWebPages();
    }
}
