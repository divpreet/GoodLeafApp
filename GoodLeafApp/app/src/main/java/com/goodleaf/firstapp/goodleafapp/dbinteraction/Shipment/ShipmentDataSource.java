package com.goodleaf.firstapp.goodleafapp.dbinteraction.Shipment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.goodleaf.firstapp.goodleafapp.dbinteraction.MySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class ShipmentDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.SHIPMENT_NO, MySQLiteHelper.SHIPMENT_DATE, MySQLiteHelper.ORDER_NO,
            MySQLiteHelper.PRODUCT_NO, MySQLiteHelper.QUANTITY,
            MySQLiteHelper.SHIPMENT_EVALUATION, MySQLiteHelper.MISC_CHARGES};

    public ShipmentDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Shipment createShipment(String shipmentNo, String shipmentDate, String orderNo,
                                   String productNo, int quantity,
                                   String shipmentEvaluation, String miscCharges) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.SHIPMENT_NO, shipmentNo);
        values.put(MySQLiteHelper.SHIPMENT_DATE,shipmentDate);
        values.put(MySQLiteHelper.ORDER_NO, orderNo);
        values.put(MySQLiteHelper.PRODUCT_NO, productNo);
        values.put(MySQLiteHelper.QUANTITY, quantity);
        values.put(MySQLiteHelper.SHIPMENT_EVALUATION, shipmentEvaluation);
        values.put(MySQLiteHelper.MISC_CHARGES, miscCharges);
        database.insert(MySQLiteHelper.TABLE_SHIPMENTDETAILS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SHIPMENTDETAILS,
                allColumns, MySQLiteHelper.SHIPMENT_NO + " = " + shipmentNo, null,
                null, null, null);
        cursor.moveToFirst();
        Shipment newShipment = cursorToShipment(cursor);
        cursor.close();
        return newShipment;
    }
    public Shipment updateShipment(String shipmentNo, String shipmentDate, String orderNo,
                                   String productNo, int quantity,
                                   String shipmentEvaluation, String miscCharges) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.SHIPMENT_NO, shipmentNo);
        values.put(MySQLiteHelper.SHIPMENT_DATE,shipmentDate);
        values.put(MySQLiteHelper.ORDER_NO, orderNo);
        values.put(MySQLiteHelper.PRODUCT_NO, productNo);
        values.put(MySQLiteHelper.QUANTITY, quantity);
        values.put(MySQLiteHelper.SHIPMENT_EVALUATION, shipmentEvaluation);
        values.put(MySQLiteHelper.MISC_CHARGES, miscCharges);
        database.update(MySQLiteHelper.TABLE_SHIPMENTDETAILS, values,
                MySQLiteHelper.SHIPMENT_NO + " = " + shipmentNo, null);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SHIPMENTDETAILS,
                allColumns, MySQLiteHelper.SHIPMENT_NO + " = " + shipmentNo, null,
                null, null, null);
        cursor.moveToFirst();
        Shipment newShipment = cursorToShipment(cursor);
        cursor.close();
        return newShipment;
    }

    public void deleteShipment(Shipment shipment) {
        String id = shipment.getShipmentNo();
        System.out.println("Shipment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_SHIPMENTDETAILS, MySQLiteHelper.SHIPMENT_NO
                + " = " + id, null);
    }

    public void deleteAllShipments() {
        database.delete(MySQLiteHelper.TABLE_SHIPMENTDETAILS, null, null);
    }

    public List<Shipment> getAllShipments() {
        List<Shipment> shipments = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_SHIPMENTDETAILS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Shipment shipment = cursorToShipment(cursor);
            shipments.add(shipment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return shipments;
    }

    public List<Shipment> getShipments(String shipmentNo,String orderNo, String shipmentDate, String productNo) {
        List<Shipment> shipments = new ArrayList<>();
        Cursor cursor;
        if (shipmentNo.length() > 0) {
            cursor = database.query(MySQLiteHelper.TABLE_SHIPMENTDETAILS,
                    allColumns, MySQLiteHelper.SHIPMENT_NO
                            + " = " + shipmentNo, null, null, null, null);
        }else{
            String query = "";
            if(orderNo.length() > 0){
                query += MySQLiteHelper.ORDER_NO + "=" + orderNo;
            }
            if(shipmentDate.length() > 0){
                if(query.length() > 0){
                    query += "OR ";
                }
                query += MySQLiteHelper.SHIPMENT_DATE + " LIKE '%" + shipmentDate + "%'";
            }
            if(productNo.length() > 0){
                if(query.length() > 0){
                    query += "OR ";
                }
                query += MySQLiteHelper.PRODUCT_NO + "=" + productNo;
            }
            cursor = database.query(MySQLiteHelper.TABLE_ORDERS,
                    allColumns, query,
                    null, null, null, null, null);
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Shipment shipment = cursorToShipment(cursor);
            shipments.add(shipment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return shipments;
    }

    private Shipment cursorToShipment(Cursor cursor) {
        Shipment shipment = new Shipment();
        shipment.setShipmentNo(cursor.getString(0));
        shipment.setShipmentDate(cursor.getString(1));
        shipment.setOrderNo(cursor.getString(2));
        shipment.setProductNo(cursor.getString(3));
        shipment.setQuantity(cursor.getInt(4));
        shipment.setShipmentEvaluation(cursor.getString(5));
        shipment.setMiscCharges(cursor.getString(6));
        return shipment;
    }
}