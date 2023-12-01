package com.fanstatic.controller.publics;



import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.model.ComboProduct;
import com.fanstatic.service.model.CategoryService;
import com.fanstatic.service.model.ComboProductService;
import com.fanstatic.service.model.ExtraPortionService;
import com.fanstatic.service.model.HotProductSerivce;
import com.fanstatic.service.model.ProductService;
import com.fanstatic.service.model.SizeService;
import com.fanstatic.util.ResponseUtils;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/u/order")
public class PlaceOrderController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ExtraPortionService extraPortionService;
    private final ComboProductService comboProductService;
    private final HotProductSerivce hotProductSerivce;
    private final SizeService sizeService;

    @GetMapping("/show/products")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showProduct() {
        ResponseDTO reponseDTO = productService.show(RequestParamConst.ACTIVE_TRUE);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/products-by-category")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showProductByCategory(@RequestParam Integer category) {
        ResponseDTO reponseDTO = productService.showByCategoryId(category);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/categories")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showCategory() {
        ResponseDTO reponseDTO = categoryService.show(RequestParamConst.ACTIVE_TRUE);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/extra-portion")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showExtraPortion() {
        ResponseDTO reponseDTO = extraPortionService.show(RequestParamConst.ACTIVE_TRUE);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/combo-product")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showComboProduct() {
        ResponseDTO reponseDTO = comboProductService.show(RequestParamConst.ACTIVE_TRUE);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/hot-product")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showHotProduct() {
        ResponseDTO reponseDTO = hotProductSerivce.showOnOrder();
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/show/size")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showSize() {
        ResponseDTO reponseDTO = sizeService.show(RequestParamConst.ACTIVE_TRUE);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }
}
