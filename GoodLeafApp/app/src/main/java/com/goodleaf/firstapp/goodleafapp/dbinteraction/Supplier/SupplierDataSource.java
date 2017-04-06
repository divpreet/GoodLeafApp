package com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.goodleaf.firstapp.goodleafapp.dbinteraction.MySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class SupplierDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.SUPPLIER_NO, MySQLiteHelper.SUPPLIER_NAME,
            MySQLiteHelper.BUSINESS_NAME_SUPP, MySQLiteHelper.SUPPLIER_ADDRESS,
            MySQLiteHelper.SUPPLIER_CONTACT, MySQLiteHelper.SUPPLIER_ACCOUNT_NUMBER,
            MySQLiteHelper.SUPPLIER_ACCOUNT_NAME, MySQLiteHelper.SUPPLIER_BANK_NAME,
            MySQLiteHelper.SUPPLIER_BANK_CODE};

    public SupplierDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Supplier createSupplier(String supplierNo, String supplierName,
                                   String businessName, String supplierAddress,
                                   String supplierContact, String supplierAccountNumber,
                                   String supplierAccountName, String supplierBankName,
                                   String supplierBankCode) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.SUPPLIER_NO, supplierNo);
        values.put(MySQLiteHelper.SUPPLIER_NAME, supplierName);
        values.put(MySQLiteHelper.BUSINESS_NAME, businessName);
        values.put(MySQLiteHelper.SUPPLIER_ADDRESS, supplierAddress);
        values.put(MySQLiteHelper.SUPPLIER_CONTACT, supplierContact);
        values.put(MySQLiteHelper.SUPPLIER_ACCOUNT_NUMBER, supplierAccountNumber);
        values.put(MySQLiteHelper.SUPPLIER_ACCOUNT_NAME, supplierAccountName);
        values.put(MySQLiteHelper.SUPPLIER_BANK_NAME, supplierBankName);
        values.put(MySQLiteHelper.SUPPLIER_BANK_CODE, supplierBankCode);
        database.insert(MySQLiteHelper.TABLE_SUPPLIER, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SUPPLIER,
                allColumns, MySQLiteHelper.SUPPLIER_NO + " = " + supplierNo, null,
                null, null, null);
        cursor.moveToFirst();
        Supplier newSupplier = cursorToSupplier(cursor);
        cursor.close();
        return newSupplier;
    }

    public void deleteSupplier(Supplier supplier) {
        String id = supplier.getSupplierNo();
        System.out.println("Supplier deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_SUPPLIER, MySQLiteHelper.SUPPLIER_NO
                + " = " + id, null);
    }
    public void deleteAllSuppliers() {
        database.delete(MySQLiteHelper.TABLE_SUPPLIER, null, null);
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_SUPPLIER,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Supplier supplier = cursorToSupplier(cursor);
            suppliers.add(supplier);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return suppliers;
    }

    public List<Supplier> getSuppliers(String supplierNo, String supplierName) {
        List<Supplier> suppliers = new ArrayList<>();

        Cursor cursor;
        if (supplierNo.length() > 0) {
            cursor = database.query(MySQLiteHelper.TABLE_SUPPLIER,
                    allColumns, MySQLiteHelper.SUPPLIER_NO + "=" + supplierNo,
                    null, null, null, null, null);
        } else {
            cursor = database.query(MySQLiteHelper.TABLE_SUPPLIER,
                    allColumns, MySQLiteHelper.SUPPLIER_NAME + " LIKE '%" + supplierName + "%'",
                    null, null, null, null, null);
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Supplier supplier = cursorToSupplier(cursor);
            suppliers.add(supplier);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return suppliers;
    }


    private Supplier cursorToSupplier(Cursor cursor) {
        Supplier supplier = new Supplier();
        supplier.setSupplierNo(cursor.getString(0));
        supplier.setSupplierName(cursor.getString(1));
        supplier.setBusinessName(cursor.getString(2));
        supplier.setSupplierAddress(cursor.getString(3));
        supplier.setSupplierContact(cursor.getString(4));
        supplier.setSupplierAccountNumber(cursor.getString(5));
        supplier.setSupplierAccountName(cursor.getString(6));
        supplier.setSupplierBankName(cursor.getString(7));
        supplier.setSupplierBankCode(cursor.getString(8));
        return supplier;
    }

    public Supplier updateSupplier(String supplierNo, String supplierName,
                                   String businessName, String supplierAddress,
                                   String supplierContact, String supplierAccountNumber,
                                   String supplierAccountName, String supplierBankName,
                                   String supplierBankCode) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.SUPPLIER_NO, supplierNo);
        values.put(MySQLiteHelper.SUPPLIER_NAME, supplierName);
        values.put(MySQLiteHelper.BUSINESS_NAME, businessName);
        values.put(MySQLiteHelper.SUPPLIER_ADDRESS, supplierAddress);
        values.put(MySQLiteHelper.SUPPLIER_CONTACT, supplierContact);
        values.put(MySQLiteHelper.SUPPLIER_ACCOUNT_NUMBER, supplierAccountNumber);
        values.put(MySQLiteHelper.SUPPLIER_ACCOUNT_NAME, supplierAccountName);
        values.put(MySQLiteHelper.SUPPLIER_BANK_NAME, supplierBankName);
        values.put(MySQLiteHelper.SUPPLIER_BANK_CODE, supplierBankCode);
        database.update(MySQLiteHelper.TABLE_SUPPLIER, values,
                MySQLiteHelper.SUPPLIER_NO + " = " + supplierNo, null);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SUPPLIER,
                allColumns, MySQLiteHelper.SUPPLIER_NO + " = " + supplierNo, null,
                null, null, null);
        cursor.moveToFirst();
        Supplier newSupplier = cursorToSupplier(cursor);
        cursor.close();
        return newSupplier;
    }
}