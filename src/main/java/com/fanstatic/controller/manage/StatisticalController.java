package com.fanstatic.controller.manage;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fanstatic.dto.ResponseDTO;

import com.fanstatic.service.model.StatisticalService;
import com.fanstatic.util.ResponseUtils;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/manage/statistical")
@AllArgsConstructor
public class StatisticalController {
    private final StatisticalService statisticalService;

    @GetMapping("/show/revenue")
    @ResponseBody
    public ResponseEntity<ResponseDTO> GetRevenue(@RequestParam(required = false) Integer year) {
        ResponseDTO reponseDTO = statisticalService.calculateRevenueByMonths(year);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

     @GetMapping("/show/revenueby")
    @ResponseBody
    public ResponseEntity<ResponseDTO> GetRevenueByDayAndMonth(@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month) {
        ResponseDTO reponseDTO = statisticalService.calculateRevenueByMonths(year);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/oder")
    @ResponseBody
    public ResponseEntity<ResponseDTO> GetOder(@RequestParam(required = false) Integer year) {
        ResponseDTO reponseDTO = statisticalService.countOrdersByMonths(year);

        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/customer")
    @ResponseBody
    public ResponseEntity<ResponseDTO> GetCustomer(@RequestParam(required = false) Integer year) {
        ResponseDTO reponseDTO = statisticalService.countCustomersByMonths(year);

        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/dashboard")
    @ResponseBody
    public ResponseEntity<ResponseDTO> GetDashboard() {
        ResponseDTO reponseDTO = statisticalService.analysisDashBoardOverViewData();
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/product")
    @ResponseBody
    public ResponseEntity<ResponseDTO> GetProduct(@RequestParam(required = false) Integer year) {
        ResponseDTO reponseDTO = statisticalService.findTop10BestSellingProducts(year);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

}
