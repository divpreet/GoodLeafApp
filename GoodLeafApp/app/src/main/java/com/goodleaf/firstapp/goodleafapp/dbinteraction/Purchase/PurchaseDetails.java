package com.goodleaf.firstapp.goodleafapp.dbinteraction.Purchase;

public class PurchaseDetails {
    private String purchaseDetailsNo;
    private String purchaseNo;
    private String productNo;
    private int quantity;
    private String price;
    private int quantityPending;

    public String getPurchaseDetailsNo() {
        return purchaseDetailsNo;
    }

    public void setPurchaseDetailsNo(String purchaseDetailsNo) {
        this.purchaseDetailsNo = purchaseDetailsNo;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
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
