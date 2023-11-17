package com.fanstatic.config.constants;

public class ApplicationConst {
    public static final String CLIENT_HOST = "https://fantastic-ui-chi.vercel.app";

    public static final int CUSTOMER_ROLE_ID = 3;
    public static final int ADNIN_ROLE_ID = 1;
    public static final int CASHIER_ROLE_ID = 2;

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

}
