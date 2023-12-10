package com.fanstatic.dto.model.notification;

import java.util.Date;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.user.UserCompactDTO;
import com.fanstatic.dto.model.user.UserDTO;

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

    private Date sendAt;

    private boolean hasSeen;

    private UserCompactDTO sender;

}
