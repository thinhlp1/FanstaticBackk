package com.fanstatic.service.order;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tomcat.util.http.ResponseUtil;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.order.request.ExtraPortionOrderRequestDTO;
import com.fanstatic.dto.model.order.request.OrderItemRequestDTO;
import com.fanstatic.dto.model.order.request.OrderRequestDTO;
import com.fanstatic.model.ExtraPortion;
import com.fanstatic.model.Order;
import com.fanstatic.model.OrderExtraPortion;
import com.fanstatic.model.OrderItem;
import com.fanstatic.model.OrderItemOption;
import com.fanstatic.model.OrderTable;
import com.fanstatic.model.OrderType;
import com.fanstatic.model.Product;
import com.fanstatic.model.ProductCategory;
import com.fanstatic.model.ProductVarient;
import com.fanstatic.model.Table;
import com.fanstatic.repository.ExtraPortionRepository;
import com.fanstatic.repository.OptionRepository;
import com.fanstatic.repository.OrderExtraPortionRepository;
import com.fanstatic.repository.OrderItemRepository;
import com.fanstatic.repository.OrderRepository;
import com.fanstatic.repository.OrderTableRepository;
import com.fanstatic.repository.OrderTypeRepository;
import com.fanstatic.repository.ProductRepository;
import com.fanstatic.repository.ProductVarientRepository;
import com.fanstatic.repository.StatusRepository;
import com.fanstatic.repository.TableRepository;
import com.fanstatic.repository.UserRepository;
import com.fanstatic.service.model.TableService;
import com.fanstatic.service.system.FileService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final SystemService systemService;
    private final FileService fileService;

    private final TableService tableService;
    private final ExtraPortionRepository extraPortionRepository;
    private final OrderItemRepository orderItemRepository;
    private final OptionRepository optionRepository;
    private final StatusRepository statusRepository;
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderTypeRepository orderTypeRepository;
    private final OrderExtraPortionRepository orderExtraPortionRepository;

    private final ProductRepository productRepository;
    private final ProductVarientRepository productVarientRepository;

    private final PlatformTransactionManager transactionManager;

    public ResponseDTO checkTableOrdered(int tableId) {

        return ResponseUtils.success(200, "OKE", null);
    }

    public ResponseDTO create(OrderRequestDTO orderRequestDTO) {

        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        Order order = modelMapper.map(orderRequestDTO, Order.class);

        OrderType orderType = orderTypeRepository.findById(orderRequestDTO.getOrderType()).orElse(null);
        if (orderType == null) {
            return ResponseUtils.fail(500, "Loại order không tồn tại", null);

        }

        order.setCreateAt(new Date());
        order.setCreateBy(systemService.getUserLogin());
        order.setCustomer(systemService.getUserLogin());
        order.setStatus(statusRepository.findById("CONFIRMING").get());
        order.setTotal(total(orderRequestDTO));
        order.setOrderType(orderType);

        Order orderSaved = orderRepository.saveAndFlush(order);

        if (orderSaved != null) {
            // create order item
            ResponseDTO orderItemSaved = createOrderItem(orderRequestDTO.getOrderItems(), orderSaved);
            if (!orderItemSaved.isSuccess()) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(orderItemSaved.getStatusCode(), orderItemSaved.getMessage(), null);

            }

            ResponseDTO orderTableSaved = createOrderTable(List.of(orderRequestDTO.getTableId()), orderSaved);
            if (!orderTableSaved.isSuccess()) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(orderTableSaved.getStatusCode(), orderTableSaved.getMessage(), null);

            }

            ResponseDTO orderExtraSaved = createExtraPortion(orderRequestDTO.getExtraPortions(), orderSaved);
            if (!orderExtraSaved.isSuccess()) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(orderExtraSaved.getStatusCode(), orderExtraSaved.getMessage(), null);

            }

        } else {
            transactionManager.rollback(transactionStatus);
            return ResponseUtils.fail(500, "Tạo order không thành công", null);
        }

        transactionManager.commit(transactionStatus);

        return ResponseUtils.success(200, "OKE", null);

    }

    public ResponseDTO createExtraPortion(List<ExtraPortionOrderRequestDTO> extraPortionOrderRequestDTOs, Order order) {
        List<OrderExtraPortion> orderExtraPortions = new ArrayList<>();
        for (ExtraPortionOrderRequestDTO extraPortionOrderRequestDTO : extraPortionOrderRequestDTOs) {

            ExtraPortion extraPortion = extraPortionRepository
                    .findByExtraPortionIdAndActiveIsTrue(extraPortionOrderRequestDTO.getExtraPortionId()).orElse(null);
            if (extraPortion == null) {
                return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
            }
            OrderExtraPortion orderExtraPortion = modelMapper.map(extraPortionOrderRequestDTO, OrderExtraPortion.class);

            orderExtraPortion.setCreateAt(new Date());
            orderExtraPortion.setCreateBy(systemService.getUserLogin());
            orderExtraPortion.setExtraPortion(extraPortion);
            orderExtraPortion.setOrder(order);
            orderExtraPortions.add(orderExtraPortion);
        }
        try {
            List<OrderExtraPortion> listSaved = orderExtraPortionRepository.saveAll(orderExtraPortions);
            if (listSaved != null) {
                return ResponseUtils.success(200, "Tạo thành công!", null);
            }
            return ResponseUtils.fail(500, "Tạo order không thành công", null);
        } catch (Exception e) {
            return ResponseUtils.fail(500, "Tạo order không thành công", null);
        }
    }

    public ResponseDTO createOrderItem(List<OrderItemRequestDTO> orderItemRequestDTOs, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequestDTO orderItemDTO : orderItemRequestDTOs) {

            OrderItem orderItem = modelMapper.map(orderItemDTO, OrderItem.class);

            Product product = productRepository.findByIdAndActiveIsTrue(orderItemDTO.getProductId()).orElse(null);
            if (product == null) {
                return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
            }

            if (orderItemDTO.getProductVariantId() != null) {
                ProductVarient productVarient = productVarientRepository
                        .findByIdAndActiveIsTrue(orderItemDTO.getProductVariantId()).orElse(null);

                if (productVarient == null) {
                    return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
                }

                orderItem.setProductVarient(productVarient);
            }

            orderItem.setProduct(product);
            System.out.println("PRODUCT: " + product.getName());
            List<OrderItemOption> orderItemOptions = new ArrayList<>();
            for (Integer optionId : orderItemDTO.getOptionsId()) {
                System.out.println(optionId);
                OrderItemOption orderItemOption = new OrderItemOption();
                orderItemOption.setOption(optionRepository.findById(optionId).get());
                orderItemOption.setOrderItem(orderItem);

                orderItemOption.setCreateAt(new Date());
                orderItemOption.setCreateBy(systemService.getUserLogin());
                orderItemOptions.add(orderItemOption);
            }

            orderItem.setCreateAt(new Date());
            orderItem.setCreateBy(systemService.getUserLogin());
            orderItem.setStatus(statusRepository.findById("ITEM_PROCESSING").get());
            orderItem.setOrderItemOptions(orderItemOptions);
            orderItem.setOrder(order);
            // orderItemRepository.save(orderItem);
            orderItems.add(orderItem);

        }
        try {
            List<OrderItem> listSaved = orderItemRepository.saveAll(orderItems);
            if (listSaved != null) {
                return ResponseUtils.success(200, "Tạo thành công!", null);
            }
            return ResponseUtils.fail(500, "Tạo order không thành công", null);
        } catch (Exception e) {
            return ResponseUtils.fail(500, "Tạo order không thành công", null);
        }
    }

    public ResponseDTO createOrderTable(List<Integer> tablesId, Order order) {
        List<OrderTable> orderTables = new ArrayList<>();

        for (Integer tableId : tablesId) {

            Table table = tableRepository.findById(tableId).orElse(null);

            if (table == null) {
                return ResponseUtils.fail(500, "Bàn không hợp lệ", null);
            }

            OrderTable orderTable = new OrderTable();
            orderTable.setCreateAt(new Date());
            orderTable.setCreateBy(systemService.getUserLogin());
            orderTable.setTable(table);
            orderTable.setOrder(order);

            orderTables.add(orderTable);
        }

        try {
            List<OrderTable> listSaved = orderTableRepository.saveAll(orderTables);
            if (listSaved != null) {
                return ResponseUtils.success(200, "Tạo thành công!", null);
            }
            return ResponseUtils.fail(500, "Tạo order không thành công", null);
        } catch (Exception e) {
            return ResponseUtils.fail(500, "Tạo order không thành công", null);
        }

    }

    public Long total(OrderRequestDTO orderRequestDTO) {
        long orderTotal = 0;

        // Lặp qua danh sách các mục đơn hàng
        if (orderRequestDTO.getOrderItems() != null) {
            for (OrderItemRequestDTO orderItemDTO : orderRequestDTO.getOrderItems()) {
                // Tính tổng tiền cho từng mục đơn hàng và cộng vào tổng tiền của đơn hàng
                long itemTotal = calculateItemTotal(orderItemDTO);
                orderTotal += itemTotal;
            }
        }

        if (orderRequestDTO.getExtraPortions() != null) {
            for (ExtraPortionOrderRequestDTO extraPortionOrderRequestDTO : orderRequestDTO.getExtraPortions()) {
                // Tính tổng tiền cho từng mục đơn hàng và cộng vào tổng tiền của đơn hàng
                long itemTotal = calculateExtraPortionTotal(extraPortionOrderRequestDTO);
                orderTotal += itemTotal;
            }
        }

        return orderTotal;
    }

    private long calculateItemTotal(OrderItemRequestDTO orderItemDTO) {
        long itemTotal = 0;
        // Lấy thông tin sản phẩm và biến thể sản phẩm
        Product product = productRepository.findByIdAndActiveIsTrue(orderItemDTO.getProductId()).orElse(null);

        if (product != null) {
            long productPrice = product.getPrice();

            if (orderItemDTO.getProductVariantId() != null) {
                ProductVarient productVariant = productVarientRepository
                        .findByIdAndActiveIsTrue(orderItemDTO.getProductVariantId())
                        .orElse(null);
                if (productVariant != null) {
                    productPrice = productVariant.getPrice();
                }
            }

            long itemPrice = productPrice;
            itemTotal = itemPrice * orderItemDTO.getQuantity();
        }

        return itemTotal;
    }

    private long calculateExtraPortionTotal(ExtraPortionOrderRequestDTO extraPortionOrderRequestDTO) {
        long itemTotal = 0;
        // Lấy thông tin sản phẩm và biến thể sản phẩm
        ExtraPortion extraPortion = extraPortionRepository.findById(extraPortionOrderRequestDTO.getId()).orElse(null);

        if (extraPortion != null) {

            long itemPrice = extraPortion.getPrice();
            itemTotal = itemPrice * extraPortion.getPrice();

        }

        return itemTotal;
    }

    public ResponseDTO detail(Integer id) {
        return null;
    }

}
