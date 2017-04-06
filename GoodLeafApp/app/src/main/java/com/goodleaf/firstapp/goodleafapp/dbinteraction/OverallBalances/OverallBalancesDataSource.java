package com.goodleaf.firstapp.goodleafapp.dbinteraction.OverallBalances;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.goodleaf.firstapp.goodleafapp.dbinteraction.MySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class OverallBalancesDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.CUSTOMER_NO,
            MySQLiteHelper.SUPPLIER_NO, MySQLiteHelper.BALANCE};

    public OverallBalancesDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public OverallBalances createOverallBalance(String customerNo,
                                         String supplierNo, String balance) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.CUSTOMER_NO, customerNo);
        values.put(MySQLiteHelper.SUPPLIER_NO, supplierNo);
        values.put(MySQLiteHelper.BALANCE, balance);
        database.insert(MySQLiteHelper.TABLE_OVERALLBALANCES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_OVERALLBALANCES,
                allColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        OverallBalances newOverallBalance = cursorToOverallBalances(cursor);
        cursor.close();
        return newOverallBalance;
    }

    public OverallBalances updateOverallBalance(String customerNo,
                                 String supplierNo, String balance) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.CUSTOMER_NO, customerNo);
        values.put(MySQLiteHelper.SUPPLIER_NO, supplierNo);
        values.put(MySQLiteHelper.BALANCE, balance);
        if(customerNo == null) {
            database.update(MySQLiteHelper.TABLE_OVERALLBALANCES, values,
                    MySQLiteHelper.SUPPLIER_NO + " = " + supplierNo, null);
            Cursor cursor = database.query(MySQLiteHelper.TABLE_OVERALLBALANCES,
                    allColumns, MySQLiteHelper.SUPPLIER_NO + " = " + supplierNo, null,
                    null, null, null);

            cursor.moveToFirst();
            OverallBalances newOverallBalance = cursorToOverallBalances(cursor);
            cursor.close();
            return newOverallBalance;
        }else{
            database.update(MySQLiteHelper.TABLE_OVERALLBALANCES, values,
                    MySQLiteHelper.CUSTOMER_NO + " = " + customerNo, null);
            Cursor cursor = database.query(MySQLiteHelper.TABLE_OVERALLBALANCES,
                    allColumns, MySQLiteHelper.CUSTOMER_NO + " = " + customerNo, null,
                    null, null, null);

            cursor.moveToFirst();
            OverallBalances newOverallBalance = cursorToOverallBalances(cursor);
            cursor.close();
            return newOverallBalance;
        }
    }

    public void deleteOverallBalance(String customerNo, String supplierNo) {
        if(customerNo == null) {
            database.delete(MySQLiteHelper.TABLE_OVERALLBALANCES, MySQLiteHelper.SUPPLIER_NO
                    + " = " + supplierNo, null);
        }else{
            database.delete(MySQLiteHelper.TABLE_OVERALLBALANCES, MySQLiteHelper.CUSTOMER_NO
                    + " = " + customerNo, null);
        }
    }

    public OverallBalances getAllOverallBalancesForSupplier(String supplierNo) {
        OverallBalances overallBalances = new OverallBalances();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_OVERALLBALANCES,
                allColumns, MySQLiteHelper.SUPPLIER_NO + " = " + supplierNo, null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            overallBalances = cursorToOverallBalances(cursor);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return overallBalances;
    }

    public OverallBalances getAllOverallBalancesForCustomer(String customerNo) {
        OverallBalances overallBalances = new OverallBalances();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_OVERALLBALANCES,
                allColumns, MySQLiteHelper.CUSTOMER_NO + " = " + customerNo, null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            overallBalances = cursorToOverallBalances(cursor);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return overallBalances;
    }

    public List<OverallBalances> getAllOverallBalances() {
        List<OverallBalances> overallBalances = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_OVERALLBALANCES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OverallBalances overallBalance = cursorToOverallBalances(cursor);
            overallBalances.add(overallBalance);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return overallBalances;
    }

    private OverallBalances cursorToOverallBalances(Cursor cursor) {
        OverallBalances overallBalance = new OverallBalances();
        overallBalance.setCustomerNo(cursor.getString(0));
        overallBalance.setSupplierNo(cursor.getString(1));
        overallBalance.setBalance(cursor.getString(2));
        return overallBalance;
    }
}
