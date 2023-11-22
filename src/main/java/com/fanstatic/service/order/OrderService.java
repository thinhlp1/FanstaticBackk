package com.fanstatic.service.order;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.ResponseUtil;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.bill.BillDTO;
import com.fanstatic.dto.model.category.CategoryCompactDTO;
import com.fanstatic.dto.model.category.CategoryDTO;
import com.fanstatic.dto.model.customer.CustomerDTO;
import com.fanstatic.dto.model.extraportion.ExtraPortionDTO;
import com.fanstatic.dto.model.option.OptionDTO;
import com.fanstatic.dto.model.order.CreateOrderResponseDTO;
import com.fanstatic.dto.model.order.OrderDTO;
import com.fanstatic.dto.model.order.OrderExtraPortionDTO;
import com.fanstatic.dto.model.order.OrderItemDTO;
import com.fanstatic.dto.model.order.OrderItemSessionDTO;
import com.fanstatic.dto.model.order.checkout.CheckVoucherRequestDTO;
import com.fanstatic.dto.model.order.checkout.CheckoutRequestDTO;
import com.fanstatic.dto.model.order.checkout.ConfirmCheckoutRequestDTO;
import com.fanstatic.dto.model.order.edit.OrderExtraPortionRemoveDTO;
import com.fanstatic.dto.model.order.edit.OrderExtraPortionUpdateDTO;
import com.fanstatic.dto.model.order.edit.OrderItemRemoveDTO;
import com.fanstatic.dto.model.order.edit.OrderItemUpdateDTO;
import com.fanstatic.dto.model.order.checkout.ApplyVoucherDTO;
import com.fanstatic.dto.model.order.request.CancalOrderRequestDTO;
import com.fanstatic.dto.model.order.request.ExtraPortionOrderRequestDTO;
import com.fanstatic.dto.model.order.request.OrderItemRequestDTO;
import com.fanstatic.dto.model.order.request.OrderRequestDTO;
import com.fanstatic.dto.model.order.request.SwitchOrderRequestDTO;
import com.fanstatic.dto.model.payment.PaymentMethodDTO;
import com.fanstatic.dto.model.saleevent.SaleEventDTO;
import com.fanstatic.dto.model.status.StatusDTO;
import com.fanstatic.dto.model.table.TableDTO;
import com.fanstatic.dto.model.user.UserCompactDTO;
import com.fanstatic.dto.model.voucher.VoucherDTO;
import com.fanstatic.model.Bill;
import com.fanstatic.model.CancelReason;
import com.fanstatic.model.Category;
import com.fanstatic.model.ComboProduct;
import com.fanstatic.model.ExtraPortion;
import com.fanstatic.model.File;
import com.fanstatic.model.Option;
import com.fanstatic.model.Order;
import com.fanstatic.model.OrderExtraPortion;
import com.fanstatic.model.OrderItem;
import com.fanstatic.model.OrderItemOption;
import com.fanstatic.model.OrderTable;
import com.fanstatic.model.OrderType;
import com.fanstatic.model.PaymentMethod;
import com.fanstatic.model.Product;
import com.fanstatic.model.ProductCategory;
import com.fanstatic.model.ProductVarient;
import com.fanstatic.model.SaleEvent;
import com.fanstatic.model.Status;
import com.fanstatic.model.Table;
import com.fanstatic.model.User;
import com.fanstatic.model.Voucher;
import com.fanstatic.repository.BillRepository;
import com.fanstatic.repository.CancelReasonRepository;
import com.fanstatic.repository.CategoryRepository;
import com.fanstatic.repository.ComboProductRepository;
import com.fanstatic.repository.ExtraPortionRepository;
import com.fanstatic.repository.OptionRepository;
import com.fanstatic.repository.OrderExtraPortionRepository;
import com.fanstatic.repository.OrderItemOptionRepository;
import com.fanstatic.repository.OrderItemRepository;
import com.fanstatic.repository.OrderRepository;
import com.fanstatic.repository.OrderTableRepository;
import com.fanstatic.repository.OrderTypeRepository;
import com.fanstatic.repository.PaymentMethodRepository;
import com.fanstatic.repository.ProductCategoryRepository;
import com.fanstatic.repository.ProductRepository;
import com.fanstatic.repository.ProductVarientRepository;
import com.fanstatic.repository.SaleEventRepository;
import com.fanstatic.repository.SaleProductRepository;
import com.fanstatic.repository.StatusRepository;
import com.fanstatic.repository.TableRepository;
import com.fanstatic.repository.UserRepository;
import com.fanstatic.repository.VoucherRepository;
import com.fanstatic.service.model.RolePermissionService;
import com.fanstatic.service.model.TableService;
import com.fanstatic.service.payos.PayOSService;
import com.fanstatic.service.system.FileService;
import com.fanstatic.service.system.PushNotificationService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final SystemService systemService;
    private final FileService fileService;
    private final PayOSService payOSService;
    private final PushNotificationService pushNotificationService;
    private final RolePermissionService rolePermissionService;

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
    private final ComboProductRepository comboProductRepository;
    private final CancelReasonRepository cancelReasonRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final VoucherRepository voucherRepository;
    private final BillRepository billRepository;
    private final SaleProductRepository saleProductRepository;
    private final SaleEventRepository saleEventRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final OrderItemOptionRepository orderItemOptionRepository;

    private final ProductRepository productRepository;
    private final ProductVarientRepository productVarientRepository;

    private final PlatformTransactionManager transactionManager;

    @PersistenceContext
    private EntityManager entityManager;

    private final int MIN_PEOPLE = 1;
    private final int MAX_PEOPLE = 20;
    private final int MIN_QUANTITY = 1;
    private final int MAX_QUANTITY = 99;

    public ResponseDTO checkTableOrdered(int tableId) {
        Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // Tính thời điểm 24 giờ

        Table table = tableRepository.findByIdAndActiveIsTrue(tableId).orElse(null);
        if (table == null) {
            return ResponseUtils.fail(404, "Bàn không tồn tại", null);

        }

        int isOpcciped = orderTableRepository.checkTalbeOccupied(tableId, twentyFourHoursAgo);
        if (isOpcciped > 0) {
            return ResponseUtils.fail(201, "Bàn đã được đặt", null);

        }
        return ResponseUtils.success(200, "Bàn trống ", null);
    }

    public ResponseDTO checkUserHasOrder() {
        Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // Tính thời điểm 24 giờ

        User customer = systemService.getUserLogin();
        List<Order> orders = orderRepository.findOrderUser(customer.getId(), twentyFourHoursAgo).orNull();

        if (orders == null) {
            return ResponseUtils.success(202, "Không có order", null);

        }
        List<ResponseDataDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            // if
            // ((!order.getStatus().getId().equals(ApplicationConst.OrderStatus.CONFIRMING)
            // && !order.getStatus().getId().equals(ApplicationConst.OrderStatus.CANCEL)
            // )
            // && order.getRootOrder() != null) {
            // continue;
            // }

            if (order.getRootOrder() != null) {
                if ((order.getStatus().getId().equals(ApplicationConst.OrderStatus.CONFIRMING)
                        || order.getStatus().getId().equals(ApplicationConst.OrderStatus.CANCEL))) {

                    OrderDTO orderDTO = convertOrderToDTO(order);
                    orderDTOs.add(orderDTO);
                }

            } else {

                OrderDTO orderDTO = convertOrderToDTO(order);
                orderDTOs.add(orderDTO);
            }

        }

        ResponseListDataDTO ordersDTO = new ResponseListDataDTO();
        ordersDTO.setDatas(orderDTOs);
        ordersDTO.setNameList("Danh sách order của khách hàng");

        return ResponseUtils.success(200, "Khách hàng có order ", ordersDTO);
    }

    public ResponseDTO create(OrderRequestDTO orderRequestDTO) {

        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        boolean isOpcciped = checkTalbeOccupied(orderRequestDTO.getTableId());
        if (isOpcciped) {
            return ResponseUtils.fail(201, "Bàn đã được đặt ", null);

        }

        Order order = modelMapper.map(orderRequestDTO, Order.class);

        OrderType orderType = orderTypeRepository.findById(orderRequestDTO.getOrderType()).orElse(null);
        if (orderType == null) {
            return ResponseUtils.fail(500, "Loại order không tồn tại", null);

        }

        order.setCreateAt(new Date());
        order.setCreateBy(systemService.getUserLogin());

        // TODO Nếu người dùng đang đăng nhập là nhân viên thì ko cần set customer Id
        // TODO Nếu người dùng có tài khoản nhưng ko đem điện thoại thì có thể nhập số
        // điện thoại trên điện thoại nhân viên rồi gán cho order
        // TODO Nếu người tạo order là nhân viên thì trạng thái sẽ chuyển sang đang

        if (isStaffCreateOrderAndConfirm()) {

            if (orderRequestDTO.getCustomerId() != null) {
                User user = userRepository.findByIdAndActiveIsTrue(orderRequestDTO.getCustomerId()).orElse(null);
                if (user == null) {
                    return ResponseUtils.fail(500, "Customer không tồn tại", null);
                }
                order.setCustomer(user);
                order.setEmployeeConfirmed(systemService.getUserLogin());
            }

        } else {
            order.setCustomer(systemService.getUserLogin());

        }
        order.setStatus(statusRepository.findById(ApplicationConst.OrderStatus.CONFIRMING).get());
        order.setTotal(total(orderRequestDTO));
        order.setOrderType(orderType);

        Order orderSaved = orderRepository.saveAndFlush(order);

        if (orderSaved != null) {
            // create order item
            List<OrderItemRequestDTO> orderItemRequestDTOs = orderRequestDTO.getOrderItems();
            if (orderItemRequestDTOs != null && !orderItemRequestDTOs.isEmpty()) {
                ResponseDTO orderItemSaved = createOrderItem(orderRequestDTO.getOrderItems(), orderSaved);
                if (!orderItemSaved.isSuccess()) {
                    transactionManager.rollback(transactionStatus);
                    return ResponseUtils.fail(orderItemSaved.getStatusCode(), orderItemSaved.getMessage(), null);

                }
            }

            ResponseDTO orderTableSaved = createOrderTable(List.of(orderRequestDTO.getTableId()), orderSaved);
            if (!orderTableSaved.isSuccess()) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(orderTableSaved.getStatusCode(), orderTableSaved.getMessage(), null);

            }

            List<ExtraPortionOrderRequestDTO> extraPortionDTOs = orderRequestDTO.getExtraPortions();
            if (extraPortionDTOs != null && !extraPortionDTOs.isEmpty()) {
                ResponseDTO orderExtraSaved = createExtraPortion(orderRequestDTO.getExtraPortions(), orderSaved);
                if (!orderExtraSaved.isSuccess()) {
                    transactionManager.rollback(transactionStatus);
                    return ResponseUtils.fail(orderExtraSaved.getStatusCode(), orderExtraSaved.getMessage(), null);

                }
            }

        } else {
            transactionManager.rollback(transactionStatus);
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        }

        transactionManager.commit(transactionStatus);

        systemService.writeSystemLog(orderSaved.getOrderId(), "", null);

        if (isStaffCreateOrderAndConfirm()) {
            confirm(orderSaved.getOrderId());
        } else {
            pushNotificationOrder(order.getCustomer().getId(), orderSaved.getOrderId(),
                    "Order của bạn đã được gửi cho nhân viên");

        }

        return ResponseUtils.success(200, "Tạo order thành công", null);

    }

    public ResponseDTO confirm(int id) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return ResponseUtils.fail(404, "Order không tồn tại", null);

        }

        if (!order.getStatus().getId().equals(ApplicationConst.OrderStatus.CONFIRMING)) {
            return ResponseUtils.fail(500, "Order không thể xác nhận", null);

        }

        if (order.getRootOrder() != null) {
            // combine order
            Order rootOrder = orderRepository.findById(order.getRootOrder()).get();
            String rNote = order.getNote();
            String newNote = rootOrder.getNote();
            newNote = newNote + " - " + rNote;

            Long total = total(rootOrder) + total(order);

            rootOrder.setNote(newNote);
            rootOrder.setTotal(total);
            rootOrder.setUpdateAt(new Date());
            rootOrder.setUpdateBy(systemService.getUserLogin());
            rootOrder.setEmployeeConfirmed(systemService.getUserLogin());

            ResponseDTO syncOrderItem = syncOrderItem(order.getOrderItems(), rootOrder);
            if (!syncOrderItem.isSuccess()) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(syncOrderItem.getStatusCode(),
                        syncOrderItem.getMessage(), null);

            }

            ResponseDTO syncExtraPortion = syncExtraPortion(order.getOrderExtraPortions(), rootOrder);
            if (!syncExtraPortion.isSuccess()) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(syncExtraPortion.getStatusCode(),
                        syncExtraPortion.getMessage(), null);

            }

            ResponseDTO syncOrderTable = syncOrderTable(order.getOrderTables(), rootOrder);
            if (!syncOrderTable.isSuccess()) {
                transactionManager.rollback(transactionStatus);
                return ResponseUtils.fail(syncOrderTable.getStatusCode(),
                        syncOrderTable.getMessage(), null);

            }

        }

        if (order.getStatus().getId().equals(ApplicationConst.OrderStatus.CONFIRMING)) {
            order.setStatus(statusRepository.findById(ApplicationConst.OrderStatus.PROCESSING).get());
            order.setUpdateAt(new Date());
            order.setUpdateBy(systemService.getUserLogin());
            order.setEmployeeConfirmed(systemService.getUserLogin());

            orderRepository.save(order);

            for (OrderItem orderItem : order.getOrderItems()) {
                orderItem.setStatus(statusRepository.findById(ApplicationConst.OrderStatus.ITEM_PROCESSING).get());
                orderItemRepository.save(orderItem);
            }

            systemService.writeSystemLog(order.getOrderId(), "", null);

            transactionManager.commit(transactionStatus);

            pushNotificationOrder(order.getCustomer().getId(), order.getOrderId(),
                    "Order của bạn đã được nhân viên tiếp nhận");

            return ResponseUtils.success(200, "Duyệt order thành công", null);
        } else {
            return ResponseUtils.fail(500, "Trạng thái order không hợp lệ", null);

        }

    }

    public ResponseDTO reOrder(OrderRequestDTO orderRequestDTO) {

        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        Order rootOrder = orderRepository.findById(orderRequestDTO.getId()).orElse(null);
        if (rootOrder == null) {
            return ResponseUtils.fail(404, "Order không tồn tại", null);

        }

        if (rootOrder.getStatus().getId().equals(ApplicationConst.OrderStatus.CONFIRMING)) {
            return ResponseUtils.fail(404, "Order trước của bạn đang chờ duyệt", null);

        }

        boolean isOpcciped = checkTalbeOccupied(orderRequestDTO.getTableId(), orderRequestDTO.getId());
        if (isOpcciped) {
            return ResponseUtils.fail(201, "Bàn đã được đặt ", null);

        }

        Order order = modelMapper.map(orderRequestDTO, Order.class);

        OrderType orderType = orderTypeRepository.findById(orderRequestDTO.getOrderType()).orElse(null);
        if (orderType == null) {
            return ResponseUtils.fail(500, "Loại order không tồn tại", null);

        }

        order.setCreateAt(new Date());
        order.setCreateBy(systemService.getUserLogin());
        order.setCustomer(systemService.getUserLogin());
        order.setStatus(statusRepository.findById(ApplicationConst.OrderStatus.CONFIRMING).get());
        order.setTotal(total(orderRequestDTO));
        order.setOrderType(orderType);
        order.setRootOrder(orderRequestDTO.getId());

        Order orderSaved = orderRepository.saveAndFlush(order);

        if (orderSaved != null) {
            // create order item
            List<OrderItemRequestDTO> orderItemRequestDTOs = orderRequestDTO.getOrderItems();
            if (orderItemRequestDTOs != null && !orderItemRequestDTOs.isEmpty()) {
                ResponseDTO orderItemSaved = createOrderItem(orderRequestDTO.getOrderItems(), orderSaved);
                if (!orderItemSaved.isSuccess()) {
                    transactionManager.rollback(transactionStatus);
                    return ResponseUtils.fail(orderItemSaved.getStatusCode(), orderItemSaved.getMessage(), null);

                }
            }

            // ResponseDTO orderTableSaved =
            // createOrderTable(List.of(orderRequestDTO.getTableId()), orderSaved);
            // if (!orderTableSaved.isSuccess()) {
            // transactionManager.rollback(transactionStatus);
            // return ResponseUtils.fail(orderTableSaved.getStatusCode(),
            // orderTableSaved.getMessage(), null);

            // }

            List<ExtraPortionOrderRequestDTO> extraPortionDTOs = orderRequestDTO.getExtraPortions();
            if (extraPortionDTOs != null && !extraPortionDTOs.isEmpty()) {
                ResponseDTO orderExtraSaved = createExtraPortion(orderRequestDTO.getExtraPortions(), orderSaved);
                if (!orderExtraSaved.isSuccess()) {
                    transactionManager.rollback(transactionStatus);
                    return ResponseUtils.fail(orderExtraSaved.getStatusCode(), orderExtraSaved.getMessage(), null);

                }
            }

        } else {
            transactionManager.rollback(transactionStatus);
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        }

        // systemService.writeSystemLog(orderSaved.getOrderId(), "", null);

        transactionManager.commit(transactionStatus);

        return ResponseUtils.success(200, "Tạo order thành công", null);

    }

    public ResponseDTO detail(Integer id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return ResponseUtils.fail(404, "Order không tồn tại", null);

        }

        if (!systemService.checkCustomerResource(order.getCustomer().getId())) {
            return ResponseUtils.fail(403, "Bạn không có quyền truy cập order này", null);

        }

        OrderDTO orderDTO = convertOrderToDTO(order);

        return ResponseUtils.success(200, "Chi tiết order", orderDTO);
    }

    public ResponseDTO cancel(CancalOrderRequestDTO cancalOrderRequestDTO) {
        Order order = orderRepository.findById(cancalOrderRequestDTO.getOrderId()).orElse(null);
        if (order == null) {
            return ResponseUtils.fail(404, "Order không tồn tại", null);

        }

        if (!systemService.checkCustomerResource(order.getCustomer().getId())) {
            return ResponseUtils.fail(403, "Bạn không có quyền truy cập order này", null);

        }

        if (!order.getStatus().getId().equals(ApplicationConst.OrderStatus.CONFIRMING)) {
            return ResponseUtils.fail(500, "Bạn không thể hủy order này. Vui lòng liên hệ nhân viên", null);

        }

        Status status = statusRepository.findById(ApplicationConst.OrderStatus.CANCEL).get();
        CancelReason cancelReason = cancelReasonRepository.findById(cancalOrderRequestDTO.getCancelId()).orElse(null);

        if (cancelReason != null) {
            order.setCancelReason(cancelReason);
        }

        order.setStatus(status);
        order.setDeleteAt(new Date());

        order.setDeleteBy(systemService.getUserLogin());

        orderRepository.save(order);
        systemService.writeSystemLog(order.getOrderId(), "", null);
        pushNotificationOrder(order.getCustomer().getId(), order.getOrderId(), "Order của bạn đã được hủy");

        return ResponseUtils.success(200, "Hủy thành công", null);
    }

    public ResponseDTO switchOrder(SwitchOrderRequestDTO switchOrderRequestDTO) {
        Order order = orderRepository.findById(switchOrderRequestDTO.getOrderId()).orElse(null);
        if (order == null) {
            return ResponseUtils.fail(404, "Order không tồn tại", null);
        }

        Table table = tableRepository.findByIdAndActiveIsTrue(switchOrderRequestDTO.getDestinationTable()).orElse(null);
        if (table == null) {
            return ResponseUtils.fail(404, "Bàn không tồn tại", null);

        }

        if (!systemService.checkCustomerResource(order.getCustomer().getId())) {
            return ResponseUtils.fail(403, "Bạn không có quyền truy cập order này", null);
        }

        Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // Tính thời điểm 24 giờ
        int isOpcciped = orderTableRepository.checkTalbeOccupied(switchOrderRequestDTO.getDestinationTable(),
                twentyFourHoursAgo);
        if (isOpcciped > 0) {
            return ResponseUtils.fail(201, "Bàn đã được đặt", null);

        }

        ResponseDTO switchResposne = switchOrderTable(order.getOrderTables(), table);
        if (switchResposne.isSuccess()) {
            systemService.writeSystemLog(order.getOrderId(), "", null);
            return ResponseUtils.success(200, "Đổi thành công", null);

        }
        return ResponseUtils.fail(200, "Đổi không thành công", null);

    }

    public ResponseDTO checkoutRequest(CheckoutRequestDTO checkoutRequestDTO) {

        Order order = orderRepository.findById(checkoutRequestDTO.getOrderId()).orElse(null);
        if (order == null) {
            return ResponseUtils.fail(404, "Order không tồn tại", null);

        }

        if (!systemService.checkCustomerResource(order.getCustomer().getId())) {
            return ResponseUtils.fail(403, "Bạn không có quyền truy cập order này", null);

        }

        if (!order.getStatus().getId().equals(ApplicationConst.OrderStatus.PROCESSING)) {
            return ResponseUtils.fail(500, "Order không thể thanh toán", null);

        }

        PaymentMethod paymentMethod = paymentMethodRepository.findById(checkoutRequestDTO.getPaymentMethod())
                .orElse(null);
        if (paymentMethod == null) {
            return ResponseUtils.fail(500, "Phương thức thanh toán không hợp lệ", null);

        }

        if (checkoutRequestDTO.getVoucherId() != null) {
            Voucher voucher = voucherRepository.findByIdAndActiveIsTrue(checkoutRequestDTO.getVoucherId()).orElse(null);
            if (voucher == null) {
                return ResponseUtils.fail(500, "Voucher không hợp lệ", null);

            } else {
                ResponseDTO voucherValid = checkVoucherApply(
                        new CheckVoucherRequestDTO(checkoutRequestDTO.getOrderId(), voucher.getId()));
                if (!voucherValid.isSuccess()) {
                    return ResponseUtils.fail(500, "Voucher không hợp lệ", null);

                }
                order.setVoucher(voucher);

            }
        }

        Status status = statusRepository.findById(ApplicationConst.OrderStatus.AWAIT_CHECKOUT).get();
        order.setStatus(status);

        order.setUpdateAt(new Date());
        order.setUpdateBy(systemService.getUserLogin());

        orderRepository.save(order);
        pushNotificationOrder(order.getCustomer().getId(), order.getOrderId(), "Đã gửi yêu cầu thành toán");

        return ResponseUtils.success(200, "Yêu cầu thanh toán thành công", null);

    }

    public ResponseDTO confirmCheckoutOrder(ConfirmCheckoutRequestDTO confirmCheckoutRequestDTO) {
        Order order = orderRepository.findById(confirmCheckoutRequestDTO.getOrderId()).orElse(null);
        if (order == null) {
            return ResponseUtils.fail(400, "Order không tồn tại", null);
        }

        Bill b = billRepository.findBillCheckouted(confirmCheckoutRequestDTO.getOrderId()).orNull();
        if (b != null) {
            return ResponseUtils.fail(400, "Order đã được xác nhận thanh toán", null);
        }

        if (!order.getStatus().getId().equals(ApplicationConst.OrderStatus.AWAIT_CHECKOUT)) {
            return ResponseUtils.fail(400, "Order không thể thanh toán", null);

        }

        PaymentMethod paymentMethod = paymentMethodRepository.findById(confirmCheckoutRequestDTO.getPaymentMethod())
                .orElse(null);
        if (paymentMethod == null) {
            return ResponseUtils.fail(400, "Phương thức thanh toán không hợp lệ", null);
        }

        Long receiveMoney = confirmCheckoutRequestDTO.getReceiveMoney();
        Status status = statusRepository.findById(ApplicationConst.BillStatus.AWAIT_PAYMENT).get();

        Bill bill = new Bill();
        bill.setCreateAt(new Date());
        bill.setCreateBy(systemService.getUserLogin());
        bill.setPaymentMethod(paymentMethod);
        bill.setReceiveMoney(receiveMoney);
        bill.setTotal(total(order));
        bill.setStatus(status);
        bill.setOrder(order);

        bill.setCheckoutUrl(generateOrderCheckoutUrl(order, bill.getTotal()));

        Bill billSaved = billRepository.saveAndFlush(bill);
        if (billSaved != null) {

            OrderDTO orderDTO = convertOrderToDTO(order);

            return ResponseUtils.success(200, "Xác nhận thanh toán thành công", orderDTO);
        }

        return ResponseUtils.fail(500, "Xác nhận thanh toán không thành công ở dưới", null);

    }

    public ResponseDTO handleCheckout(Integer orderCode) {
        Integer orderId = getOrderIdFromOrderCode(orderCode);

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return ResponseUtils.fail(400, "Order không tồn tại", null);
        }

        Bill bill = billRepository.findBillByOrderIdAndStatus(orderId, ApplicationConst.BillStatus.AWAIT_PAYMENT)
                .orNull();
        if (bill == null) {
            return ResponseUtils.fail(400, "Không tìm thấy yêu cầu thanh toán", null);
        }

        Status status = statusRepository.findById(ApplicationConst.BillStatus.PAID).get();
        Status orderStatus = statusRepository.findById(ApplicationConst.OrderStatus.COMPLETE).get();

        bill.setStatus(status);
        bill.setUpdateAt(new Date());
        billRepository.save(bill);

        order.setStatus(orderStatus);
        order.setUpdateAt(new Date());
        orderRepository.save(order);

        OrderDTO orderDTO = convertOrderToDTO(order);

        return ResponseUtils.success(200, "Thanh toán order đã bị hủy", orderDTO);
    }

    public ResponseDTO cacncelCheckout(Integer orderCode) {
        Integer orderId = getOrderIdFromOrderCode(orderCode);

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return ResponseUtils.fail(400, "Order không tồn tại", null);
        }

        Bill bill = billRepository.findBillByOrderIdAndStatus(orderId, ApplicationConst.BillStatus.AWAIT_PAYMENT)
                .orNull();
        if (bill == null) {
            return ResponseUtils.fail(400, "Không tìm thấy yêu cầu thanh toán", null);
        }

        Status status = statusRepository.findById(ApplicationConst.BillStatus.CANCELLED).get();
        bill.setStatus(status);
        bill.setUpdateAt(new Date());
        billRepository.save(bill);

        OrderDTO orderDTO = convertOrderToDTO(order);

        return ResponseUtils.success(200, "Thanh toán order đã bị hủy", orderDTO);
    }

    public ResponseDTO updateOrderItem(OrderItemUpdateDTO orderItemUpdateDTO) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            Order order = orderRepository.findById(orderItemUpdateDTO.getOrderId()).orElse(null);
            if (order == null) {
                return ResponseUtils.fail(404, "Order không tồn tại", null);
            }

            OrderItem orderItem = orderItemRepository.findByIdAndOrder(orderItemUpdateDTO.getId(), order).orElse(null);
            if (orderItem == null) {
                return ResponseUtils.fail(404, "Order item không tồn tại", null);
            }

            if (orderItemUpdateDTO.getQuantity() < MIN_QUANTITY || orderItemUpdateDTO.getQuantity() > MAX_PEOPLE) {
                return ResponseUtils.fail(400, "Số lượng không hợp lệ", null);

            }

            if (!isOrderUpdated(order)) {
                return ResponseUtils.fail(500, "Order không thể update", null);

            }

            orderItem.setQuantity(orderItemUpdateDTO.getQuantity());
            orderItem.setNote(orderItemUpdateDTO.getNote());
            orderItemRepository.save(orderItem);

            List<Integer> optionsId = orderItemUpdateDTO.getOptionsId();
            List<OrderItemOption> orderItemOptions = orderItem.getOrderItemOptions();

            // List chứa những OrderItemOption có trong csdl mà không có trong optionsId
            List<OrderItemOption> optionsInDbNotInList = orderItemOptions.stream()
                    .filter(orderItemOption -> !optionsId.contains(orderItemOption.getOption().getId()))
                    .collect(Collectors.toList());

            // List chứa những OrderItemOption có trong optionsId mà không có trong csdl
            List<Integer> optionIdsNotInDb = optionsId.stream()
                    .filter(optionId -> orderItemOptions.stream()
                            .noneMatch(orderItemOption -> orderItemOption.getOption().getId() == optionId))
                    .collect(Collectors.toList());

            List<OrderItemOption> orderItemOptions2 = new ArrayList<>();
            for (Integer optionId : optionIdsNotInDb) {
                Option option = optionRepository.findById(optionId).orElse(null);
                if (option != null) {
                    OrderItemOption orderItemOption = new OrderItemOption();
                    orderItemOption.setOrderItem(orderItem);
                    orderItemOption.setOption(option);
                    orderItemOptions2.add(orderItemOption);
                }

            }

            for (OrderItemOption orderItemOption : optionsInDbNotInList) {
                orderItemOptionRepository.deleteById(orderItemOption.getId());
            }

            orderItemOptionRepository.saveAllAndFlush(orderItemOptions2);
        } catch (Exception e) {

        }
        transactionManager.commit(transactionStatus);

        return ResponseUtils.success(200, "Update thành công", null);
    }

    public ResponseDTO updateOrderExtraPortion(OrderExtraPortionUpdateDTO orderExtraPortionUpdateDTO) {

        Order order = orderRepository.findById(orderExtraPortionUpdateDTO.getOrderId()).orElse(null);
        if (order == null) {
            return ResponseUtils.fail(404, "Order không tồn tại", null);
        }

        OrderExtraPortion orderExtraPortion = orderExtraPortionRepository
                .findByIdAndOrder(orderExtraPortionUpdateDTO.getId(), order)
                .orElse(null);
        if (orderExtraPortion == null) {
            return ResponseUtils.fail(404, "Order extra không tồn tại", null);
        }

        if (orderExtraPortionUpdateDTO.getQuantity() < MIN_QUANTITY
                || orderExtraPortionUpdateDTO.getQuantity() > MAX_PEOPLE) {
            return ResponseUtils.fail(400, "Số lượng không hợp lệ", null);

        }

        if (!isOrderUpdated(order)) {
            return ResponseUtils.fail(500, "Order không thể update", null);

        }

        orderExtraPortion.setQuantity(orderExtraPortionUpdateDTO.getQuantity());
        orderExtraPortionRepository.save(orderExtraPortion);

        return ResponseUtils.success(200, "Update thành công", convertOrderToDTO(order));
    }

    public ResponseDTO removeOrderItem(OrderItemRemoveDTO orderItemRemoveDTO) {

        Order order = orderRepository.findById(orderItemRemoveDTO.getOrderId()).orElse(null);
        if (order == null) {
            return ResponseUtils.fail(404, "Order không tồn tại", null);
        }

        OrderItem orderItem = orderItemRepository
                .findByIdAndOrder(orderItemRemoveDTO.getId(), order)
                .orElse(null);
        if (orderItem == null) {
            return ResponseUtils.fail(404, "Order extra không tồn tại", null);
        }

        if (!isOrderUpdated(order)) {
            return ResponseUtils.fail(500, "Order không thể update", null);

        }

        orderItemRepository.delete(orderItem);

        return ResponseUtils.success(200, "Remove thành công", convertOrderToDTO(order));
    }

    public ResponseDTO removeOrderExtraPortion(OrderExtraPortionRemoveDTO orderExtraPortionRemoveDTO) {

        Order order = orderRepository.findById(orderExtraPortionRemoveDTO.getOrderId()).orElse(null);
        if (order == null) {
            return ResponseUtils.fail(404, "Order không tồn tại", null);
        }

        OrderExtraPortion orderExtraPortion = orderExtraPortionRepository
                .findByIdAndOrder(orderExtraPortionRemoveDTO.getId(), order)
                .orElse(null);
        if (orderExtraPortion == null) {
            return ResponseUtils.fail(404, "Order extra không tồn tại", null);
        }

        if (!isOrderUpdated(order)) {
            return ResponseUtils.fail(500, "Order không thể update", null);

        }

        orderExtraPortionRepository.delete(orderExtraPortion);

        return ResponseUtils.success(200, "Remove thành công", convertOrderToDTO(order));
    }

    public ResponseDTO addNewOrderItem(OrderItemRequestDTO orderItemRequestDTO) {

        Order order = orderRepository.findById(orderItemRequestDTO.getOrderId()).orElse(null);
        if (order == null) {
            return ResponseUtils.fail(404, "Order không tồn tại", null);
        }

        if (!isOrderUpdated(order)) {
            return ResponseUtils.fail(500, "Order không thể update", null);

        }

        if (orderItemRequestDTO.getQuantity() < MIN_QUANTITY
                || orderItemRequestDTO.getQuantity() > MAX_PEOPLE) {
            return ResponseUtils.fail(400, "Số lượng không hợp lệ", null);

        }

        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            // Kiểm tra nếu product, product variant hoặc combo giống nhau
            if (isSameProductOrVariantOrCombo(orderItem, orderItemRequestDTO)) {
                // Cộng số lượng từ orderItem vào orderItem

                orderItem.setQuantity(orderItem.getQuantity() +
                        orderItemRequestDTO.getQuantity());
                orderItemRepository.save(orderItem);
                order = orderRepository.findById(orderItemRequestDTO.getOrderId()).orElse(null);

                return ResponseUtils.success(200, "Thêm mới thành công",
                        convertOrderToDTO(order));

            }

        }

        List<OrderItemRequestDTO> orderItemRequestDTOs = new ArrayList<>();
        orderItemRequestDTOs.add(orderItemRequestDTO);
        ResponseDTO isSaved = createOrderItem(orderItemRequestDTOs, order);
        if (isSaved.isSuccess()) {
            order = orderRepository.findById(orderItemRequestDTO.getOrderId()).orElse(null);
            return ResponseUtils.success(200, "Thêm mới thành công",
                    convertOrderToDTO(order));

        }
        return ResponseUtils.success(200, "Thêm mới thất bại", null);

    }

    public ResponseDTO addNewOrderExtraPortion(ExtraPortionOrderRequestDTO extraPortionOrderRequestDTO) {
        Order order = orderRepository.findById(extraPortionOrderRequestDTO.getOrderId()).orElse(null);
        if (order == null) {
            return ResponseUtils.fail(404, "Order không tồn tại", null);
        }

        ExtraPortion extraPortion = extraPortionRepository
                .findByExtraPortionIdAndActiveIsTrue(extraPortionOrderRequestDTO.getExtraPortionId()).orElse(null);
        if (extraPortion == null) {
            return ResponseUtils.fail(404, "Order extra không tồn tại", null);
        }
        if (!isOrderUpdated(order)) {
            return ResponseUtils.fail(500, "Order không thể update", null);

        }

        if (extraPortionOrderRequestDTO.getQuantity() < MIN_QUANTITY
                || extraPortionOrderRequestDTO.getQuantity() > MAX_PEOPLE) {
            return ResponseUtils.fail(400, "Số lượng không hợp lệ", null);

        }

        for (OrderExtraPortion rootExtraPortion : order.getOrderExtraPortions()) {
            if (rootExtraPortion.getExtraPortion().getExtraPortionId() == extraPortionOrderRequestDTO
                    .getExtraPortionId()) {
                rootExtraPortion
                        .setQuantity(rootExtraPortion.getQuantity() + extraPortionOrderRequestDTO.getQuantity());
                order = orderRepository.findById(extraPortionOrderRequestDTO.getOrderId()).orElse(null);
                return ResponseUtils.success(200, "Thêm mới thành công",
                        convertOrderToDTO(order));

            }

        }

        OrderExtraPortion orderExtraPortion = modelMapper.map(extraPortionOrderRequestDTO, OrderExtraPortion.class);

        orderExtraPortion.setCreateAt(new Date());
        orderExtraPortion.setCreateBy(systemService.getUserLogin());
        orderExtraPortion.setExtraPortion(extraPortion);
        orderExtraPortion.setOrder(order);
        order = orderRepository.findById(extraPortionOrderRequestDTO.getOrderId()).orElse(null);

        orderExtraPortionRepository.save(orderExtraPortion);
        order = orderRepository.findById(extraPortionOrderRequestDTO.getOrderId()).orElse(null);

        return ResponseUtils.success(200, "Thêm mới thành công",
                convertOrderToDTO(order));
    }

    public ResponseDTO checkVoucherApply(CheckVoucherRequestDTO checkVoucherRequestDTO) {

        Voucher voucher = voucherRepository.findByIdAndActiveIsTrue(checkVoucherRequestDTO.getVoucherId()).orElse(null);
        if (voucher == null) {
            return ResponseUtils.fail(500, "Voucher không hợp lệ", null);
        }

        Order order = orderRepository.findById(checkVoucherRequestDTO.getOrderId()).orElse(null);
        if (order == null) {
            return ResponseUtils.fail(404, "Order không tồn tại", null);
        }

        Date currentDate = new Date();
        Date startAt = voucher.getStartAt();
        Date endAt = voucher.getEndAt();

        Long total = total(order);

        if (currentDate.after(startAt) && currentDate.before(endAt)) {
            // Hôm nay nằm giữa startAt và endAt
            System.out.println("Voucher thời gian hợp lệ vl");

            if (voucher.getPriceCondition().intValue() <= total) {

                ApplyVoucherDTO applyVoucherDTO = new ApplyVoucherDTO();

                long discount = (long) (total * ((double) voucher.getPercent() / 100));
                System.out.println(discount);

                if (discount > voucher.getValue()) {
                    discount = voucher.getValue();
                }
                long finalTotal = total - discount;

                applyVoucherDTO.setDiscount(discount);
                applyVoucherDTO.setFinalTotal(finalTotal);
                applyVoucherDTO.setTotal(total);

                return ResponseUtils.success(200, "Áp dụng thành công", applyVoucherDTO);
            } else {
                return ResponseUtils.fail(400, "Chưa đủ điều kiện áp dụng", null);
            }

        } else if (currentDate.after(endAt)) {
            // Hôm nay không nằm giữa startAt và endAt
            System.out.println("Voucher đã hết hạn");
            return ResponseUtils.fail(400, "Voucher đã hết hạn", null);

        } else if (currentDate.before(startAt)) {
            // Hôm nay không nằm giữa startAt và endAt
            System.out.println("Voucher chưa bắt đầu áp dụng");
            return ResponseUtils.fail(400, "Voucher chưa áp dụng", null);
        }
        return ResponseUtils.fail(400, "Chưa đủ điều kiện áp dụng", null);

    }

    public ResponseDTO getListOrderAwaitCheckout() {
        Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // Tính thời điểm 24 giờ
        List<Order> orders = orderRepository.findOrdersCreatedAwaitCheckout(twentyFourHoursAgo);
        List<ResponseDataDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            OrderDTO orderDTO = convertOrderToDTO(order);
            orderDTOs.add(orderDTO);
        }

        ResponseListDataDTO responseListDataDTO = new ResponseListDataDTO();
        responseListDataDTO.setDatas(orderDTOs);
        return ResponseUtils.success(200, "Danh sách order chờ thanh toán", responseListDataDTO);
    }

    public ResponseDTO getListOrder() {
        Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // Tính thời điểm 24 giờ
        List<Order> orders = orderRepository.findOrdersCreated(twentyFourHoursAgo);
        List<ResponseDataDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            OrderDTO orderDTO = convertOrderToDTO(order);
            orderDTOs.add(orderDTO);
        }

        ResponseListDataDTO responseListDataDTO = new ResponseListDataDTO();
        responseListDataDTO.setDatas(orderDTOs);
        return ResponseUtils.success(200, "Danh sách order", responseListDataDTO);

    }

    private OrderDTO convertOrderToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getOrderId());
        orderDTO.setNote(order.getNote());
        orderDTO.setOrderType(order.getOrderType().getName());
        orderDTO.setPeople(order.getPeople());
        orderDTO.setTotal(total(order));

        orderDTO.setCustomer(modelMapper.map(order.getCustomer(),
                CustomerDTO.class));

        if (order.getOrderExtraPortions() != null) {
            orderDTO.setExtraPortions(getOrderExtraPortions(order.getOrderExtraPortions()));

        }
        // if ()

        if (order.getOrderItems() != null) {
            orderDTO.setOrderItems(getOrderItems(order.getOrderItems()));

        }

        orderDTO.setTables(getTables(order.getOrderTables()));
        orderDTO.setBill(modelMapper.map(order.getBill(), BillDTO.class));
        orderDTO.setCreateAt(order.getCreateAt());
        orderDTO.setDeleteAt(order.getDeleteAt());
        orderDTO.setUpdateAt(order.getUpdateAt());
        orderDTO.setStatus(modelMapper.map(order.getStatus(), StatusDTO.class));

        if (order.getEmployeeConfirmed() != null) {
            orderDTO.setEmploeeConfirmed(modelMapper.map(order.getEmployeeConfirmed(), UserCompactDTO.class));
        }

        if (order.getDeleteBy() != null) {
            orderDTO.setEmploeeConfirmed(modelMapper.map(order.getDeleteBy(), UserCompactDTO.class));
        }

        if (order.getVoucher() != null) {
            orderDTO.setVoucher(modelMapper.map(order.getVoucher(), VoucherDTO.class));
        }

        if (order.getPaymentMethod() != null) {
            orderDTO.setPaymentMethodDTO(modelMapper.map(orderDTO, PaymentMethodDTO.class));
        }

        Bill bill = billRepository.findBillCheckouted(order.getOrderId()).orNull();
        if (bill != null) {
            bill.setStatus(null);
            orderDTO.setBill(modelMapper.map(bill, BillDTO.class));
            orderDTO.getBill().setStatus(modelMapper.map(bill.getStatus(), StatusDTO.class));
        }

        return orderDTO;
    }

    private ResponseDTO createExtraPortion(List<ExtraPortionOrderRequestDTO> extraPortionOrderRequestDTOs,
            Order order) {
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
                return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
            }
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        } catch (Exception e) {
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        }
    }

    private ResponseDTO createOrderItem(List<OrderItemRequestDTO> orderItemRequestDTOs, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequestDTO orderItemDTO : orderItemRequestDTOs) {

            OrderItem orderItem = modelMapper.map(orderItemDTO, OrderItem.class);

            if (orderItemDTO.getProductId() == null && orderItemDTO.getComboId() == null) {
                return ResponseUtils.fail(500, "Order item không hợp lệ", null);

            }

            if (orderItemDTO.getProductId() != null) {
                Product product = productRepository.findByIdAndActiveIsTrue(orderItemDTO.getProductId())
                        .orElse(null);
                if (product == null) {
                    return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
                }

                orderItem.setProduct(product);
                if (orderItemDTO.getProductVariantId() != null) {
                    ProductVarient productVarient = productVarientRepository
                            .findByIdAndActiveIsTrue(orderItemDTO.getProductVariantId()).orElse(null);

                    if (productVarient == null) {
                        return ResponseUtils.fail(500, "Sản phẩm không tồn tại", null);
                    }

                    orderItem.setProductVarient(productVarient);
                }

            }

            if (orderItemDTO.getComboId() != null) {
                ComboProduct combo = comboProductRepository.findByIdAndActiveIsTrue(orderItemDTO.getComboId())
                        .orElse(null);
                if (combo == null) {
                    return ResponseUtils.fail(500, "Combo không tồn tại", null);
                }
                orderItem.setComboProduct(combo);
            }

            List<OrderItemOption> orderItemOptions = new ArrayList<>();
            if (orderItemDTO.getOptionsId() != null) {
                for (Integer optionId : orderItemDTO.getOptionsId()) {
                    System.out.println(optionId);
                    OrderItemOption orderItemOption = new OrderItemOption();
                    orderItemOption.setOption(optionRepository.findById(optionId).get());
                    orderItemOption.setOrderItem(orderItem);

                    orderItemOption.setCreateAt(new Date());
                    orderItemOption.setCreateBy(systemService.getUserLogin());
                    orderItemOptions.add(orderItemOption);
                }
            }
            orderItem.setCreateAt(new Date());
            orderItem.setCreateBy(systemService.getUserLogin());
            // orderItem.setStatus(statusRepository.findById("ITEM_PROCESSING").get());
            orderItem.setOrder(order);
            // orderItemRepository.save(orderItem);
            orderItems.add(orderItem);

            orderItemOptionRepository.saveAll(orderItemOptions);
            orderItem.setOrderItemOptions(orderItemOptions);

        }
        try {
            List<OrderItem> listSaved = orderItemRepository.saveAll(orderItems);
            if (listSaved != null) {
                return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
            }
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        }
    }

    private ResponseDTO createOrderTable(List<Integer> tablesId, Order order) {
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
                return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
            }
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        } catch (Exception e) {
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        }

    }

    public ResponseDTO updatePeople(Integer people, Integer orderId) {

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return ResponseUtils.fail(404, "Order không tồn tại", null);
        }

        if (!systemService.checkCustomerResource(order.getCustomer().getId())) {
            return ResponseUtils.fail(403, "Bạn không có quyền truy cập order này", null);

        }

        if (people < MIN_PEOPLE || people > MAX_PEOPLE) {
            return ResponseUtils.fail(404, "Số lượng người không hợp lệ", null);
        }

        order.setPeople(orderId);
        order.setUpdateAt(new Date());
        order.setUpdateBy(systemService.getUserLogin());
        orderRepository.save(order);
        return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

    }

    public ResponseDTO test() {
        User user = userRepository.findById(33).get();
        pushNotificationOrder(user.getId(), 150, "dfdsf");

        return ResponseUtils.success(200, "OKE", null);
    }

    private Long total(Order order) {
        long orderTotal = 0;

        // Lặp qua danh sách các mục đơn hàng
        if (order.getOrderItems() != null) {
            for (OrderItem orderItem : order.getOrderItems()) {
                // Tính tổng tiền cho từng mục đơn hàng và cộng vào tổng tiền của đơn hàng
                long itemTotal = calculateItemTotal(orderItem);
                orderTotal += itemTotal;
            }

        }

        if (order.getOrderExtraPortions() != null) {
            for (OrderExtraPortion orderExtraPortion : order.getOrderExtraPortions()) {
                // Tính tổng tiền cho từng mục đơn hàng và cộng vào tổng tiền của đơn hàng
                long itemTotal = calculateExtraPortionTotal(orderExtraPortion);

                orderTotal += itemTotal;
            }
        }

        return orderTotal;

    }

    private Long total(OrderRequestDTO orderRequestDTO) {
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
        SaleEvent saleEvent;
        // Lấy thông tin sản phẩm và biến thể sản phẩm
        if (orderItemDTO.getProductId() != null) {
            Product product = productRepository.findByIdAndActiveIsTrue(orderItemDTO.getProductId()).orElse(null);

            if (product != null) {
                long itemPrice = 0;

                if (orderItemDTO.getProductVariantId() != null) {
                    ProductVarient productVariant = productVarientRepository
                            .findByIdAndActiveIsTrue(orderItemDTO.getProductVariantId())
                            .orElse(null);
                    if (productVariant != null) {

                        saleEvent = saleProductRepository.findSaleByProductVarientId(productVariant.getId()).orNull();
                        if (saleEvent != null) {
                            itemPrice = (long) (productVariant.getPrice()
                                    - (productVariant.getPrice() * ((double) saleEvent.getPercent() / 100)));
                        } else {
                            itemPrice = productVariant.getPrice();

                        }
                    }
                } else {
                    saleEvent = saleProductRepository.findSaleByProductId(product.getId()).orNull();

                    if (saleEvent != null) {
                        itemPrice = (long) (product.getPrice()
                                - (product.getPrice() * ((double) saleEvent.getPercent() / 100)));
                    } else {
                        itemPrice = product.getPrice();
                    }
                }

                itemTotal = itemPrice * orderItemDTO.getQuantity();
            } else {

            }
        }

        if (orderItemDTO.getComboId() != null) {

            ComboProduct comboProduct = comboProductRepository.findByIdAndActiveIsTrue(orderItemDTO.getComboId())
                    .orElse(null);
            if (comboProduct != null) {
                long itemPrice = 0;

                saleEvent = saleProductRepository.findSaleByComboId(comboProduct.getId()).orNull();

                if (saleEvent != null) {
                    itemPrice = (long) (comboProduct.getPrice()
                            - (comboProduct.getPrice() * ((double) saleEvent.getPercent() / 100)));
                } else {
                    itemPrice = comboProduct.getPrice();
                }
                itemTotal = itemTotal + itemPrice;
            }

        }

        return itemTotal;
    }

    private long calculateItemTotal(OrderItem orderItem) {
        long itemTotal = 0;
        // Lấy thông tin sản phẩm và biến thể sản phẩm
        SaleEvent saleEvent;
        Product product = orderItem.getProduct();

        if (product != null) {
            long itemPrice = 0;

            ProductVarient productVariant = orderItem.getProductVarient();
            if (productVariant != null) {

                saleEvent = saleProductRepository.findSaleByProductVarientId(productVariant.getId()).orNull();
                if (saleEvent != null) {
                    itemPrice = (long) (productVariant.getPrice()
                            - (productVariant.getPrice() * ((double) saleEvent.getPercent() / 100)));
                } else {
                    itemPrice = productVariant.getPrice();

                }

            } else {
                saleEvent = saleProductRepository.findSaleByProductId(product.getId()).orNull();

                if (saleEvent != null) {
                    itemPrice = (long) (product.getPrice()
                            - (product.getPrice() * ((double) saleEvent.getPercent() / 100)));
                } else {
                    itemPrice = product.getPrice();
                }
            }
            itemTotal = itemPrice * orderItem.getQuantity();
        }

        ComboProduct comboProduct = orderItem.getComboProduct();
        if (comboProduct != null) {
            long itemPrice = 0;

            saleEvent = saleProductRepository.findSaleByComboId(comboProduct.getId()).orNull();

            if (saleEvent != null) {
                itemPrice = (long) (comboProduct.getPrice()
                        - (comboProduct.getPrice() * ((double) saleEvent.getPercent() / 100)));
            } else {
                itemPrice = comboProduct.getPrice();
            }
            itemTotal = itemTotal + itemPrice;
        }

        return itemTotal;
    }

    private long calculateExtraPortionTotal(ExtraPortionOrderRequestDTO extraPortionOrderRequestDTO) {
        long itemTotal = 0;
        // Lấy thông tin sản phẩm và biến thể sản phẩm
        ExtraPortion extraPortion = extraPortionRepository
                .findByExtraPortionIdAndActiveIsTrue(extraPortionOrderRequestDTO.getId())
                .orElse(null);

        if (extraPortion != null) {

            long itemPrice = extraPortion.getPrice();
            itemTotal = itemPrice * extraPortionOrderRequestDTO.getQuantity();

        }

        return itemTotal;
    }

    private long calculateExtraPortionTotal(OrderExtraPortion orderExtraPortion) {
        long itemTotal = 0;
        ExtraPortion extraPortion = orderExtraPortion.getExtraPortion();

        if (extraPortion != null) {

            long itemPrice = extraPortion.getPrice();
            itemTotal = itemPrice * orderExtraPortion.getQuantity();

        }

        return itemTotal;
    }

    // private List<OrderExtraPortionDTO>
    // getOrderExtraPortions(List<OrderExtraPortion> orderExtraPortions) {
    // List<OrderExtraPortionDTO> orderExtraPortionDTOs = new ArrayList<>();
    // for (OrderExtraPortion orderExtraPortion : orderExtraPortions) {
    // OrderExtraPortionDTO orderExtraPortionDTO =
    // modelMapper.map(orderExtraPortion, OrderExtraPortionDTO.class);

    // File image = orderExtraPortion.getExtraPortion().getImageFile();
    // orderExtraPortionDTO.getExtraPortion().setImageFileUrl(image != null ?
    // image.getLink() : "");
    // orderExtraPortionDTOs.add(orderExtraPortionDTO);
    // }
    // return orderExtraPortionDTOs;
    // }

    private List<OrderExtraPortionDTO> getOrderExtraPortions(List<OrderExtraPortion> orderExtraPortions) {
        List<OrderExtraPortionDTO> orderExtraPortionDTOs = new ArrayList<>();

        for (OrderExtraPortion orderExtraPortion : orderExtraPortions) {

            OrderExtraPortionDTO orderExtraPortionDTO = modelMapper.map(orderExtraPortion,
                    OrderExtraPortionDTO.class);

            File image = orderExtraPortion.getExtraPortion().getImageFile();
            orderExtraPortionDTO.getExtraPortion().setImageFileUrl(image != null ? image.getLink() : "");
            orderExtraPortionDTOs.add(orderExtraPortionDTO);

        }

        return orderExtraPortionDTOs;
    }

    private List<OrderItemDTO> getOrderItems(List<OrderItem> orderItems) {
        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            ProductVarient productVarient = orderItem.getProductVarient();
            ComboProduct comboProduct = orderItem.getComboProduct();

            OrderItemDTO newOrderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
            if (productVarient != null) {
                Product product2 = productVarient.getProduct();
                SaleEvent saleEvent = saleProductRepository.findSaleByProductVarientId(productVarient.getId()).orNull();
                if (saleEvent != null) {

                    newOrderItemDTO.getProductVarient().setSaleEvent(modelMapper.map(saleEvent, SaleEventDTO.class));

                }
                List<ProductCategory> productCategories = productCategoryRepository.findByProduct(product2);

                List<CategoryCompactDTO> categoryDTOs = new ArrayList<>();
                for (ProductCategory productCategory : productCategories) {
                    Category category = productCategory.getCategory();
                    CategoryCompactDTO categoryDTO = modelMapper.map(category, CategoryCompactDTO.class);
                    categoryDTOs.add(categoryDTO);
                }
                newOrderItemDTO.getProductVarient().setCategories(categoryDTOs);

            } else if (product != null) {
                SaleEvent saleEvent = saleProductRepository.findSaleByProductId(product.getId()).orNull();
                if (saleEvent != null) {

                    newOrderItemDTO.getProduct().setSaleEvent(modelMapper.map(saleEvent, SaleEventDTO.class));

                }
                List<ProductCategory> productCategories = productCategoryRepository.findByProduct(product);

                List<CategoryCompactDTO> categoryDTOs = new ArrayList<>();
                for (ProductCategory productCategory : productCategories) {
                    Category category = productCategory.getCategory();
                    CategoryCompactDTO categoryDTO = modelMapper.map(category, CategoryCompactDTO.class);
                    categoryDTOs.add(categoryDTO);
                }
                newOrderItemDTO.getProduct().setCategories(categoryDTOs);

            } else if (comboProduct != null) {
                SaleEvent saleEvent = saleProductRepository.findSaleByComboId(comboProduct.getId()).orNull();
                if (saleEvent != null) {
                    newOrderItemDTO.getComboProduct().setSaleEvent(modelMapper.map(saleEvent, SaleEventDTO.class));

                }

            }

            // if orderItemDTOs dont have item, then create new item

            Long total = calculateItemTotal(orderItem);

            newOrderItemDTO.setTotal(total);

            List<OptionDTO> optionDTOs = new ArrayList<>();
            for (OrderItemOption orderItemOption : orderItem.getOrderItemOptions()) {
                OptionDTO optionDTO = modelMapper.map(orderItemOption.getOption(),
                        OptionDTO.class);
                optionDTOs.add(optionDTO);
            }
            newOrderItemDTO.setOptions(optionDTOs);
            orderItemDTOs.add(newOrderItemDTO);

        }

        return orderItemDTOs;
    }

    // private List<OrderItemDTO> getOrderItems(List<OrderItem> orderItems) {
    // List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
    // for (OrderItem orderItem : orderItems) {
    // OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);

    // List<OptionDTO> optionDTOs = new ArrayList<>();
    // for (OrderItemOption orderItemOption : orderItem.getOrderItemOptions()) {
    // OptionDTO optionDTO = modelMapper.map(orderItemOption.getOption(),
    // OptionDTO.class);
    // optionDTOs.add(optionDTO);
    // }
    // orderItemDTO.setOptions(optionDTOs);
    // orderItemDTOs.add(orderItemDTO);
    // }
    // return orderItemDTOs;
    // }

    private List<TableDTO> getTables(List<OrderTable> tables) {
        List<TableDTO> tableDTOs = new ArrayList<>();
        for (OrderTable table : tables) {
            boolean found = false;
            for (TableDTO tableDTO : tableDTOs) {
                if (tableDTO.getId() == table.getTable().getId()) {
                    found = true;
                    break;
                }
            }

            if (!found) {

                TableDTO tableDTO = modelMapper.map(table.getTable(), TableDTO.class);
                tableDTOs.add(tableDTO);
            }
        }
        return tableDTOs;
    }

    private boolean checkTalbeOccupied(int tableId) {
        Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // Tính thời điểm 24 giờ
        int isOpcciped = orderTableRepository.checkTalbeOccupied(tableId, twentyFourHoursAgo);
        if (isOpcciped > 0) {
            return true;

        }
        return false;
    }

    private boolean checkTalbeOccupied(int tableId, int orderId) {
        Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // Tính thời điểm 24 giờ
        int isOpcciped = orderTableRepository.checkTalbeOccupiedAndRootId(tableId, orderId, twentyFourHoursAgo);
        if (isOpcciped > 0) {
            return true;

        }
        return false;
    }

    // private ResponseDTO syncOrderItem(List<OrderItem> orderItems, Order
    // rootOrder) {
    // List<OrderItem> syncOrderItems = new ArrayList<>();
    // for (OrderItem orderItem : orderItems) {
    // orderItem.setOrder(rootOrder);
    // orderItem.setUpdateAt(new Date());
    // orderItem.setUpdateBy(systemService.getUserLogin());
    // syncOrderItems.add(orderItem);
    // }

    // try {
    // List<OrderItem> listSaved = orderItemRepository.saveAll(syncOrderItems);

    // if (listSaved != null) {
    // return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
    // }
    // return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
    // } catch (Exception e) {
    // return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
    // }
    // }

    private ResponseDTO syncOrderItem(List<OrderItem> orderItems, Order rootOrder) {
        List<OrderItem> syncOrderItems = new ArrayList<>();

        List<OrderItem> rootOrderItems = rootOrder.getOrderItems();

        for (OrderItem orderItem : orderItems) {
            boolean isSame = false;

            for (OrderItem rootOrderItem : rootOrderItems) {
                // Kiểm tra nếu product, product variant hoặc combo giống nhau
                if (isSameProductOrVariantOrCombo(orderItem, rootOrderItem)) {
                    // Cộng số lượng từ orderItem vào rootOrderItem
                    if (checkIsSameOrderItem(orderItem, rootOrderItem)) {
                        rootOrderItem.setQuantity(rootOrderItem.getQuantity() +
                                orderItem.getQuantity());
                        isSame = true;
                        syncOrderItems.add(rootOrderItem);

                        break;
                    }
                }

            }
            if (!isSame) {
                orderItem.setOrder(rootOrder);
                syncOrderItems.add(orderItem);

            }

        }

        try {

            List<OrderItem> listSaved = orderItemRepository.saveAll(syncOrderItems);
            // listSaved = orderItemRepository.saveAll(rootChanges);
            if (listSaved != null) {
                return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
            }
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        } catch (Exception e) {
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        }
    }

    private ResponseDTO syncExtraPortion(List<OrderExtraPortion> extraPortions, Order rootOrder) {
        List<OrderExtraPortion> syncOrderExtraPortions = new ArrayList<>();
        for (OrderExtraPortion extraPortion : extraPortions) {
            boolean isSame = false;
            for (OrderExtraPortion rootExtraPortion : rootOrder.getOrderExtraPortions()) {
                if (rootExtraPortion.getExtraPortion().getExtraPortionId() == extraPortion.getExtraPortion()
                        .getExtraPortionId()) {
                    rootExtraPortion.setQuantity(rootExtraPortion.getQuantity() + extraPortion.getQuantity());
                    isSame = true;
                    syncOrderExtraPortions.add(rootExtraPortion);
                    break;
                }

            }

            if (!isSame) {
                extraPortion.setOrder(rootOrder);
                extraPortion.setUpdateAt(new Date());
                extraPortion.setUpdateBy(systemService.getUserLogin());
                syncOrderExtraPortions.add(extraPortion);
            }
        }

        try {
            List<OrderExtraPortion> listSaved = orderExtraPortionRepository.saveAll(syncOrderExtraPortions);

            if (listSaved != null) {
                return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
            }
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        } catch (Exception e) {
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        }
    }

    private ResponseDTO syncOrderTable(List<OrderTable> orderTables, Order rootOrder) {
        List<OrderTable> syncOrderTables = new ArrayList<>();
        for (OrderTable orderTable : orderTables) {
            orderTable.setOrder(rootOrder);
            orderTable.setUpdateAt(new Date());
            orderTable.setUpdateBy(systemService.getUserLogin());
            syncOrderTables.add(orderTable);
        }

        try {
            List<OrderTable> listSaved = orderTableRepository.saveAll(syncOrderTables);

            if (listSaved != null) {
                return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);
            }
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        } catch (Exception e) {
            return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);
        }
    }

    private ResponseDTO switchOrderTable(List<OrderTable> orderTables, Table destinationTable) {
        List<OrderTable> syncOrderTables = new ArrayList<>();
        for (OrderTable orderTable : orderTables) {
            orderTable.setTable(destinationTable);
            orderTable.setUpdateAt(new Date());
            orderTable.setUpdateBy(systemService.getUserLogin());
            syncOrderTables.add(orderTable);
        }

        try {
            List<OrderTable> listSaved = orderTableRepository.saveAll(syncOrderTables);

            if (listSaved != null) {
                return ResponseUtils.success(200, "Đổi bàn thành công!", null);
            }
            return ResponseUtils.fail(500, "Đổi bàn không thành công", null);
        } catch (Exception e) {
            return ResponseUtils.fail(500, "Đổi bàn không thành công", null);
        }
    }

    private String generateOrderCheckoutUrl(Order order, Long total) {
        int orderCodePrefix = 100000;
        String checkoutUrl = null;
        while (true) {
            int orderCode = Integer.valueOf(orderCodePrefix + String.valueOf(order.getOrderId()));
            System.out.println(orderCode);
            checkoutUrl = payOSService.getCheckoutUrl(orderCode, total,
                    "Thanh toán hóa đơn");

            if (checkoutUrl == null) {
                System.out.println(orderCode);
                orderCodePrefix++;
                continue;
            }
            return checkoutUrl;
        }
    }

    public Integer getOrderIdFromOrderCode(Integer orderCode) {
        String orderCodeStr = String.valueOf(orderCode);
        String orderId = orderCodeStr.substring(6, orderCodeStr.length());
        return Integer.valueOf(orderId);
    }

    private void pushNotificationOrder(Integer userId, Integer orderId, String body) {
        String urlToOrder = ApplicationConst.CLIENT_HOST + "/myorder/" + orderId;
        pushNotificationService.pushNotification(userId, PushNotificationService.HIGT, "Order của bạn", body,
                urlToOrder);
    }

    private boolean isSameProductOrVariantOrCombo(OrderItem orderItem, OrderItem rootOrderItem) {
        // Kiểm tra theo product
        if ((orderItem.getProduct() != null && rootOrderItem.getProduct() != null)) {

            if (orderItem.getProduct().getId() == (rootOrderItem.getProduct().getId())) {
                checkIsSameOrderItem(orderItem, rootOrderItem);
                return true;

            }
        }

        // Kiểm tra theo product variant
        if ((orderItem.getProductVarient() != null && rootOrderItem.getProductVarient() != null)) {

            if (orderItem.getProductVarient().getId() == (rootOrderItem.getProductVarient().getId())) {
                return true;

            }
        }

        // Kiểm tra theo combo
        if ((orderItem.getComboProduct() != null && rootOrderItem.getComboProduct() != null)) {

            if (orderItem.getComboProduct().getId() == (rootOrderItem.getComboProduct().getId())) {
                return true;

            }
        }

        return false;
    }

    private boolean isSameProductOrVariantOrCombo(OrderItem orderItem, OrderItemRequestDTO orderItemRequestDTO) {
        // Kiểm tra theo product
        if ((orderItem.getProduct() != null && orderItemRequestDTO.getProductId() != null)) {

            if (orderItem.getProduct().getId() == (orderItemRequestDTO.getProductId())) {

                return checkIsSameOrderItem(orderItemRequestDTO, orderItem);

            }
        }

        // Kiểm tra theo product variant
        if ((orderItem.getProductVarient() != null && orderItemRequestDTO.getProductVariantId() != null)) {

            if (orderItem.getProductVarient().getId() == (orderItemRequestDTO.getProductVariantId())) {
                return true;

            }
        }

        // Kiểm tra theo combo
        if ((orderItem.getComboProduct() != null && orderItemRequestDTO.getComboId() != null)) {

            if (orderItem.getComboProduct().getId() == (orderItemRequestDTO.getComboId())) {
                return true;

            }
        }

        return false;
    }

    private boolean checkIsSameOrderItem(OrderItem orderItem, OrderItem rOrderItem) {
        List<OrderItemOption> rOrderItemOptions = rOrderItem.getOrderItemOptions();
        List<OrderItemOption> orderItemOptions = orderItem.getOrderItemOptions();
        if (rOrderItemOptions != null && orderItemOptions != null) {
            List<Integer> roorOrderItemOptionIds = rOrderItemOptions.stream()
                    .map(orderItemOption -> orderItemOption.getOption().getId())
                    .collect(Collectors.toList());

            List<Integer> orderItemOptionIds = orderItemOptions.stream()
                    .map(orderItemOption -> orderItemOption.getOption().getId())
                    .collect(Collectors.toList());

            boolean areOptionsSame = roorOrderItemOptionIds.containsAll(orderItemOptionIds)
                    && orderItemOptionIds.containsAll(roorOrderItemOptionIds);

            if (areOptionsSame) {
                String orderItemNote = orderItem.getNote();
                String rootOrderItemNote = rOrderItem.getNote();
                if (rootOrderItemNote != null && rootOrderItemNote != null) {
                    return rootOrderItemNote.equalsIgnoreCase(orderItemNote);
                }
            }

            return false;
        }
        return false;
    }

    private boolean checkIsSameOrderItem(OrderItemRequestDTO orderItemRequestDTO, OrderItem rOrderItem) {
        List<OrderItemOption> rOrderItemOptions = rOrderItem.getOrderItemOptions();
        List<Integer> orderItemOptionIds = orderItemRequestDTO.getOptionsId();
        if (rOrderItemOptions != null && orderItemOptionIds != null) {
            List<Integer> roorOrderItemOptionIds = rOrderItemOptions.stream()
                    .map(orderItemOption -> orderItemOption.getOption().getId())
                    .collect(Collectors.toList());

            boolean areOptionsSame = roorOrderItemOptionIds.containsAll(orderItemOptionIds)
                    && orderItemOptionIds.containsAll(roorOrderItemOptionIds);

            if (areOptionsSame) {
                String orderItemNote = orderItemRequestDTO.getNote();
                String rootOrderItemNote = rOrderItem.getNote();
                if (rootOrderItemNote != null && rootOrderItemNote != null) {
                    return rootOrderItemNote.equalsIgnoreCase(orderItemNote);
                }
            }

            return false;
        }
        return false;
    }

    public boolean isStaffCreateOrderAndConfirm() {
        User user = systemService.getUserLogin();
        boolean hasPermission = rolePermissionService.checkUserRolePermission(user.getRole().getId(), "PURCHASE_ORDER",
                "CONFIRM");
        return hasPermission;
    }

    private boolean isOrderUpdated(Order order) {
        if (!order.getStatus().getId().equals(ApplicationConst.OrderStatus.CANCEL)
                && !order.getStatus().getId().equals(ApplicationConst.OrderStatus.COMPLETE)
                && !order.getStatus().getId().equals(ApplicationConst.OrderStatus.AWAIT_CHECKOUT)) {
            return true;
        }
        return false;
    }
}
