package com.goodleaf.firstapp.goodleafapp.product;

import android.widget.Spinner;
import android.widget.TextView;

import com.goodleaf.firstapp.goodleafapp.dbinteraction.Order.OrderDetails;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Purchase.PurchaseDetails;

public class ProductQuantity{
    public ProductQuantity(Spinner orderProductDescription, TextView quantity){
        this.quantity = quantity;
        this.orderProductDescription = orderProductDescription;
    }
    private TextView quantity;
    private Spinner orderProductDescription;
    private Spinner purchaseProductDescription;
    private OrderDetails orderDetails;
    private PurchaseDetails purchaseDetails;
    private String productNo;
    private int prodQuantity;

    public int getProdQuantity() {
        return prodQuantity;
    }

    public void setProdQuantity(int prodQuantity) {
        this.prodQuantity = prodQuantity;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public Spinner getPurchaseProductDescription() {
        return purchaseProductDescription;
    }

    public void setPurchaseProductDescription(Spinner purchaseProductDescription) {
        this.purchaseProductDescription = purchaseProductDescription;
    }

    public PurchaseDetails getPurchaseDetails() {
        return purchaseDetails;
    }

    public void setPurchaseDetails(PurchaseDetails purchaseDetails) {
        this.purchaseDetails = purchaseDetails;
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }


    public TextView getQuantity() {
        return quantity;
    }

    public void setQuantity(TextView quantity) {
        this.quantity = quantity;
    }

    public Spinner getOrderProductDescription() {
        return orderProductDescription;
    }

    public void setOrderProductDescription(Spinner orderProductDescription) {
        this.orderProductDescription = orderProductDescription;
    }
}
