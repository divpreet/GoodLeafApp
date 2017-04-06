package com.goodleaf.firstapp.goodleafapp.dbinteraction.Purchase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.goodleaf.firstapp.goodleafapp.dbinteraction.MySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class PurchaseDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumnsForPurchaseDetails = {MySQLiteHelper.PURCHASEDETAILS_NO, MySQLiteHelper.PURCHASE_NO,
            MySQLiteHelper.PRODUCT_NO, MySQLiteHelper.QUANTITY, MySQLiteHelper.PRICE,
            MySQLiteHelper.QUANTITY_PENDING};

    private String[] allColumnsForPurchase = {MySQLiteHelper.PURCHASE_NO, MySQLiteHelper.PURCHASE_DATE,
            MySQLiteHelper.PURCHASE_PRICE,
            MySQLiteHelper.SUPPLIER_NO, MySQLiteHelper.STATUS};

    public PurchaseDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Purchase createPurchase(String purchaseNo, String purchaseDate, String purchasePrice,
                             String supplierNo, String status) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.PURCHASE_NO, purchaseNo);
        values.put(MySQLiteHelper.PURCHASE_DATE, purchaseDate);
        values.put(MySQLiteHelper.PURCHASE_PRICE, purchasePrice);
        values.put(MySQLiteHelper.SUPPLIER_NO, supplierNo);
        values.put(MySQLiteHelper.STATUS, status);
        database.insert(MySQLiteHelper.TABLE_PURCHASE, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PURCHASE,
                allColumnsForPurchase, MySQLiteHelper.PURCHASE_NO + " = " + purchaseNo, null,
                null, null, null);
        cursor.moveToFirst();
        Purchase newPurchase = cursorToPurchase(cursor);
        cursor.close();
        return newPurchase;
    }

    public PurchaseDetails createPurchaseDetails(String purchaseDetailsNo, String purchaseNo,
                                           String productNo, int quantity,
                                           String price, int quantityPending) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.PURCHASEDETAILS_NO, purchaseDetailsNo);
        values.put(MySQLiteHelper.PURCHASE_NO, purchaseNo);
        values.put(MySQLiteHelper.PRODUCT_NO, productNo);
        values.put(MySQLiteHelper.QUANTITY, quantity);
        values.put(MySQLiteHelper.PRICE, price);
        values.put(MySQLiteHelper.QUANTITY_PENDING, quantityPending);
        database.insert(MySQLiteHelper.TABLE_PURCHASEDETAILS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PURCHASEDETAILS,
                allColumnsForPurchaseDetails, MySQLiteHelper.PURCHASEDETAILS_NO + " = " + purchaseDetailsNo, null,
                null, null, null);
        cursor.moveToFirst();
        PurchaseDetails newPurchaseDetails = cursorToPurchaseDetails(cursor);
        cursor.close();
        return newPurchaseDetails;
    }
    public void deletePurchase(Purchase purchase) {
        String id = purchase.getPurchaseNo();
        System.out.println("Purchase deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_PURCHASEDETAILS, MySQLiteHelper.PURCHASE_NO
                + " = " + id, null);
        database.delete(MySQLiteHelper.TABLE_PURCHASE, MySQLiteHelper.PURCHASE_NO
                + " = " + id, null);
    }
    public void deleteAllPurchases() {
        database.delete(MySQLiteHelper.TABLE_PURCHASEDETAILS, null, null);
        database.delete(MySQLiteHelper.TABLE_PURCHASE, null, null);
    }

    public List<Purchase> getAllPurchases() {
        List<Purchase> purchases = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PURCHASE,
                allColumnsForPurchase, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Purchase purchase = cursorToPurchase(cursor);
            purchases.add(purchase);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return purchases;
    }

    public Purchase updatePurchase(String purchaseNo, String purchaseDate, String purchasePrice,
                             String supplierNo, String status) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.PURCHASE_NO, purchaseNo);
        values.put(MySQLiteHelper.PURCHASE_DATE, purchaseDate);
        values.put(MySQLiteHelper.PURCHASE_PRICE, purchasePrice);
        values.put(MySQLiteHelper.SUPPLIER_NO, supplierNo);
        values.put(MySQLiteHelper.STATUS, status);
        database.update(MySQLiteHelper.TABLE_PURCHASE, values,
                MySQLiteHelper.PURCHASE_NO + " = " + purchaseNo, null);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PURCHASE,
                allColumnsForPurchase, MySQLiteHelper.PURCHASE_NO + " = " + purchaseNo, null,
                null, null, null);
        cursor.moveToFirst();
        Purchase newPurchase = cursorToPurchase(cursor);
        cursor.close();
        return newPurchase;
    }

    public PurchaseDetails updatePurchaseDetails(String purchaseDetailsNo, String purchaseNo,
                                           String productNo, int quantity,
                                           String price, int quantityPending) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.PURCHASEDETAILS_NO, purchaseDetailsNo);
        values.put(MySQLiteHelper.PURCHASE_NO, purchaseNo);
        values.put(MySQLiteHelper.PRODUCT_NO, productNo);
        values.put(MySQLiteHelper.QUANTITY, quantity);
        values.put(MySQLiteHelper.PRICE, price);
        values.put(MySQLiteHelper.QUANTITY_PENDING, quantityPending);
        database.update(MySQLiteHelper.TABLE_PURCHASEDETAILS, values,
                MySQLiteHelper.PURCHASEDETAILS_NO + " = " + purchaseDetailsNo, null);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PURCHASEDETAILS,
                allColumnsForPurchaseDetails, MySQLiteHelper.PURCHASEDETAILS_NO + " = " + purchaseDetailsNo, null,
                null, null, null);
        cursor.moveToFirst();
        PurchaseDetails newPurchaseDetails = cursorToPurchaseDetails(cursor);
        cursor.close();
        return newPurchaseDetails;
    }

    public List<PurchaseDetails> getAllPurchaseDetails() {
        List<PurchaseDetails> purchaseDetails = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PURCHASEDETAILS,
                allColumnsForPurchaseDetails, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PurchaseDetails purchaseDetail = cursorToPurchaseDetails(cursor);
            purchaseDetails.add(purchaseDetail);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return purchaseDetails;
    }

    public List<PurchaseDetails> getAllPurchaseDetailsForPurchase(String purchaseNo) {
        List<PurchaseDetails> purchaseDetails = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PURCHASEDETAILS,
                allColumnsForPurchaseDetails, MySQLiteHelper.PURCHASE_NO + "=" + purchaseNo,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PurchaseDetails purchaseDetail = cursorToPurchaseDetails(cursor);
            purchaseDetails.add(purchaseDetail);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return purchaseDetails;
    }

    private Purchase cursorToPurchase(Cursor cursor) {
        Purchase purchase = new Purchase();
        purchase.setPurchaseNo(cursor.getString(0));
        purchase.setPurchaseDate(cursor.getString(1));
        purchase.setPurchasePrice(cursor.getString(2));
        purchase.setSupplierNo(cursor.getString(3));
        purchase.setStatus(cursor.getString(4));
        return purchase;
    }

    private PurchaseDetails cursorToPurchaseDetails(Cursor cursor) {
        PurchaseDetails purchaseDetails = new PurchaseDetails();
        purchaseDetails.setPurchaseDetailsNo(cursor.getString(0));
        purchaseDetails.setPurchaseNo(cursor.getString(1));
        purchaseDetails.setProductNo(cursor.getString(2));
        purchaseDetails.setQuantity(cursor.getInt(3));
        purchaseDetails.setPrice(cursor.getString(4));
        purchaseDetails.setQuantityPending(cursor.getInt(5));
        return purchaseDetails;
    }

    public List<Purchase> getPurchases(String purchaseNo, String purchaseSupplierNo, String purchaseDate, String purchaseStatus){
        List<Purchase> purchases = new ArrayList<>();

        Cursor cursor;
        if (purchaseNo.length() > 0) {
            cursor = database.query(MySQLiteHelper.TABLE_PURCHASE,
                    allColumnsForPurchase, MySQLiteHelper.PURCHASE_NO + "=" + purchaseNo,
                    null, null, null, null, null);
        } else {
            String query = "";
            if(purchaseSupplierNo.length() > 0){
                query += MySQLiteHelper.SUPPLIER_NO + "=" + purchaseSupplierNo;
            }
            if(purchaseDate.length() > 0){
                if(query.length() > 0){
                    query += "OR ";
                }
                query += MySQLiteHelper.PURCHASE_DATE + " LIKE '%" + purchaseDate + "%'";
            }
            if(purchaseStatus.length() > 0){
                if(query.length() > 0){
                    query += "OR ";
                }
                query += MySQLiteHelper.STATUS + " LIKE '%" + purchaseStatus + "%'";
            }
            cursor = database.query(MySQLiteHelper.TABLE_PURCHASE,
                    allColumnsForPurchase, query,
                    null, null, null, null, null);
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Purchase purchase = cursorToPurchase(cursor);
            purchases.add(purchase);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return purchases;
    }
}