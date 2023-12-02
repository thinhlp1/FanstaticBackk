package com.fanstatic.controller.manage;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.service.order.OrderService;
import com.fanstatic.util.ResponseUtils;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/manage/order")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/show/today")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showToday() {
        ResponseDTO responseDTO = orderService.showToDay();
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show/day")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showInDay(@RequestParam(required = true) Integer day) {
        ResponseDTO responseDTO = orderService.showInDay(day);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/show/time")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showInTime(@RequestParam(required = false) Integer month, @RequestParam(required = false) Integer year) {
        ResponseDTO responseDTO = orderService.showInTime(year, month);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    // @GetMapping("/show/time")
    // @ResponseBody
    // public ResponseEntity<ResponseDTO> show() {
    // ResponseDTO responseDTO = hotProductSerivce.show();
    // return ResponseUtils.returnReponsetoClient(responseDTO);
    // }
}
