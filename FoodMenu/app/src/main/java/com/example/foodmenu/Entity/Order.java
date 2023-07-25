package com.example.foodmenu.Entity;

public class Order {
    public static final String CODE = "OD";
    public static final String WAITING_STATUS_CODE = "WAITING";
    public static final String DOING_STATUS_CODE = "DOING";
    public static final String DONE_STATUS_CODE = "DONE";

    private String order_id, customer_id;

    public Order(String order_id, String customer_name){
        this.order_id = order_id;
        this.customer_id = customer_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
}
