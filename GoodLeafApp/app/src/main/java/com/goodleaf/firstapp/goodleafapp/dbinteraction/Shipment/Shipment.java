package com.goodleaf.firstapp.goodleafapp.dbinteraction.Shipment;

public class Shipment {
    private String shipmentNo;
    private String shipmentDate;
    private String orderNo;
    private String productNo;
    private int quantity;
    private String shipmentEvaluation;
    private String miscCharges;

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    public String getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(String shipmentDate) {
        this.shipmentDate = shipmentDate;
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

    public String getShipmentEvaluation() {
        return shipmentEvaluation;
    }

    public void setShipmentEvaluation(String shipmentEvaluation) {
        this.shipmentEvaluation = shipmentEvaluation;
    }

    public String getMiscCharges() {
        return miscCharges;
    }

    public void setMiscCharges(String miscCharges) {
        this.miscCharges = miscCharges;
    }
}
