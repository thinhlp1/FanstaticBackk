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
    public static final String TOPIC_ORDER_CHECKOUT = TOPIC + "/purchase/order/checkout";

    public static final String TOPIC_CURRENT_ORDER = TOPIC + "/purchase/order/current-order";

}
