package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.order.OrderDTO;
import com.fanstatic.dto.model.requestStaff.CreateRequestStaffNotificationDTO;
import com.fanstatic.dto.model.requestStaff.RequestStaffNotificationDTO;
import com.fanstatic.dto.model.user.UserCompactDTO;
import com.fanstatic.model.File;
import com.fanstatic.model.Order;
import com.fanstatic.model.RequestStaffNotification;
import com.fanstatic.model.User;
import com.fanstatic.repository.OrderRepository;
import com.fanstatic.repository.RequestStaffNotificationRepository;
import com.fanstatic.service.order.OrderService;
import com.fanstatic.service.system.PushNotificationService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.DateUtils;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestStaffNotifacationService {
        private final RequestStaffNotificationRepository requestStaffNotificationRepository;
        private final SystemService systemService;
        private final PushNotificationService pushNotificationService;
        private final OrderService orderService;
        private final OrderRepository orderRepository;

        private final ModelMapper modelMapper;

        public ResponseDTO create(CreateRequestStaffNotificationDTO createRequestStaffNotificationDTO) {
                RequestStaffNotification requestStaffNotification = new RequestStaffNotification();
                User customer = systemService.getUserLogin();

                Order order = orderRepository.findById(createRequestStaffNotificationDTO.getOrderId()).orElse(null);
                if (order == null) {
                        return ResponseUtils.fail(404, "Order không tồn tại", null);

                }

                requestStaffNotification.setCustomer(customer);
                requestStaffNotification.setContent(createRequestStaffNotificationDTO.getContent());
                requestStaffNotification.setCreateAt(new Date());
                requestStaffNotification.setOrder(order);
                RequestStaffNotification requestStaffNotificationSaved = requestStaffNotificationRepository
                                .saveAndFlush(requestStaffNotification);

                RequestStaffNotificationDTO requestStaffNotificationDTO = modelMapper.map(requestStaffNotificationSaved,
                                RequestStaffNotificationDTO.class);
                requestStaffNotificationDTO.setOrderDTO((OrderDTO) orderService
                                .detail(createRequestStaffNotificationDTO.getOrderId()).getData());

                return ResponseUtils.success(200, "Yêu cầu thành công", requestStaffNotificationDTO);
        }

        public ResponseDTO confirm(Integer id) {
                RequestStaffNotification requestStaffNotification = requestStaffNotificationRepository.findById(id)
                                .orElse(null);
                if (requestStaffNotification == null) {
                        return ResponseUtils.fail(200, "Yêu cầu không tồn tại", null);
                }
                if (requestStaffNotification.getEmployeeConfirm() != null) {
                        return ResponseUtils.fail(400, "Yêu cầu đã bị nhân viên từ chối",
                                        modelMapper.map(requestStaffNotification.getEmployeeConfirm(),
                                                        UserCompactDTO.class));

                }

                User employeeConfirm = systemService.getUserLogin();

                requestStaffNotification.setEmployeeConfirm(employeeConfirm);
                requestStaffNotification.setConfirmAt(new Date());

                RequestStaffNotification requestStaffNotificationSaved = requestStaffNotificationRepository
                                .saveAndFlush(requestStaffNotification);

                RequestStaffNotificationDTO requestStaffNotificationDTO = details(requestStaffNotificationSaved);
                pushNotificationToUser(requestStaffNotification.getCustomer().getId(), "Nhân viên đang tới",
                                "Order của bạn đã được tiếp nhận");
                return ResponseUtils.success(200, "Xác nhận thành công", requestStaffNotificationDTO);
        }

        public ResponseDTO deny(Integer id) {
                RequestStaffNotification requestStaffNotification = requestStaffNotificationRepository.findById(id)
                                .orElse(null);
                if (requestStaffNotification == null) {
                        return ResponseUtils.fail(404, "Yêu cầu không tồn tại", null);
                }

                if (requestStaffNotification.getEmployeeConfirm() != null) {
                        return ResponseUtils.fail(400, "Yêu cầu đã bị nhân viên từ chối",
                                        modelMapper.map(requestStaffNotification.getEmployeeConfirm(),
                                                        UserCompactDTO.class));

                }
                User employeeConfirm = systemService.getUserLogin();

                requestStaffNotification.setEmployeeConfirm(employeeConfirm);
                requestStaffNotification.setDenyAt(new Date());

                RequestStaffNotification requestStaffNotificationSaved = requestStaffNotificationRepository
                                .saveAndFlush(requestStaffNotification);

                RequestStaffNotificationDTO requestStaffNotificationDTO = details(requestStaffNotificationSaved);
                pushNotificationToUser(requestStaffNotification.getCustomer().getId(), "Nhân viên đang tới",
                                "Order của bạn đã được tiếp nhận");

                return ResponseUtils.success(200, "Xác nhận thành công", requestStaffNotificationDTO);
        }

        public RequestStaffNotificationDTO details(RequestStaffNotification requestStaffNotification) {
                RequestStaffNotificationDTO requestStaffNotificationDTO = modelMapper.map(requestStaffNotification,
                                RequestStaffNotificationDTO.class);

                File file = requestStaffNotification.getCustomer().getImage();
                if (file != null) {
                        requestStaffNotificationDTO.getCustomer().setImageUrl(file.getLink());
                }

                Order order = requestStaffNotification.getOrder();

                if (order != null) {
                        requestStaffNotificationDTO.setOrderDTO((OrderDTO) orderService
                                        .detail(order.getOrderId()).getData());
                }
                if (requestStaffNotification.getDenyAt() != null) {
                        requestStaffNotificationDTO.setStatus("DENIED");
                } else if (requestStaffNotification.getConfirmAt() != null) {
                        requestStaffNotificationDTO.setStatus("CONFIRM");
                } else {
                        requestStaffNotificationDTO.setStatus("WAIT");
                }
                return requestStaffNotificationDTO;
        }

        public ResponseDTO showToDay() {
                Date twentyFourHoursAgo = DateUtils.getDayBeforeTime(24); // Tính thời
                                                                          // điểm 24 giờ

                List<RequestStaffNotification> requestStaffNotifications = requestStaffNotificationRepository
                                .findAllInTime(twentyFourHoursAgo);
                List<ResponseDataDTO> requestStaffNotificationDTOs = new ArrayList<>();

                for (RequestStaffNotification requestStaffNotification : requestStaffNotifications) {
                        RequestStaffNotificationDTO requestStaffNotificationDTO = details(
                                        requestStaffNotification);
                        requestStaffNotificationDTOs.add(requestStaffNotificationDTO);
                }

                ResponseListDataDTO responseListDataDTO = new ResponseListDataDTO();
                responseListDataDTO.setDatas(requestStaffNotificationDTOs);
                responseListDataDTO.setNameList("Danhh sách gọi nhân viên");

                return ResponseUtils.success(200, "Danh sach gọi nhân viên", responseListDataDTO);
        }

        public ResponseDTO show() {

                List<RequestStaffNotification> requestStaffNotifications = requestStaffNotificationRepository
                                .findAllOrderCreateAtDesc();
                List<ResponseDataDTO> requestStaffNotificationDTOs = new ArrayList<>();

                for (RequestStaffNotification requestStaffNotification : requestStaffNotifications) {
                        RequestStaffNotificationDTO requestStaffNotificationDTO = details(
                                        requestStaffNotification);
                        requestStaffNotificationDTOs.add(requestStaffNotificationDTO);
                }

                ResponseListDataDTO responseListDataDTO = new ResponseListDataDTO();
                responseListDataDTO.setDatas(requestStaffNotificationDTOs);
                responseListDataDTO.setNameList("Danhh sách gọi nhân viên");

                return ResponseUtils.success(200, "Danh sach gọi nhân viên", responseListDataDTO);
        }

        private void pushNotificationToUser(Integer userId, String title, String body) {
                String url = ApplicationConst.CLIENT_HOST;
                pushNotificationService.pushNotification(userId, PushNotificationService.HIGT, title, body,
                                url);
        }
}
