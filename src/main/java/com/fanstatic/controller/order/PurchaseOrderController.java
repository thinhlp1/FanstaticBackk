package com.fanstatic.controller.order;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fanstatic.config.constants.WebsocketConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.order.checkout.CheckVoucherRequestDTO;
import com.fanstatic.dto.model.order.checkout.CheckoutRequestDTO;
import com.fanstatic.dto.model.order.checkout.ConfirmCheckoutRequestDTO;
import com.fanstatic.dto.model.order.request.CancalOrderRequestDTO;
import com.fanstatic.dto.model.order.request.OrderRequestDTO;
import com.fanstatic.dto.model.order.request.SwitchOrderRequestDTO;
import com.fanstatic.service.order.OrderService;
import com.fanstatic.util.ResponseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final OrderService orderService;
    private final WSPurcharseOrderController wsPurcharseOrderController;

    @PostMapping("/api/purchase/order/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid OrderRequestDTO orderRequestDTO) {
        ResponseDTO responseDTO = orderService.create(orderRequestDTO);

        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_NEW);
        }

        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/api/purchase/order/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> detail(@PathVariable Integer id) {
        ResponseDTO responseDTO = orderService.detail(id);

        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + id);

        }

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @GetMapping("/api/purchase/order/update/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> update(@PathVariable Integer id) {
        ResponseDTO responseDTO = orderService.detail(id);

        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);

        }

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @GetMapping("/api/purchase/order/show/current-order")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showCurrentOrder() {
        ResponseDTO responseDTO = orderService.getListOrder();

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @GetMapping("/api/purchase/order/show/await-checkout")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showAwaitCheckout() {
        ResponseDTO responseDTO = orderService.getListOrderAwaitCheckout();

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PostMapping("/api/purchase/order/create/re-order/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> reOrder(@RequestBody @Valid OrderRequestDTO orderRequestDTO,
            @PathVariable Integer id) {
        ResponseDTO responseDTO = orderService.reOrder(orderRequestDTO, id);

        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_NEW);

        }

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PutMapping("/api/purchase/order/confirm/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> confirm(@PathVariable Integer id) {
        ResponseDTO responseDTO = orderService.confirm(id);
        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + id);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);

        }

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PutMapping("/api/purchase/order/cancel")
    @ResponseBody
    public ResponseEntity<ResponseDTO> cancel(@RequestBody @Valid CancalOrderRequestDTO cancalOrderRequestDTO) {
        ResponseDTO responseDTO = orderService.cancel(cancalOrderRequestDTO);
        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + cancalOrderRequestDTO.getOrderId());
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);

        }
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/api/purchase/order/switch")
    @ResponseBody
    public ResponseEntity<ResponseDTO> swithOrder(@RequestBody @Valid SwitchOrderRequestDTO switchOrderRequestDTO) {
        ResponseDTO responseDTO = orderService.switchOrder(switchOrderRequestDTO);
        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + switchOrderRequestDTO.getOrderId());
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);

        }
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/api/purchase/order/create/checkout-request")
    @ResponseBody
    public ResponseEntity<ResponseDTO> checkoutOrderRequest(@RequestBody @Valid CheckoutRequestDTO checkoutRequestDTO) {
        ResponseDTO responseDTO = orderService.checkoutRequest(checkoutRequestDTO);
        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + checkoutRequestDTO.getOrderId());
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_CHECKOUT_REQUEST);

        }
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/api/purchase/order/checkout")
    @ResponseBody
    public ResponseEntity<ResponseDTO> checkoutOrder(
            @RequestBody @Valid ConfirmCheckoutRequestDTO confirmCheckoutRequestDTO) {
        ResponseDTO responseDTO = orderService.confirmCheckoutOrder(confirmCheckoutRequestDTO);
        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + confirmCheckoutRequestDTO.getOrderId());
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_AWAIT_CHECKOUT);

        }
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/api/purchase/order/create/check-voucher")
    @ResponseBody
    public ResponseEntity<ResponseDTO> checkVoucner(@RequestBody @Valid CheckVoucherRequestDTO checkoutRequestDTO) {
        ResponseDTO responseDTO = orderService.checkVoucherApply(checkoutRequestDTO);

        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/api/purchase/order/create/test")
    @ResponseBody
    public ResponseEntity<ResponseDTO> test() {
        ResponseDTO responseDTO = orderService.test();

        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/handle-checkout")
    @ResponseBody
    public ResponseEntity<ResponseDTO> handleCheckout(@RequestParam("orderCode") Integer ordercode) {

        ResponseDTO responseDTO = orderService.handleCheckout(ordercode);
        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + orderService.getOrderIdFromOrderCode(ordercode));
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_CHECKOUT_PAID);

        }
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/cancel-checkout")
    @ResponseBody
    public ResponseEntity<ResponseDTO> cancelCheckout(@RequestParam("orderCode") Integer ordercode) {

        ResponseDTO responseDTO = orderService.cacncelCheckout(ordercode);
        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + orderService.getOrderIdFromOrderCode(ordercode));
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_CANCEL_CHECKOUT);

        }

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
