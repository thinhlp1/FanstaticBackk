package com.fanstatic.dto.model.requestStaff;

import java.util.Date;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.customer.CustomerDTO;
import com.fanstatic.dto.model.order.OrderDTO;
import com.fanstatic.dto.model.user.UserCompactDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestStaffNotificationDTO extends ResponseDataDTO{
    private CustomerDTO customer;

    private UserCompactDTO employeeConfirm;

    private String content;

    private Date createAt;

    private Date confirmAt;

    private String status;

    private Date denyAt;

    private OrderDTO orderDTO;

}
