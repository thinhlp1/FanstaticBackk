package com.fanstatic.controller.order;

import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.fanstatic.config.constants.WebsocketConst;
import com.fanstatic.config.websocket.WebSocketResponseController;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.auth.LoginDTO;
import com.fanstatic.dto.model.account.AccountDTO;
import com.fanstatic.dto.model.order.request.OrderRequestDTO;
import com.fanstatic.service.order.OrderService;
import com.fanstatic.service.system.WebsocketService;
import com.fanstatic.util.ResponseUtils;
import com.fanstatic.util.SessionUtils;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final OrderService orderService;
    private final WSPurcharseOrderController wsPurcharseOrderController;

    @PostMapping("/purcharse/order/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid OrderRequestDTO orderRequestDTO) {
        ResponseDTO responseDTO = orderService.create(orderRequestDTO);
        wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_NEW);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/purcharse/order/show/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> detail(@PathVariable Integer id) {
        ResponseDTO responseDTO = orderService.detail(id);
        wsPurcharseOrderController.sendWebSocketResponse(id, responseDTO);
        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @GetMapping("/purcharse/order/update/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@PathVariable Integer id) {
        ResponseDTO responseDTO = orderService.detail(id);
        wsPurcharseOrderController.sendWebSocketResponse(id, responseDTO);
        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @GetMapping("/purcharse/order/show/current-order")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showCurrentOrder() {
        ResponseDTO responseDTO = orderService.getListOrder();
        wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_CURRENT_ORDER);
        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PostMapping("/purcharse/order/create/re-order/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> reOrder(@RequestBody @Valid OrderRequestDTO orderRequestDTO,
            @PathVariable Integer id) {
        System.out.println("===============================1");
        ResponseDTO responseDTO = orderService.reOrder(orderRequestDTO, id);
        wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_WAIT_CONFIRM_ORDER);
        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PutMapping("/purcharse/order/confirm/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> confirmOrder(@PathVariable Integer id) {
        ResponseDTO responseDTO = orderService.confirm(id);
        wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_CURRENT_ORDER);
        wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_DETAILS);

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PutMapping("/purcharse/order/delete/cancel/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> cancelOrder(@PathVariable Integer id) {
        ResponseDTO responseDTO = orderService.cancel(id);
        wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_CURRENT_ORDER);
        wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_DETAILS);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    // @MessageMapping("/order/delete")
    // @SendTo("/topic/order/delete")
    // public ResponseDTO greet3(LoginDTO loginDTO) throws InterruptedException {
    // AccountDTO accountDTO = new AccountDTO();
    // accountDTO.setNumberPhone("0334831013");
    // accountDTO.setPassword("12082003az9");
    // System.err.println("DELETE");

    // return ResponseUtils.success(200, "XÓA ORDER", accountDTO);
    // }
}
