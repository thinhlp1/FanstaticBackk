package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.comboProduct.ComboProductDTO;
import com.fanstatic.dto.model.product.ProductDTO;
import com.fanstatic.dto.model.product.ProductForSaleDTO;
import com.fanstatic.dto.model.product.ProductVarientDTO;
import com.fanstatic.dto.model.saleEventProduct.GetAllProductDTO;
import com.fanstatic.dto.model.saleEventProduct.ObjectSaleDTO;
import com.fanstatic.dto.model.saleEventProduct.SaleProductByIdDTO;
import com.fanstatic.dto.model.saleEventProduct.SaleProductDTO;
import com.fanstatic.dto.model.saleEventProduct.SaleProductRequestDTO;
import com.fanstatic.dto.model.supplier.SupplierDTO;
import com.fanstatic.model.ComboProduct;
import com.fanstatic.model.Product;
import com.fanstatic.model.ProductVarient;
import com.fanstatic.model.SaleEvent;
import com.fanstatic.model.SaleProduct;
import com.fanstatic.model.Supplier;
import com.fanstatic.repository.ComboProductRepository;
import com.fanstatic.repository.ProductRepository;
import com.fanstatic.repository.ProductVarientRepository;
import com.fanstatic.repository.SaleEventRepository;
import com.fanstatic.repository.SaleProductRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleProductService {
    private final SaleProductRepository saleProductRepository;
    private final ModelMapper modelMapper;
    private final SystemService systemService;
    private final ProductRepository productRepository;
    private final ProductVarientRepository productVarientRepository;
    private final ComboProductRepository comboProductRepository;
    private final SaleEventRepository saleEventRepository;

    public ResponseDTO create(SaleProductRequestDTO saleProductRequestDTO) {

        SaleEvent saleEvent = saleEventRepository.findById(saleProductRequestDTO.getSaleEvent()).orElse(null);
        if (saleProductRequestDTO.getProduct().size() > 0) {
            for (int i = 0; i < saleProductRequestDTO.getProduct().size(); i++) {
                Product product = productRepository.findById(saleProductRequestDTO.getProduct().get(i)).orElse(null);
                SaleProduct saleProduct = new SaleProduct();
                saleProduct.setSaleEvent(saleEvent);
                saleProduct.setProduct(product);
                SaleProduct saleProductSaved = saleProductRepository.saveAndFlush(saleProduct);
                systemService.writeSystemLog(saleProductSaved.getId(), null, null);
            }
        }

        if (saleProductRequestDTO.getComboProduct().size() > 0) {
            for (int i = 0; i < saleProductRequestDTO.getComboProduct().size(); i++) {
                ComboProduct comboProduct = comboProductRepository
                        .findById(saleProductRequestDTO.getComboProduct().get(i)).orElse(null);
                SaleProduct saleProduct = new SaleProduct();

                saleProduct.setSaleEvent(saleEvent);
                saleProduct.setComboProduct(comboProduct);
                SaleProduct saleProductSaved = saleProductRepository.saveAndFlush(saleProduct);
                systemService.writeSystemLog(saleProductSaved.getId(), null, null);

            }
        }
        if (saleProductRequestDTO.getProductVarient().size() > 0) {
            for (int i = 0; i < saleProductRequestDTO.getProductVarient().size(); i++) {
                ProductVarient productVarient = productVarientRepository
                        .findById(saleProductRequestDTO.getProductVarient().get(i)).orElse(null);
                SaleProduct saleProduct = new SaleProduct();
                saleProduct.setSaleEvent(saleEvent);
                saleProduct.setProductVarient(productVarient);
                SaleProduct saleProductSaved = saleProductRepository.saveAndFlush(saleProduct);
                systemService.writeSystemLog(saleProductSaved.getId(), null, null);
            }
        }
        return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);

    }

    public ResponseDTO update(SaleProductRequestDTO saleProductRequestDTO) {

        SaleProduct saleProduct = saleProductRepository.findByIdAndActiveIsTrue(saleProductRequestDTO.getId())
                .orElse(null);

        if (saleProduct == null) {
            return ResponseUtils.fail(401, "Sự kiện không tồn tại", null);
        }

        modelMapper.map(saleProductRequestDTO, saleProduct);

        saleProduct.setUpdateAt(new Date());
        saleProduct.setUpdateBy(systemService.getUserLogin());
        saleProduct.setActive(DataConst.ACTIVE_TRUE);
        SaleProduct saleProductSaved = saleProductRepository.save(saleProduct);

        systemService.writeSystemLog(saleProductSaved.getId(), null, null);

        return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

    }

    public ResponseDTO delete(int id) {

        SaleProduct saleProduct = saleProductRepository.findByIdAndActiveIsTrue(id).orElse(null);

        if (saleProduct == null) {
            return ResponseUtils.fail(401, "Sự kiện không tồn tại", null);
        }

        saleProduct.setActive(DataConst.ACTIVE_FALSE);
        saleProduct.setDeleteAt(new Date());
        saleProduct.setDeleteBy(systemService.getUserLogin());

        SaleProduct saleProductSaved = saleProductRepository.save(saleProduct);

        systemService.writeSystemLog(saleProductSaved.getId(), null, null);
        return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);

    }

    public ResponseDTO restore(int id) {

        SaleProduct saleProduct = saleProductRepository.findByIdAndActiveIsFalse(id).orElse(null);

        if (saleProduct == null) {
            return ResponseUtils.fail(401, "Sự kiện không tồn tại", null);
        }

        saleProduct.setActive(DataConst.ACTIVE_TRUE);
        saleProduct.setUpdateAt(new Date());
        saleProduct.setUpdateBy(systemService.getUserLogin());

        SaleProduct saleProductSaved = saleProductRepository.save(saleProduct);

        systemService.writeSystemLog(saleProductSaved.getId(), null, null);

        return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);

    }

    // public ResponseDTO detail(int id) {

    // SaleProduct saleProduct = saleProductRepository.findById(id).orElse(null);

    // if (saleProduct == null) {
    // return ResponseUtils.fail(401, "SaleEvent không tồn tại", null);
    // }

    // SaleProductDTO saleProductDTO = modelMapper.map(saleProduct,
    // SaleProductDTO.class);

    // return ResponseUtils.success(200, "Chi tiết sự kiện.", saleProductDTO);

    // }

    public ResponseDTO show(int active) {
        List<SaleProduct> saleProducts = new ArrayList<>();

        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                saleProducts = saleProductRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_TRUE:
                saleProducts = saleProductRepository.findAllByActiveIsTrue().orElse(saleProducts);
                break;
            case RequestParamConst.ACTIVE_FALSE:
                saleProducts = saleProductRepository.findAllByActiveIsFalse().orElse(saleProducts);
                break;
            default:
                saleProducts = saleProductRepository.findAll();
                break;
        }
        List<ResponseDataDTO> saleProductDTOS = new ArrayList<>();

        for (SaleProduct saleProduct : saleProducts) {

            SaleProductDTO saleProductDTO = new SaleProductDTO();

            modelMapper.map(saleProduct, saleProductDTO);

            saleProductDTOS.add(saleProductDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(saleProductDTOS);
        return ResponseUtils.success(200, "Danh sách sản phẩm sale", reponseListDataDTO);
    }

    public ResponseDTO getAllProductAndProductVariantAndComProduct() {

        List<ComboProduct> comboProducts = new ArrayList<>();
        List<ProductVarient> productVariants = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        products = productRepository.findAll();
        productVariants = productVarientRepository.findAll();
        comboProducts = comboProductRepository.findAll();

        List<ResponseDataDTO> listAllProduct = new ArrayList<>();

        List<ProductForSaleDTO> listProducts = new ArrayList<>();
        GetAllProductDTO getAllProducts = new GetAllProductDTO();
        for (Product product : products) {
            // Chuyển đổi từ Product sang ProductForSaleDTO

            ProductForSaleDTO productDTO = new ProductForSaleDTO();
            modelMapper.map(product, productDTO);

            listProducts.add(productDTO);

        }
        getAllProducts.setProducts(listProducts);

        List<ComboProductDTO> listCombo = new ArrayList<>();

        for (ComboProduct comboProduct : comboProducts) {
            ComboProductDTO comboProductDTO = new ComboProductDTO();
            modelMapper.map(comboProduct, comboProductDTO);
            listCombo.add(comboProductDTO);
        }

        getAllProducts.setComboProducts(listCombo);

        List<ProductVarientDTO> listVarients = new ArrayList<>();

        for (ProductVarient productVarient : productVariants) {
            ProductVarientDTO productVarientDTO = new ProductVarientDTO();
            modelMapper.map(productVarient, productVarientDTO);
            listVarients.add(productVarientDTO);
        }
        getAllProducts.setProductVarients(listVarients);

        listAllProduct.add(getAllProducts);

        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(listAllProduct);
        return ResponseUtils.success(200, "Danh sách sản phẩm", reponseListDataDTO);
    }

    public ResponseDTO getProductBySaleEventId(int id) {
        List<SaleProduct> saleProducts = new ArrayList<>();
        saleProducts = saleProductRepository.findAll();
        List<ResponseDataDTO> saleProductDTOS = new ArrayList<>();
         List<SaleProduct> listSaleProductBySaleEventId = new ArrayList<>();
        List<ProductForSaleDTO> listProduct = new ArrayList<>();
        List<ProductVarientDTO> listProductVariant = new ArrayList<>();
        List<ComboProductDTO> listComboProduct = new ArrayList<>();
        

        SaleProductByIdDTO saleProductDTO = new SaleProductByIdDTO();
        for (SaleProduct saleProduct : saleProducts) {
            if(saleProduct.getSaleEvent().getId() == id) {
               listSaleProductBySaleEventId.add(saleProduct);
            }
        }

        System.out.println("Độ dài của saleeventid " + listSaleProductBySaleEventId.size());
        if (listSaleProductBySaleEventId.size() > 0) {
            for(SaleProduct saleProduct : listSaleProductBySaleEventId) {
            if (saleProduct.getComboProduct() != null) {
                  if (saleProduct.getComboProduct().getId() != null) {
                    ComboProductDTO comboProductDTO = new ComboProductDTO();
                    modelMapper.map(saleProduct.getComboProduct(), comboProductDTO);
                    listComboProduct.add(comboProductDTO);
            }
            }
            if (saleProduct.getProductVarient() != null) {
                  if (saleProduct.getProductVarient().getId() != null) {
                ProductVarientDTO productVarientDTO = new ProductVarientDTO();
                 modelMapper.map(saleProduct.getProductVarient(), productVarientDTO);
                listProductVariant.add(productVarientDTO);
            }
            }
              if (saleProduct.getProduct() != null) {
                  if (saleProduct.getProduct().getId() != null) {
                ProductForSaleDTO productForSaleDTO = new ProductForSaleDTO();
                 modelMapper.map(saleProduct.getProduct(), productForSaleDTO);
                listProduct.add(productForSaleDTO);
            }
            }
            
            }
        }

        if (listComboProduct.size() > 0) {
                   saleProductDTO.setComboProducts(listComboProduct);
        }
         if (listProductVariant.size() > 0) {
                saleProductDTO.setProductVarients(listProductVariant);
        }
           if (listProduct.size() > 0) {
                 saleProductDTO.setProducts(listProduct);
        }
        saleProductDTO.setSaleEventId(id);
        saleProductDTOS.add(saleProductDTO); 
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(saleProductDTOS);
        return ResponseUtils.success(200, "Danh sách sản phẩm", reponseListDataDTO);
    }
}
