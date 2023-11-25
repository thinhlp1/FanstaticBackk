package com.fanstatic.dto.model.profile;

import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.customer.CustomerDTO;
import com.fanstatic.dto.model.order.OrderDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileUserDTO extends ResponseDataDTO {
    private CustomerDTO customerDTO;

    private List<ResponseDataDTO> orderDTOs;
}
