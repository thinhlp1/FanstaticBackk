package com.fanstatic.service.model;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.saleevent.SaleEventDTO;
import com.fanstatic.repository.SaleEventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleEventService {
    private final ModelMapper modelMapper;
    private final SaleEventRepository saleEventRepository;

    public ResponseDTO create(SaleEventDTO saleEventDTO) {
        return null;
    }

}
