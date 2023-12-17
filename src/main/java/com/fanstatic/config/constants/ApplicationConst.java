package com.fanstatic.config.constants;

public class ApplicationConst {
    public static final String CLIENT_HOST = "http://fanstatic.vivit.live";
    // public static final String CLIENT_HOST = "http://vivit.live:3000";

    public static final String CLIENT_HOST_ICON = "http://fanstatic.vivit.live/icon/favicon.svg";

    public static final int CUSTOMER_ROLE_ID = 3;
    public static final int ADNIN_ROLE_ID = 1;
    public static final int CASHIER_ROLE_ID = 2;
    public static final int MANAGER_ROLE_ID = 4;
    public static final int WAITER_ROLE_ID = 5;

    public static final String ADMIN_ROUTE = "/admin";
    public static final String CASHIER_ROUTE = "/cashier";
    public static final String WAITER_ROUTE = "/waiter";
    public static final String CUSTOMER_ROUTE = "/customer";

    public static class PaymentMethod {
        public static final String CASH = "CASH";
        public static final String INTERNET_BANKING = "INTERNET_BANKING";
    }

    public static class OrderStatus {
        public static final String CONFIRMING = "CONFIRMING";
        public static final String COMPLETE = "COMPLETE";
        public static final String PROCESSING = "PROCESSING";
        public static final String CANCEL = "CANCEL";
        public static final String AWAIT_CHECKOUT = "AWAIT_CHECKOUT";

        public static final String ITEM_COMPLETE = "ITEM_COMPLETE";
        public static final String ITEM_PROCESSING = "ITEM_PROCESSING";

    }

    public static class BillStatus {
        public static final String PAID = "PAID";
        public static final String AWAIT_PAYMENT = "AWAIT_PAYMENT";
        public static final String CANCELLED = "CANCELLED";

    }

    public static class Notification {
        public static final String RECEIVE_NOTIFICATION = "RECEIVE_NOTIFICATION";
        public static final String CHECKOUTORDER = "CHECKOUTORDER";
        public static final String NEWORDER = "NEWORDER";
        public static final String OUTOFSTOCK = "OUTOFSTOCK";
        public static final String UPDATEORDER = "UPDATEORDER";
        public static final String COMPLETEORDER = "COMPLETEORDER";
        public static final String CUSORDER = "CUSORDER";

    }

    public static String[] CUSTOMER_PERMISSION = { "CUSTOMER_ORDER" };

    public static class NotificationType {
        public static final String ORDER = "ORDER";
        public static final String OUTOFSTOCK = "OUTOFSTOCK";
        public static final String CUSTOMER_SALE = "CUSTOMER_SALE";
        public static final String ORDER_NO_ACTION = "ORDER_NO_ACTION";

    }

}
