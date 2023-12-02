package com.fanstatic.config.constants;

public class WebsocketConst {
    public static final String TOPIC = "/topic";

    public static final String TOPIC_PURCHARSE_ORDER = TOPIC + "/purcharse/order";

    /*
     * Lắng nghe thay đổi khi order thay đổi
     * data trả về là order
     */
    public static final String TOPIC_ORDER_DETAILS = TOPIC + "/purchase/order/detail";

    /*
     * Lắng nghe các sự kiện khi có order mới đc tạo
     * data : dữ liệu order mới tạo
     */
    public static final String TOPIC_ORDER_NEW = TOPIC + "/purchase/order/new";

    /*
     * lắng nghe sự kiện update order như : xác nhận, hủy, cập nhật,...
     * data : dữ liệu order vừa mới update
     */
    public static final String TOPIC_ORDER_UPDATE = TOPIC + "/purchase/order/update";

    /*
     * lắng nghe sự kiện khi yêu cầu thanh toán order
     * data : dữ liệu order vừa mới update
     */
    public static final String TOPIC_ORDER_CHECKOUT_REQUEST = TOPIC + "/purchase/order/checkout-request";

    /*
     * lắng nghe sự kiện khi thanh toán thành công
     * data : dữ liệu order vừa mới update
     */
    public static final String TOPIC_ORDER_CHECKOUT_PAID = TOPIC + "/purchase/order/checkout-paid";

    /*
     * lắng nghe sự kiện khi yêu cầu thanh toán đc nhân viên xác nhạn
     * data : dữ liệu order vừa mới update
     */
    public static final String TOPIC_ORDER_AWAIT_CHECKOUT = TOPIC + "/purchase/order/await-checkout";

    /*
     * lắng nghe sự kiện khi yêu cầu thanh toán đc nhân viên xác nhạn
     * data : dữ liệu order vừa mới update
     */
    public static final String TOPIC_ORDER_CANCEL_CHECKOUT = TOPIC + "/purchase/order/cancel-checkout";

    /*
     * lắng nghe sự kiện khi có sản phẩm vừa mới cập nhật hết hàng hoặc còn hàng
     * data : dữ liệu order vừa mới update
     */
    public static final String TOPIC_PRODUCT_CHANGE_STOCK = TOPIC + "/product/change-stock";

    /*
     * lắng nghe sự kiện khi có khách hàng tạo thông báo yêu cầu
     * data : dữ liệu order vừa mới update
     */
    public static final String TOPIC_REQUEST_STAFF_NEW = TOPIC + "/request-staff/new";

    /*
     * lắng nghe sự kiện khi có yêu cầu có thay đổi như xác nhận hoặc từ chối
     * data : dữ liệu order vừa mới update
     */
    public static final String TOPIC_REQUEST_STAFF_UPDATE = TOPIC + "/request-staff/udpate";

    /*
     * lắng nghe sự kiện khi xác nhận hoặc từ chối yêu cầu
     * data : dữ liệu order vừa mới update
     */
    public static final String TOPIC_REQUEST_STAFF_DETAIL = TOPIC + "/request-staff/detail";

}
