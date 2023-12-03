package com.fanstatic.dto.model.notification;

import java.util.Date;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationDTO extends ResponseDataDTO{
    private int id;

    private String title;

    private String content;

    private String action;

    private Date seenAt;

    private boolean hasSeen;

}
