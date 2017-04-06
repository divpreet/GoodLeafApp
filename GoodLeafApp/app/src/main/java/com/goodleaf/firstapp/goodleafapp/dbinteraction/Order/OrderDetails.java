package com.goodleaf.firstapp.goodleafapp.dbinteraction.Order;

public class OrderDetails {
    private String orderDetailsNo;
    private String orderNo;
    private String productNo;
    private int quantity;
    private String price;
    private int quantityPending;

    public String getOrderDetailsNo() {
        return orderDetailsNo;
    }

    public void setOrderDetailsNo(String orderDetailsNo) {
        this.orderDetailsNo = orderDetailsNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantityPending() {
        return quantityPending;
    }

    public void setQuantityPending(int quantityPending) {
        this.quantityPending = quantityPending;
    }
}
