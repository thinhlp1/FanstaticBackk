package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.config.constants.WebsocketConst;
import com.fanstatic.controller.order.WSPurcharseOrderController;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.notification.NotificationDTO;
import com.fanstatic.model.Notification;
import com.fanstatic.model.User;
import com.fanstatic.repository.NotificationRepository;
import com.fanstatic.repository.UserRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final RolePermissionService rolePermissionService;
    private final SystemService systemService;

    private final WSPurcharseOrderController wsPurcharseOrderController;

    private final ModelMapper modelMapper;

    private final String DETAIL_TO_ORDER = "";

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
        List<Notification> notifications = notificationRepository.findByReceiver(user);
        List<ResponseDataDTO> notificationDTOs = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = modelMapper.map(notification, NotificationDTO.class);
            notificationDTO.setHasSeen(!(notification.getSeenAt() == null));
            notificationDTOs.add(notificationDTO);
        }
        ResponseListDataDTO responseListDataDTO = new ResponseListDataDTO();
        responseListDataDTO.setNameList("Danh sách thông báo");
        responseListDataDTO.setDatas(notificationDTOs);

        return ResponseUtils.success(200, "Danh sách thông báo", responseListDataDTO);
    }

    public boolean sendOrderCreate(Integer orderId) {
        String message = "Order mới được tạo. Mã order: " + orderId;
        String action = ApplicationConst.CLIENT_HOST + "/" + DETAIL_TO_ORDER + "/" + orderId;
        String title = "Order mới";

        // get user who receive notication
        List<User> users = userRepository
                .findByRoleRolePermissionsFeaturePermissionManagerFeatureIdAndRoleRolePermissionsFeaturePermissionPermissionId(
                        ApplicationConst.Notification.RECEIVE_NOTIFICATION, ApplicationConst.Notification.NEWORDER);
        // System.out.println("USERL: " + users.size());
        saveNotification(users, message, title, action);
        return true;
    }

    public boolean sendCustomerOrder(Integer orderId, User customer) {
        String message = "Order mới được tạo. Mã order: " + orderId;
        String action = ApplicationConst.CLIENT_HOST + "/" + DETAIL_TO_ORDER + "/" + orderId;
        String title = "Order mới";

        Notification notification = new Notification();
        notification.setAction(action);
        notification.setContent(message);
        notification.setSendAt(new Date());
        notification.setTitle(title);
        notification.setReceiver(customer);

        Notification notificationSaved = notificationRepository.saveAndFlush(notification);

        NotificationDTO notificationDTO = modelMapper.map(notificationSaved, NotificationDTO.class);
        notificationDTO.setHasSeen(!(notification.getSeenAt() == null));

        wsPurcharseOrderController.sendWebSocketResponse(notificationDTO,
                WebsocketConst.TOPPIC_NOTIFICATION + "/" + customer.getId());

        return true;
    }

    public boolean sendOrderCheckout(Integer orderId) {
        String message = "Order " + orderId + " đang yêu cầu thanh toán";
        String action = ApplicationConst.CLIENT_HOST + "/" + DETAIL_TO_ORDER + "/" + orderId;
        String title = "Order cần thánh toán";

        // get user who receive notication
        List<User> users = userRepository
                .findByRoleRolePermissionsFeaturePermissionManagerFeatureIdAndRoleRolePermissionsFeaturePermissionPermissionId(
                        ApplicationConst.Notification.RECEIVE_NOTIFICATION,
                        ApplicationConst.Notification.CHECKOUTORDER);

        saveNotification(users, message, title, action);
        return true;
    }

    public boolean sendOrderComplete(Integer orderId) {
        String message = "Order " + orderId + " đã hoàn thành";
        String action = ApplicationConst.CLIENT_HOST + "/" + DETAIL_TO_ORDER + "/" + orderId;
        String title = "Order đã hoàn thành";

        // get user who receive notication
        List<User> users = userRepository
                .findByRoleRolePermissionsFeaturePermissionManagerFeatureIdAndRoleRolePermissionsFeaturePermissionPermissionId(
                        ApplicationConst.Notification.RECEIVE_NOTIFICATION,
                        ApplicationConst.Notification.COMPLETEORDER);

        saveNotification(users, message, title, action);
        return true;
    }

    public boolean sendOrderupdate(Integer orderId) {
        String message = "Order " + orderId + " đã được cập nhật";
        String action = ApplicationConst.CLIENT_HOST + "/" + DETAIL_TO_ORDER + "/" + orderId;
        String title = "Order đã được cập nhật";

        // get user who receive notication
        List<User> users = userRepository
                .findByRoleRolePermissionsFeaturePermissionManagerFeatureIdAndRoleRolePermissionsFeaturePermissionPermissionId(
                        ApplicationConst.Notification.RECEIVE_NOTIFICATION,
                        ApplicationConst.Notification.UPDATEORDER);

        saveNotification(users, message, title, action);
        return true;
    }

    public void saveNotification(List<User> users, String message, String title, String action) {
        List<String> topics = new ArrayList<>();
        List<Notification> notifications = new ArrayList<>();

        for (User user : users) {
            Notification notification = new Notification();
            notification.setAction(action);
            notification.setContent(message);
            notification.setSendAt(new Date());
            notification.setTitle(title);
            notification.setReceiver(user);

            topics.add(WebsocketConst.TOPPIC_NOTIFICATION + "/" + user.getId());
            notifications.add(notification);
        }

        List<Notification> notificationsSaved = notificationRepository.saveAllAndFlush(notifications);
        for (Notification notification2 : notificationsSaved) {
            NotificationDTO notificationDTO = modelMapper.map(notification2, NotificationDTO.class);
            notificationDTO.setHasSeen(!(notification2.getSeenAt() == null));
            wsPurcharseOrderController.sendWebSocketResponse(notificationDTO,
                    WebsocketConst.TOPPIC_NOTIFICATION + "/" + notification2.getReceiver().getId());
        }
    }

}
