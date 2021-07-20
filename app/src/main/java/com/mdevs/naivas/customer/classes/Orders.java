package com.mdevs.naivas.customer.classes;

public class Orders {
    String orderId, orderNo, mpesaCode, cust_id, dateTime, amountPaid, orderStatus;

    public Orders(String orderId, String orderNo, String mpesaCode, String cust_id, String dateTime, String amountPaid, String orderStatus) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.mpesaCode = mpesaCode;
        this.cust_id = cust_id;
        this.dateTime = dateTime;
        this.amountPaid = amountPaid;
        this.orderStatus = orderStatus;
    }

    public Orders(String orderNo, String mpesaCode, String dateTime, String amountPaid, String orderStatus) {
        this.orderNo = orderNo;
        this.mpesaCode = mpesaCode;
        this.dateTime = dateTime;
        this.amountPaid = amountPaid;
        this.orderStatus = orderStatus;
    }

    public Orders(String orderNo, String mpesaCode, String cust_id, String dateTime, String amountPaid, String orderStatus) {
        this.orderNo = orderNo;
        this.mpesaCode = mpesaCode;
        this.cust_id = cust_id;
        this.dateTime = dateTime;
        this.amountPaid = amountPaid;
        this.orderStatus = orderStatus;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getMpesaCode() {
        return mpesaCode;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCust_id() {
        return cust_id;
    }
}