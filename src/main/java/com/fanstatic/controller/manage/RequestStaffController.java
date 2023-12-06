package com.fanstatic.controller.manage;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.config.constants.WebsocketConst;
import com.fanstatic.controller.order.WSPurcharseOrderController;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.product.ProductChangeStockDTO;
import com.fanstatic.dto.model.requestStaff.CreateRequestStaffNotificationDTO;
import com.fanstatic.service.model.RequestStaffNotifacationService;
import com.fanstatic.service.system.PushNotificationService;
import com.fanstatic.util.ResponseUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RequestStaffController {
    private final RequestStaffNotifacationService requestStaffNotifacationService;
    private final WSPurcharseOrderController wsPurcharseOrderController;

    @PostMapping("/api/request-staff/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(
            @RequestBody CreateRequestStaffNotificationDTO createRequestStaffNotificationDTO) {

        ResponseDTO responseDTO = requestStaffNotifacationService.create(createRequestStaffNotificationDTO);

        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_REQUEST_STAFF_NEW);
        }

        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/api/manage/notification/update/confirm/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> confirm(@PathVariable("id") Integer id) {

        ResponseDTO responseDTO = requestStaffNotifacationService.confirm(id);
        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_REQUEST_STAFF_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_REQUEST_STAFF_DETAIL + "/" + id);

        }

        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/api/manage/notification/update/deny/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> deny(@PathVariable("id") Integer id) {

        ResponseDTO responseDTO = requestStaffNotifacationService.deny(id);
        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_REQUEST_STAFF_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_REQUEST_STAFF_DETAIL + "/" + id);

        }

        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/api/manage/notification/show")
    @ResponseBody
    public ResponseEntity<ResponseDTO> show() {

        ResponseDTO responseDTO = requestStaffNotifacationService.show();

        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/api/manage/notification/show/today")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showToday() {

        ResponseDTO responseDTO = requestStaffNotifacationService.showToDay();

        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

}
