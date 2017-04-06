package com.goodleaf.firstapp.goodleafapp.dbinteraction.Transaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.goodleaf.firstapp.goodleafapp.dbinteraction.MySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class TransactionDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.TRANSACTION_NO, MySQLiteHelper.TRANSACTION_DATE,
            MySQLiteHelper.TRANSACTION_DESC, MySQLiteHelper.CUSTOMER_NO,
            MySQLiteHelper.SUPPLIER_NO, MySQLiteHelper.TRANSACTION_TYPE,
            MySQLiteHelper.DEBIT,MySQLiteHelper.CREDIT};

    public TransactionDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Transaction createTransaction(String transactionNo, String transactionDate,
                                      String transactionDesc, String customerNo,
                                      String supplierNo, String transactionType, String debit, String credit) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.TRANSACTION_NO, transactionNo);
        values.put(MySQLiteHelper.TRANSACTION_DATE, transactionDate);
        values.put(MySQLiteHelper.TRANSACTION_DESC, transactionDesc);
        values.put(MySQLiteHelper.CUSTOMER_NO, customerNo);
        values.put(MySQLiteHelper.SUPPLIER_NO, supplierNo);
        values.put(MySQLiteHelper.TRANSACTION_TYPE, transactionType);
        values.put(MySQLiteHelper.DEBIT, debit);
        values.put(MySQLiteHelper.CREDIT, credit);
        database.insert(MySQLiteHelper.TABLE_TRANSACTIONS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_TRANSACTIONS,
                allColumns, MySQLiteHelper.TRANSACTION_NO + " = " + transactionNo, null,
                null, null, null);
        cursor.moveToFirst();
        Transaction newTransaction = cursorToTransaction(cursor);
        cursor.close();
        return newTransaction;
    } 
    
    public Transaction updateTransaction(String transactionNo, String transactionDate,
                                         String transactionDesc, String customerNo,
                                         String supplierNo, String transactionType, String debit, String credit) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.TRANSACTION_NO, transactionNo);
        values.put(MySQLiteHelper.TRANSACTION_DATE, transactionDate);
        values.put(MySQLiteHelper.TRANSACTION_DESC, transactionDesc);
        values.put(MySQLiteHelper.CUSTOMER_NO, customerNo);
        values.put(MySQLiteHelper.SUPPLIER_NO, supplierNo);
        values.put(MySQLiteHelper.TRANSACTION_TYPE, transactionType);
        values.put(MySQLiteHelper.DEBIT, debit);
        values.put(MySQLiteHelper.CREDIT, credit);
        database.update(MySQLiteHelper.TABLE_TRANSACTIONS, values,
                MySQLiteHelper.TRANSACTION_NO + " = " + transactionNo, null);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_TRANSACTIONS,
                allColumns, MySQLiteHelper.TRANSACTION_NO + " = " + transactionNo, null,
                null, null, null);
        cursor.moveToFirst();
        Transaction newTransaction = cursorToTransaction(cursor);
        cursor.close();
        return newTransaction;
    }

    public void deleteTransaction(Transaction transaction) {
        String id = transaction.getTransactionNo();
        System.out.println("Transaction deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_TRANSACTIONS, MySQLiteHelper.TRANSACTION_NO
                + " = " + id, null);
    }

    public void deleteAllTransactions() {
        database.delete(MySQLiteHelper.TABLE_TRANSACTIONS, null, null);
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_TRANSACTIONS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Transaction transaction = cursorToTransaction(cursor);
            transactions.add(transaction);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return transactions;
    }

    private Transaction cursorToTransaction(Cursor cursor) {
        Transaction transaction = new Transaction();
        transaction.setTransactionNo(cursor.getString(0));
        transaction.setTransactionDate(cursor.getString(1));
        transaction.setTransactionDesc(cursor.getString(2));
        transaction.setCustomerNo(cursor.getString(3));
        transaction.setSupplierNo(cursor.getString(4));
        transaction.setTransactionType(cursor.getString(5));
        transaction.setDebit(cursor.getString(6));
        transaction.setCredit(cursor.getString(7));
        return transaction;
    }

    public List<Transaction> getTransactions(String transactionNo, String transactionCustomerNo, String transactionSupplierNo, String transactionType ,String transactionDate){
        List<Transaction> transactions = new ArrayList<>();

        Cursor cursor;
        if (transactionNo.length() > 0) {
            cursor = database.query(MySQLiteHelper.TABLE_TRANSACTIONS,
                    allColumns, MySQLiteHelper.TRANSACTION_NO + "=" + transactionNo,
                    null, null, null, null, null);
        } else {
            String query = "";
            if(transactionCustomerNo.length() > 0){
                query += MySQLiteHelper.CUSTOMER_NO + "=" + transactionCustomerNo;
            }
            if(transactionSupplierNo.length() > 0){
                query += MySQLiteHelper.SUPPLIER_NO + "=" + transactionSupplierNo;
            }
            if(transactionDate.length() > 0){
                if(query.length() > 0){
                    query += "OR ";
                }
                query += MySQLiteHelper.TRANSACTION_DATE + " LIKE '%" + transactionDate + "%'";
            }
            if(transactionType.length() > 0){
                if(query.length() > 0){
                    query += "OR ";
                }
                query += MySQLiteHelper.TRANSACTION_TYPE + " LIKE '%" + transactionType + "%'";
            }
            cursor = database.query(MySQLiteHelper.TABLE_TRANSACTIONS,
                    allColumns, query,
                    null, null, null, null, null);
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Transaction transaction = cursorToTransaction(cursor);
            transactions.add(transaction);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return transactions;
    }
}