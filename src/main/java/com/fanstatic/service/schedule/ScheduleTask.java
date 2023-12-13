package com.fanstatic.service.schedule;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fanstatic.service.model.NotificationService;
import com.fanstatic.util.DateUtils;

@Component
public class ScheduleTask {

    @Autowired
    private NotificationService notificationService;

    @Scheduled(cron = "0 0 22 * * *")
    public void clearNotification() {
        // thực hiện vào cuối ngày
        System.out.println("Task is running on the last day of the month...");
        notificationService.cleareNotification();

    }
}
