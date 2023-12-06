package com.fanstatic.dto.model.requestStaff;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateRequestStaffNotificationDTO {
    private String content;

    private Integer orderId;
}
