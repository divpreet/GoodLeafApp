package com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.goodleaf.firstapp.goodleafapp.dbinteraction.MySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class CustomerDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.CUSTOMER_NO, MySQLiteHelper.CUSTOMER_NAME,
            MySQLiteHelper.BUSINESS_NAME, MySQLiteHelper.CUSTOMER_ADDRESS,
            MySQLiteHelper.CUSTOMER_CONTACT, MySQLiteHelper.CUSTOMER_ADDRESS1,
            MySQLiteHelper.CUSTOMER_CONTACT1};

    public CustomerDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Customer createCustomer(String customerNo, String customerName,
                                   String businessName, String customerAddress,
                                   String customerContact, String customerAddress1, String customerContact1) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.CUSTOMER_NO, customerNo);
        values.put(MySQLiteHelper.CUSTOMER_NAME, customerName);
        values.put(MySQLiteHelper.BUSINESS_NAME, businessName);
        values.put(MySQLiteHelper.CUSTOMER_ADDRESS, customerAddress);
        values.put(MySQLiteHelper.CUSTOMER_CONTACT, customerContact);
        values.put(MySQLiteHelper.CUSTOMER_ADDRESS1, customerAddress1);
        values.put(MySQLiteHelper.CUSTOMER_CONTACT1, customerContact1);
        database.insert(MySQLiteHelper.TABLE_CUSTOMER, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CUSTOMER,
                allColumns, MySQLiteHelper.CUSTOMER_NO + " = " + customerNo, null,
                null, null, null);
        cursor.moveToFirst();
        Customer newCustomer = cursorToCustomer(cursor);
        cursor.close();
        return newCustomer;
    }

    public void deleteCustomer(Customer customer) {
        String id = customer.getCustomerNo();
        System.out.println("Customer deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_CUSTOMER, MySQLiteHelper.CUSTOMER_NO
                + " = " + id, null);
    }
    public void deleteAllCustomers() {
        database.delete(MySQLiteHelper.TABLE_CUSTOMER, null, null);
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_CUSTOMER,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Customer customer = cursorToCustomer(cursor);
            customers.add(customer);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return customers;
    }

    public List<Customer> getCustomers(String customerNo, String customerName) {
        List<Customer> customers = new ArrayList<>();

        Cursor cursor;
        if (customerNo.length() > 0) {
            cursor = database.query(MySQLiteHelper.TABLE_CUSTOMER,
                    allColumns, MySQLiteHelper.CUSTOMER_NO + "=" + customerNo,
                    null, null, null, null, null);
        } else {
            cursor = database.query(MySQLiteHelper.TABLE_CUSTOMER,
                    allColumns, MySQLiteHelper.CUSTOMER_NAME + " LIKE '%" + customerName + "%'",
                    null, null, null, null, null);
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Customer customer = cursorToCustomer(cursor);
            customers.add(customer);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return customers;
    }

    private Customer cursorToCustomer(Cursor cursor) {
        Customer customer = new Customer();
        customer.setCustomerNo(cursor.getString(0));
        customer.setCustomerName(cursor.getString(1));
        customer.setBusinessName(cursor.getString(2));
        customer.setCustomerAddress(cursor.getString(3));
        customer.setCustomerContact(cursor.getString(4));
        customer.setCustomerAddress1(cursor.getString(5));
        customer.setCustomerContact1(cursor.getString(6));
        return customer;
    }

    public Customer updateCustomer(String customerNo, String customerName,
                                   String businessName, String customerAddress,
                                   String customerContact, String customerAddress1, String customerContact1) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.CUSTOMER_NO, customerNo);
        values.put(MySQLiteHelper.CUSTOMER_NAME, customerName);
        values.put(MySQLiteHelper.BUSINESS_NAME, businessName);
        values.put(MySQLiteHelper.CUSTOMER_ADDRESS, customerAddress);
        values.put(MySQLiteHelper.CUSTOMER_CONTACT, customerContact);
        values.put(MySQLiteHelper.CUSTOMER_ADDRESS1, customerAddress1);
        values.put(MySQLiteHelper.CUSTOMER_CONTACT1, customerContact1);
        database.update(MySQLiteHelper.TABLE_CUSTOMER, values,
                MySQLiteHelper.CUSTOMER_NO + " = " + customerNo,null);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CUSTOMER,
                allColumns, MySQLiteHelper.CUSTOMER_NO + " = " + customerNo, null,
                null, null, null);
        cursor.moveToFirst();
        Customer newCustomer = cursorToCustomer(cursor);
        cursor.close();
        return newCustomer;
    }
}