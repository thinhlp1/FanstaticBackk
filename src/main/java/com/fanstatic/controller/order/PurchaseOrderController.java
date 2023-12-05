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
import com.fanstatic.dto.model.order.edit.CompleteOrderItemDTO;
import com.fanstatic.dto.model.order.edit.OrderExtraPortionRemoveDTO;
import com.fanstatic.dto.model.order.edit.OrderExtraPortionUpdateDTO;
import com.fanstatic.dto.model.order.edit.OrderItemRemoveDTO;
import com.fanstatic.dto.model.order.edit.OrderItemUpdateDTO;
import com.fanstatic.dto.model.order.edit.OrderNewItemDTO;
import com.fanstatic.dto.model.order.edit.OrderUpdateDTO;
import com.fanstatic.dto.model.order.request.CancelOrderrequestDTO;
import com.fanstatic.dto.model.order.request.ExtraPortionOrderRequestDTO;
import com.fanstatic.dto.model.order.request.OrderItemRequestDTO;
import com.fanstatic.dto.model.order.request.OrderRequestDTO;
import com.fanstatic.dto.model.order.request.SwitchOrderRequestDTO;
import com.fanstatic.service.model.UserService;
import com.fanstatic.service.order.OrderService;
import com.fanstatic.util.ResponseUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final WSPurcharseOrderController wsPurcharseOrderController;

    @GetMapping("/api/u/order/check-table")
    @ResponseBody
    public ResponseEntity<ResponseDTO> checkTable(@RequestParam("table") Integer table) {
        ResponseDTO responseDTO = orderService.checkTableOrdered(table);

        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/api/purchase/order/create/check-user-order")
    @ResponseBody
    public ResponseEntity<ResponseDTO> check() {
        ResponseDTO responseDTO = orderService.checkUserHasOrder();

        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PostMapping("/api/purchase/order/create/check-user-exits")
    @ResponseBody
    public ResponseEntity<ResponseDTO> checkUserExit(@RequestBody String numberPhone) {
        System.out.println(numberPhone);
        ResponseDTO responseDTO = userService.checkCustomerExits(numberPhone);

        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PostMapping("/api/purchase/order/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid OrderRequestDTO orderRequestDTO) {
        ResponseDTO responseDTO = orderService.create(orderRequestDTO);

        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_NEW);
        }

        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/api/purchase/order/create/get-table")
    @ResponseBody
    public ResponseEntity<ResponseDTO> tables() {
        ResponseDTO responseDTO = orderService.getListTable();

        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/api/purchase/order/create/check-customer-exits")
    @ResponseBody
    public ResponseEntity<ResponseDTO> checkCustomer(
            @RequestParam(name = "numberPhone", required = true) String numberPhone) {
        ResponseDTO responseDTO = orderService.checkCustomerExits(numberPhone);

        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/api/purchase/order/detail/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> detail(@PathVariable Integer id) {
        ResponseDTO responseDTO = orderService.detail(id);

//        if (responseDTO.isSuccess()) {
//            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
//                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + id);
//
//        }

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @GetMapping("/api/purchase/order/detail/table/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> detailOnTable(@PathVariable Integer id) {
        ResponseDTO responseDTO = orderService.detailInTable(id);

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @GetMapping("/api/purchase/order/create/order-voucher/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> getVoucherCanApply(@PathVariable Integer id) {
        ResponseDTO responseDTO = orderService.getVoucherCanApply(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/api/purchase/order/create/order-point/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> getPoint(@PathVariable Integer id) {
        ResponseDTO responseDTO = orderService.getPoint(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/api/purchase/order/create/payment-method")
    @ResponseBody
    public ResponseEntity<ResponseDTO> getPaymentMethod() {
        ResponseDTO responseDTO = orderService.getPaymentMethod();
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/api/purchase/order/update/new-item")
    @ResponseBody
    public ResponseEntity<ResponseDTO> addToOrder(@RequestBody @Valid OrderNewItemDTO orderNewItemDTO) {
        ResponseDTO responseDTO = orderService.addToOrder(orderNewItemDTO);

        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + orderNewItemDTO.getOrderId());
        }
        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PutMapping("/api/purchase/order/update")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateOrder(@RequestBody @Valid OrderUpdateDTO orderUpdateDTO) {
        ResponseDTO responseDTO = orderService.updateOrder(orderUpdateDTO);

        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + orderUpdateDTO.getOrderId());
        }
        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PutMapping("/api/purchase/order/create/customer/update-people/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> cUpdatePeople(@PathVariable Integer id, @RequestBody Integer people) {
        ResponseDTO responseDTO = orderService.updatePeople(people, id);

        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + id);
        }
        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PutMapping("/api/purchase/order/update/update-people/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updatePeople(@PathVariable Integer id, @RequestBody Integer people) {
        ResponseDTO responseDTO = orderService.updatePeople(people, id);

        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + id);
        }

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PutMapping("/api/purchase/order/update/order-item")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateOrderItem(@RequestBody @Valid OrderItemUpdateDTO orderItemUpdateDTO) {
        ResponseDTO responseDTO = orderService.updateOrderItem(orderItemUpdateDTO);

        if (responseDTO.isSuccess()) {
            ResponseDTO responseDTO2 = orderService.detail(orderItemUpdateDTO.getOrderId());
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO2, WebsocketConst.TOPIC_ORDER_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO2,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + orderItemUpdateDTO.getOrderId());
        }

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PutMapping("/api/purchase/order/update/order-extra-portion")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateOrderExtraPortion(
            @RequestBody @Valid OrderExtraPortionUpdateDTO orderExtraPortionUpdateDTO) {
        ResponseDTO responseDTO = orderService.updateOrderExtraPortion(orderExtraPortionUpdateDTO);

        if (responseDTO.isSuccess()) {

            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + orderExtraPortionUpdateDTO.getOrderId());
        }

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PutMapping("/api/purchase/order/update/remove-order-item")
    @ResponseBody
    public ResponseEntity<ResponseDTO> removeOrderItem(
            @RequestBody @Valid OrderItemRemoveDTO orderItemRemoveDTO) {
        ResponseDTO responseDTO = orderService.removeOrderItem(orderItemRemoveDTO);

        if (responseDTO.isSuccess()) {

            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + orderItemRemoveDTO.getOrderId());
        }

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PutMapping("/api/purchase/order/update/remove-order-extra-portion")
    @ResponseBody
    public ResponseEntity<ResponseDTO> removeOrderExtraPortion(
            @RequestBody @Valid OrderExtraPortionRemoveDTO orderExtraPortionRemoveDTO) {
        ResponseDTO responseDTO = orderService.removeOrderExtraPortion(orderExtraPortionRemoveDTO);

        if (responseDTO.isSuccess()) {

            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + orderExtraPortionRemoveDTO.getOrderId());
        }

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PutMapping("/api/purchase/order/update/add-order-item")
    @ResponseBody
    public ResponseEntity<ResponseDTO> addOrderItem(
            @RequestBody @Valid OrderItemRequestDTO orderItemRequestDTO) {
        ResponseDTO responseDTO = orderService.addNewOrderItem(orderItemRequestDTO);

        if (responseDTO.isSuccess()) {

            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + orderItemRequestDTO.getOrderId());
        }

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PutMapping("/api/purchase/order/update/add-order-extra-portion")
    @ResponseBody
    public ResponseEntity<ResponseDTO> addOrderExtraPortion(
            @RequestBody @Valid ExtraPortionOrderRequestDTO extraPortionOrderRequestDTO) {
        ResponseDTO responseDTO = orderService.addNewOrderExtraPortion(extraPortionOrderRequestDTO);

        if (responseDTO.isSuccess()) {

            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + extraPortionOrderRequestDTO.getOrderId());
        }

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PutMapping("/api/purchase/order/update/complete-order-item")
    @ResponseBody
    public ResponseEntity<ResponseDTO> completeOrderItem(
            @RequestBody @Valid CompleteOrderItemDTO completeOrderItemDTO) {
        ResponseDTO responseDTO = orderService.completeOrderItem(completeOrderItemDTO);

        if (responseDTO.isSuccess()) {

            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_UPDATE);
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
                    WebsocketConst.TOPIC_ORDER_DETAILS + "/" + completeOrderItemDTO.getOrderId());
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

    @PostMapping("/api/purchase/order/create/re-order")
    @ResponseBody
    public ResponseEntity<ResponseDTO> reOrder(@RequestBody @Valid OrderRequestDTO orderRequestDTO) {
        ResponseDTO responseDTO = orderService.reOrder(orderRequestDTO);

        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_NEW);

        }

        return ResponseUtils.returnReponsetoClient(responseDTO);

    }

    @PostMapping("/api/purchase/order/update")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateOrder(@RequestBody @Valid OrderRequestDTO orderRequestDTO,
            @PathVariable Integer id) {
        ResponseDTO responseDTO = orderService.reOrder(orderRequestDTO);
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
    public ResponseEntity<ResponseDTO> cancel(@RequestBody @Valid CancelOrderrequestDTO cancalOrderRequestDTO) {
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
            // wsPurcharseOrderController.sendWebSocketResponse(responseDTO,
            // WebsocketConst.TOPIC_ORDER_DETAILS + "/" + checkoutRequestDTO.getOrderId());
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
}
