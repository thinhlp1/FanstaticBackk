package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.config.constants.WebsocketConst;
import com.fanstatic.controller.order.WSPurcharseOrderController;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.notification.NotificationDTO;
import com.fanstatic.dto.model.order.OrderDTO;
import com.fanstatic.dto.model.user.UserCompactDTO;
import com.fanstatic.model.File;
import com.fanstatic.model.Notification;
import com.fanstatic.model.Order;
import com.fanstatic.model.User;
import com.fanstatic.repository.NotificationRepository;
import com.fanstatic.repository.OrderRepository;
import com.fanstatic.repository.UserRepository;
import com.fanstatic.service.order.OrderService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.DateUtils;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final RolePermissionService rolePermissionService;
    private final SystemService systemService;
    private final OrderRepository orderRepository;
    private final WSPurcharseOrderController wsPurcharseOrderController;

    private final ModelMapper modelMapper;

    private final String DETAIL_TO_ORDER = "/staff/manage-order?id=";
    private final String DETAILS_TO_MY_ORDER = "/customer/myorder?id=";

    public ResponseDTO seenNotification(Integer id) {
        Notification notification = notificationRepository.findById(id).orElse(null);

        if (notification == null) {
            return ResponseUtils.fail(404, "Thông báo không tồn tại", null);
        }

        if (!systemService.checkUserResource(notification.getReceiver().getId())) {
            return ResponseUtils.fail(401, "Bạn không có quyền truy cập", null);
        }

        notification.setSeenAt(new Date());
        notificationRepository.save(notification);
        return ResponseUtils.success(200, "Thành công", null);
    }

    public ResponseDTO getNotification() {
        User user = systemService.getUserLogin();
        List<Notification> notifications = notificationRepository.findByReceiverOrderBySendAtDesc(user);
        List<ResponseDataDTO> notificationDTOs = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = modelMapper.map(notification, NotificationDTO.class);
            if (notification.getSender() != null) {
                File file = notification.getSender().getImage();
                if (file != null) {
                    notificationDTO.getSender().setImageUrl(file.getLink());
                }
            }
            notificationDTO.setHasSeen(!(notification.getSeenAt() == null));

            if (notification.getType().equals(ApplicationConst.NotificationType.ORDER)) {
                Order order = orderRepository.findById(Integer.valueOf(notification.getObjectId())).orElse(null);
                if (order != null) {
                    if (order.getStatus().getId().equals(ApplicationConst.OrderStatus.CANCEL)
                            || order.getStatus().getId().equals(ApplicationConst.OrderStatus.COMPLETE)) {
                        notificationDTO.setType(ApplicationConst.NotificationType.ORDER_NO_ACTION);
                    }
                }
            }

            notificationDTOs.add(notificationDTO);
        }
        ResponseListDataDTO responseListDataDTO = new ResponseListDataDTO();
        responseListDataDTO.setNameList("Danh sách thông báo");
        responseListDataDTO.setDatas(notificationDTOs);

        return ResponseUtils.success(200, "Danh sách thông báo", responseListDataDTO);
    }

    public boolean sendSwitchTable(Integer orderId, int tableNumber) {
        String message = "Order" + orderId + " đã được chuyển sang bàn số" + tableNumber;
        String action = DETAIL_TO_ORDER + orderId;
        String title = "Khách hàng chuyển bàn";

        // get user who receive notication
        List<User> users = userRepository
                .findByRoleRolePermissionsFeaturePermissionManagerFeatureIdAndRoleRolePermissionsFeaturePermissionPermissionId(
                        ApplicationConst.Notification.RECEIVE_NOTIFICATION, ApplicationConst.Notification.UPDATEORDER);
        // System.out.println("USERL: " + users.size());
        saveNotification(users, message, title, action, ApplicationConst.NotificationType.ORDER,
                String.valueOf(orderId));
        return true;
    }

    public boolean sendOrderCreate(Integer orderId) {
        String message = "Order mới được tạo. Mã order: " + orderId;
        String action = DETAIL_TO_ORDER + orderId;
        String title = "Order mới";

        // get user who receive notication
        List<User> users = userRepository
                .findByRoleRolePermissionsFeaturePermissionManagerFeatureIdAndRoleRolePermissionsFeaturePermissionPermissionId(
                        ApplicationConst.Notification.RECEIVE_NOTIFICATION, ApplicationConst.Notification.NEWORDER);
        // System.out.println("USERL: " + users.size());
        saveNotification(users, message, title, action, ApplicationConst.NotificationType.ORDER,
                String.valueOf(orderId));
        return true;
    }

    public boolean sendCustomerOrder(Integer orderId, User customer, String message, String title) {
        // String message = "Order mới được tạo. Mã order: " + orderId;
        String action = DETAILS_TO_MY_ORDER + orderId;
        // String title = "Order mới";

        Notification notification = new Notification();
        notification.setAction(action);
        notification.setContent(message);
        notification.setSendAt(new Date());
        notification.setTitle(title);
        notification.setReceiver(customer);
        notification.setType(ApplicationConst.NotificationType.ORDER);
        notification.setObjectId(String.valueOf(orderId));

        User seender = notification.getSender();
        if (seender != null) {
            UserCompactDTO userCompactDTO = modelMapper.map(seender, UserCompactDTO.class);
            if (seender.getImage() != null) {
                userCompactDTO.setImageUrl(seender.getImage().getLink());
            }
        }

        Notification notificationSaved = notificationRepository.saveAndFlush(notification);

        NotificationDTO notificationDTO = modelMapper.map(notificationSaved, NotificationDTO.class);
        notificationDTO.setHasSeen(!(notification.getSeenAt() == null));

        wsPurcharseOrderController.sendWebSocketResponse(notificationDTO,
                WebsocketConst.TOPPIC_NOTIFICATION + "/" + customer.getId());

        return true;
    }

    public boolean sendOrderCheckout(Integer orderId) {
        String message = "Order " + orderId + " đang yêu cầu thanh toán. Mã order: " + orderId;
        String action = DETAIL_TO_ORDER + orderId;
        String title = "Order cần thánh toán";

        // get user who receive notication
        List<User> users = userRepository
                .findByRoleRolePermissionsFeaturePermissionManagerFeatureIdAndRoleRolePermissionsFeaturePermissionPermissionId(
                        ApplicationConst.Notification.RECEIVE_NOTIFICATION,
                        ApplicationConst.Notification.CHECKOUTORDER);

        saveNotification(users, message, title, action, ApplicationConst.NotificationType.ORDER,
                String.valueOf(orderId));
        return true;
    }

    public boolean sendOrderComplete(Integer orderId) {
        String message = "Order " + orderId + " đã hoàn thành. Mã order: " + orderId;
        String action = DETAIL_TO_ORDER + orderId;
        String title = "Order đã hoàn thành";

        // get user who receive notication
        List<User> users = userRepository
                .findByRoleRolePermissionsFeaturePermissionManagerFeatureIdAndRoleRolePermissionsFeaturePermissionPermissionId(
                        ApplicationConst.Notification.RECEIVE_NOTIFICATION,
                        ApplicationConst.Notification.COMPLETEORDER);

        saveNotification(users, message, title, action, ApplicationConst.NotificationType.ORDER,
                String.valueOf(orderId));
        return true;
    }

    public boolean sendOrderupdate(Integer orderId) {
        String message = "Order " + orderId + " đã được cập nhật. Mã order: " + orderId;
        String action = DETAIL_TO_ORDER + orderId;
        String title = "Order đã được cập nhật";

        // get user who receive notication
        List<User> users = userRepository
                .findByRoleRolePermissionsFeaturePermissionManagerFeatureIdAndRoleRolePermissionsFeaturePermissionPermissionId(
                        ApplicationConst.Notification.RECEIVE_NOTIFICATION,
                        ApplicationConst.Notification.UPDATEORDER);

        saveNotification(users, message, title, action, ApplicationConst.NotificationType.ORDER,
                String.valueOf(orderId));
        return true;
    }

    public boolean sendOrderCancel(Integer orderId) {
        String message = "Order " + orderId + " đã bị hủy. Mã order: " + orderId;
        String action = DETAIL_TO_ORDER + orderId;
        String title = "Order đã bị hủy";

        // get user who receive notication
        List<User> users = userRepository
                .findByRoleRolePermissionsFeaturePermissionManagerFeatureIdAndRoleRolePermissionsFeaturePermissionPermissionId(
                        ApplicationConst.Notification.RECEIVE_NOTIFICATION,
                        ApplicationConst.Notification.UPDATEORDER);

        saveNotification(users, message, title, action, ApplicationConst.NotificationType.ORDER,
                String.valueOf(orderId));
        return true;
    }

    public void saveNotification(List<User> users, String message, String title, String action, String type,
            String objectId) {
        List<String> topics = new ArrayList<>();
        List<Notification> notifications = new ArrayList<>();

        for (User user : users) {
            Notification notification = new Notification();
            notification.setSender(systemService.getUserLogin());
            notification.setAction(action);
            notification.setContent(message);
            notification.setSendAt(new Date());
            notification.setTitle(title);
            notification.setReceiver(user);
            notification.setType(type);
            notification.setObjectId(objectId);

            topics.add(WebsocketConst.TOPPIC_NOTIFICATION + "/" + user.getId());
            notifications.add(notification);
        }

        List<Notification> notificationsSaved = notificationRepository.saveAllAndFlush(notifications);
        for (Notification notification2 : notificationsSaved) {
            NotificationDTO notificationDTO = modelMapper.map(notification2, NotificationDTO.class);
            if (notification2.getSender() != null) {
                File file = notification2.getSender().getImage();
                if (file != null) {
                    notificationDTO.getSender().setImageUrl(file.getLink());
                }
            }
            notificationDTO.setHasSeen(!(notification2.getSeenAt() == null));
            wsPurcharseOrderController.sendWebSocketResponse(notificationDTO,
                    WebsocketConst.TOPPIC_NOTIFICATION + "/" + notification2.getReceiver().getId());
        }
    }

    public void cleareNotification() {
        Date date = DateUtils.getDateBefore(30);
        notificationRepository.deleteBySeenAtBefore(date);

    }

}
