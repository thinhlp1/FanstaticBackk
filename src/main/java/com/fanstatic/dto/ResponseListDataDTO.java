package com.fanstatic.dto;

import java.util.List;

import com.fanstatic.dto.model.permissioin.FeaturePermissionDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseListDataDTO extends ResponseDataDTO {
    private String nameList;

    private List<ResponseDataDTO> datas;

}
