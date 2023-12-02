package com.fanstatic.controller.manage;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.config.constants.WebsocketConst;
import com.fanstatic.controller.order.WSPurcharseOrderController;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.product.ProductChangeStockDTO;
import com.fanstatic.dto.model.product.ProductRequestDTO;
import com.fanstatic.service.model.ProductService;
import com.fanstatic.service.model.ProductVarientService;
import com.fanstatic.util.ResponseUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/manage/productstock")
@AllArgsConstructor
public class ProductStockController {
    private final ProductService productService;
    private final ProductVarientService productVarientService;
    private final WSPurcharseOrderController wsPurcharseOrderController;

    @PutMapping("/change/product")
    @ResponseBody
    public ResponseEntity<ResponseDTO> changeProductOutOfStock(
            @RequestBody @Valid ProductChangeStockDTO productChangeStockDTO) {

        ResponseDTO responseDTO = productService.changeIsOutOfStock(productChangeStockDTO);

        if (responseDTO.isSuccess()) {

            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_PRODUCT_CHANGE_STOCK);

        }
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/change/product-variant")
    @ResponseBody
    public ResponseEntity<ResponseDTO> changeProductVarientStock(
            @RequestBody @Valid ProductChangeStockDTO productChangeStockDTO) {

        ResponseDTO responseDTO = productService.changeProductVarientIsOutOfStock(productChangeStockDTO);

        if (responseDTO.isSuccess()) {
            wsPurcharseOrderController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_PRODUCT_CHANGE_STOCK);
        }
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }
}
