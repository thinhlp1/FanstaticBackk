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
import com.fanstatic.dto.model.product.ProductNotVariantForSaleDTO;
import com.fanstatic.dto.model.product.ProductVarientDTO;
import com.fanstatic.dto.model.saleEventProduct.GetAllProductDTO;
import com.fanstatic.dto.model.saleEventProduct.SaleProductByIdDTO;
import com.fanstatic.dto.model.saleEventProduct.SaleProductDTO;
import com.fanstatic.dto.model.saleEventProduct.SaleProductRequestDTO;

import com.fanstatic.model.ComboProduct;
import com.fanstatic.model.Product;
import com.fanstatic.model.ProductVarient;
import com.fanstatic.model.SaleEvent;
import com.fanstatic.model.SaleProduct;

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
        if (saleEvent == null) {
            return ResponseUtils.fail(404, "Vui lòng chọn sự kiện sale", null);
        }

        List<SaleProduct> saleProductCheck = saleProductRepository.findBySaleEventId(saleProductRequestDTO.getSaleEvent()).orElse(null);
        if (saleProductCheck.size() <= 0) {
            if (saleProductRequestDTO.getProduct().size() == 0 && saleProductRequestDTO.getComboProduct().size() == 0
                && saleProductRequestDTO.getProductVarient().size() == 0) {
            return ResponseUtils.fail(404, "Vui lòng chọn sản phẩm để sale", null);
        }
        }
        

        List<Integer> listProductFill = checkProduct(saleProductRequestDTO.getProduct(), saleEvent.getId());
        if (listProductFill.size() > 0) {
            for (int i = 0; i < listProductFill.size(); i++) {
                Product product = productRepository.findById(listProductFill.get(i)).orElse(null);
                SaleProduct saleProduct = new SaleProduct();
                saleProduct.setSaleEvent(saleEvent);
                saleProduct.setProduct(product);
                SaleProduct saleProductSaved = saleProductRepository.saveAndFlush(saleProduct);
                systemService.writeSystemLog(saleProductSaved.getId(), null, null);
            }
        }
         List<Integer> listComboProductFill = checkComboProduct(saleProductRequestDTO.getComboProduct(),
                    saleEvent.getId());
        if (listComboProductFill.size() > 0) {
            for (int i = 0; i < listComboProductFill.size(); i++) {
                ComboProduct comboProduct = comboProductRepository
                        .findById(listComboProductFill.get(i)).orElse(null);
                SaleProduct saleProduct = new SaleProduct();

                saleProduct.setSaleEvent(saleEvent);
                saleProduct.setComboProduct(comboProduct);
                SaleProduct saleProductSaved = saleProductRepository.saveAndFlush(saleProduct);
                systemService.writeSystemLog(saleProductSaved.getId(), null, null);

            }
        }
          List<Integer> listProductVariantFill = checkProductVariant(saleProductRequestDTO.getProductVarient(),
                    saleEvent.getId());
        if (listProductVariantFill.size() > 0) {
            for (int i = 0; i < listProductVariantFill.size(); i++) {
                ProductVarient productVarient = productVarientRepository
                        .findById(listProductVariantFill.get(i)).orElse(null);
                SaleProduct saleProduct = new SaleProduct();
                saleProduct.setSaleEvent(saleEvent);
                saleProduct.setProductVarient(productVarient);
                SaleProduct saleProductSaved = saleProductRepository.saveAndFlush(saleProduct);
                systemService.writeSystemLog(saleProductSaved.getId(), null, null);
            }
        }
        return ResponseUtils.success(200, "Áp dụng thành công", null);

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
        // List<ProductVarient> productVariants = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        products = productRepository.findAll();
        comboProducts = comboProductRepository.findAll();

        List<ResponseDataDTO> listAllProduct = new ArrayList<>();
        List<ProductForSaleDTO> listProductVariants = new ArrayList<>();
        List<ProductNotVariantForSaleDTO> listProductNotVariants = new ArrayList<>();
        GetAllProductDTO getAllProducts = new GetAllProductDTO();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductVarients().size() > 0) {
                ProductForSaleDTO productDTO = new ProductForSaleDTO();
                modelMapper.map(products.get(i), productDTO);
                listProductVariants.add(productDTO);
            }

            if (products.get(i).getProductVarients().size() <= 0) {

                ProductNotVariantForSaleDTO productNotVariantDTO = new ProductNotVariantForSaleDTO();
                modelMapper.map(products.get(i), productNotVariantDTO);
                listProductNotVariants.add(productNotVariantDTO);
            }

        }
        getAllProducts.setProductVariants(listProductVariants);
        getAllProducts.setProductNotVariants(listProductNotVariants);

        List<ComboProductDTO> listCombo = new ArrayList<>();

        for (ComboProduct comboProduct : comboProducts) {
            ComboProductDTO comboProductDTO = new ComboProductDTO();
            modelMapper.map(comboProduct, comboProductDTO);
            listCombo.add(comboProductDTO);
        }

        getAllProducts.setComboProducts(listCombo);

        // List<ProductVarientDTO> listVarients = new ArrayList<>();

        // for (ProductVarient productVarient : productVariants) {
        // ProductVarientDTO productVarientDTO = new ProductVarientDTO();
        // modelMapper.map(productVarient, productVarientDTO);
        // listVarients.add(productVarientDTO);
        // }
        // getAllProducts.setProductVarients(listVarients);

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
            if (saleProduct.getSaleEvent().getId() == id) {
                listSaleProductBySaleEventId.add(saleProduct);
            }
        }

        System.out.println("Độ dài của saleeventid " + listSaleProductBySaleEventId.size());
        if (listSaleProductBySaleEventId.size() > 0) {
            for (SaleProduct saleProduct : listSaleProductBySaleEventId) {
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

    // check list saleProduct
    public List<Integer> checkProduct(List<Integer> listIdProduct, Integer saleEventId) {

        List<SaleProduct> saleProductsBySaleEventId = saleProductRepository.findBySaleEventId(saleEventId).orElse(null);
        List<Integer> listDeleteSaleEvent = new ArrayList<>();
        List<Integer> listFillFinal = new ArrayList<>();
        if (saleProductsBySaleEventId.size() > 0) {
           // System.out.println("Vào đây saleProductsBySaleEventId: " + saleProductsBySaleEventId.size());
            // list không có gì truyền vào nghĩa là đã xóa hết hoặc không chọn gì hết
            // thì lúc này phải xóa hết trong db
            if (listIdProduct.size() == 0 || listIdProduct == null) {
                for (int i = 0; i < saleProductsBySaleEventId.size(); i++) {
                    if (saleProductsBySaleEventId.get(i).getProduct() != null) {
                        saleProductRepository.delete(saleProductsBySaleEventId.get(i));
                    }
                }
            } else {
                for (int i = 0; i < saleProductsBySaleEventId.size(); i++) {
                    if (saleProductsBySaleEventId.get(i).getProduct() != null) {
                        listDeleteSaleEvent.add(saleProductsBySaleEventId.get(i).getProduct().getId());
                    }
                }
                if (listDeleteSaleEvent.size() > 0) {
                    // lấy lại những id chưa có trong bảng saleProduct
                    if (listIdProduct.size() > 0) {
                        listDeleteSaleEvent.removeAll(listIdProduct);
                   //     System.out.println("Vào đây listDeleteSaleEvent: " + listDeleteSaleEvent.size());
                    }

                    for (int i = 0; i < listDeleteSaleEvent.size(); i++) {
                        SaleProduct findSale = saleProductRepository.findByProductId(listDeleteSaleEvent.get(i))
                                .orElse(null);
                        if (findSale != null) {
                        //    System.out.println("vao day de xxoa");
                        // giữ lại những id đã được xóa
                            listFillFinal.add(listDeleteSaleEvent.get(i));
                            saleProductRepository.delete(findSale);
                        }
                    }
                }
            }
        }

        if (listFillFinal.size() > 0) {
            // xóa thêm một lần nữa 
            // xóa id product đã xóa trên bản
            listDeleteSaleEvent.removeAll(listFillFinal);
        }
        List<SaleProduct> saleProducts = saleProductRepository.findAll();
        List<Integer> listProductFill = new ArrayList<>();
        if (saleProducts.size() > 0) {
            for (int i = 0; i < saleProducts.size(); i++) {
                if (saleProducts.get(i).getProduct() != null) {
                    listProductFill.add(saleProducts.get(i).getProduct().getId());
                }
            }
        }
        // xóa thêm lần nữa những product đã có trên bản ở những sự kiện sale khác
        listIdProduct.removeAll(listProductFill);

        return listIdProduct;
    }

    // check list saleComboProduct
    public List<Integer> checkComboProduct(List<Integer> listIdComboProduct, Integer saleEventId) {

       
        List<SaleProduct> saleProductsBySaleEventId = saleProductRepository.findBySaleEventId(saleEventId).orElse(null);
        List<Integer> listDeleteSaleEvent = new ArrayList<>();
        List<Integer> listFillFinal = new ArrayList<>();
        if (saleProductsBySaleEventId.size() > 0) {
           // System.out.println("Vào đây saleProductsBySaleEventId: " + saleProductsBySaleEventId.size());

            if (listIdComboProduct.size() == 0 || listIdComboProduct == null) {
                for (int i = 0; i < saleProductsBySaleEventId.size(); i++) {
                    if (saleProductsBySaleEventId.get(i).getComboProduct() != null) {
                        saleProductRepository.delete(saleProductsBySaleEventId.get(i));
                    }
                }
            } else {
                for (int i = 0; i < saleProductsBySaleEventId.size(); i++) {
                    if (saleProductsBySaleEventId.get(i).getComboProduct() != null) {
                        listDeleteSaleEvent.add(saleProductsBySaleEventId.get(i).getComboProduct().getId());
                    }
                }
                if (listDeleteSaleEvent.size() > 0) {
                    // lấy lại những id chưa có trong bảng saleProduct
                    if (listIdComboProduct.size() > 0) {
                        listDeleteSaleEvent.removeAll(listIdComboProduct);
                   //     System.out.println("Vào đây listDeleteSaleEvent: " + listDeleteSaleEvent.size());
                    }

                    for (int i = 0; i < listDeleteSaleEvent.size(); i++) {
                        SaleProduct findSale = saleProductRepository.findByComboProductId(listDeleteSaleEvent.get(i))
                                .orElse(null);
                        if (findSale != null) {
                    //        System.out.println("vao day de xxoa");
                            listFillFinal.add(listDeleteSaleEvent.get(i));
                            saleProductRepository.delete(findSale);
                        }
                    }
                }
            }
        }
          if (listFillFinal.size() > 0) {
            // xóa id product đã xóa trên bản
            listDeleteSaleEvent.removeAll(listFillFinal);
        }

        List<SaleProduct> saleProducts = saleProductRepository.findAll();
         List<Integer> listComboProductFill = new ArrayList<>();
        if (saleProducts.size() > 0) {
            for (int i = 0; i < saleProducts.size(); i++) {
                if (saleProducts.get(i).getComboProduct() != null) {
                    listComboProductFill.add(saleProducts.get(i).getComboProduct().getId());
                }
            }
        }
        listIdComboProduct.removeAll(listComboProductFill);
        return listIdComboProduct;
    }

    // check list saleProductVariant
    public List<Integer> checkProductVariant(List<Integer> listIdProductVariant, Integer saleEventId) {

      
        List<SaleProduct> saleProductsBySaleEventId =
        saleProductRepository.findBySaleEventId(saleEventId).orElse(null);
        List<Integer> listDeleteSaleEvent = new ArrayList<>();
        List<Integer> listFillFinal = new ArrayList<>();
        if (saleProductsBySaleEventId.size() > 0) {
           //System.out.println("Vào đây saleProductsBySaleEventId: " + saleProductsBySaleEventId.size());
            // list không có gì truyền vào nghĩa là đã xóa hết hoặc không chọn gì hết
            // thì lúc này phải xóa hết trong db
            if (listIdProductVariant.size() == 0 || listIdProductVariant == null) {
                for (int i = 0; i < saleProductsBySaleEventId.size(); i++) {
                    if (saleProductsBySaleEventId.get(i).getProductVarient() != null) {
                        saleProductRepository.delete(saleProductsBySaleEventId.get(i));
                    }
                }
            } else {
                for (int i = 0; i < saleProductsBySaleEventId.size(); i++) {
                    if (saleProductsBySaleEventId.get(i).getProductVarient() != null) {
                        listDeleteSaleEvent.add(saleProductsBySaleEventId.get(i).getProductVarient().getId());
                    }
                }
                if (listDeleteSaleEvent.size() > 0) {
                    // lấy lại những id chưa có trong bảng saleProduct
                    if (listIdProductVariant.size() > 0) {
                        listDeleteSaleEvent.removeAll(listIdProductVariant);
                   //    System.out.println("Vào đây listDeleteSaleEvent: " + listDeleteSaleEvent.size());
                    }

                    for (int i = 0; i < listDeleteSaleEvent.size(); i++) {
                        SaleProduct findSale = saleProductRepository.findByProductVarientId(listDeleteSaleEvent.get(i))
                                .orElse(null);
                        if (findSale != null) {
                      //     System.out.println("vao day de xxoa");
                        // giữ lại những id đã được xóa
                            listFillFinal.add(listDeleteSaleEvent.get(i));
                            saleProductRepository.delete(findSale);
                        }
                    }
                }
            }
        }

        if (listFillFinal.size() > 0) {
            // xóa thêm một lần nữa 
            // xóa id product đã xóa trên bản
            listDeleteSaleEvent.removeAll(listFillFinal);
        }

          List<Integer> listComboProductVariantFill = new ArrayList<>();
        List<SaleProduct> saleProducts = saleProductRepository.findAll();

        if (saleProducts.size() > 0) {
            for (int i = 0; i < saleProducts.size(); i++) {
                if (saleProducts.get(i).getProductVarient() != null) {
                    listComboProductVariantFill.add(saleProducts.get(i).getProductVarient().getId());
                }
            }
        }
        listIdProductVariant.removeAll(listComboProductVariantFill);
        return listIdProductVariant;
    }

}
