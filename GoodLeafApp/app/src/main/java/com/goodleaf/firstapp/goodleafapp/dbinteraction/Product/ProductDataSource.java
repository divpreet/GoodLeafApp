package com.goodleaf.firstapp.goodleafapp.dbinteraction.Product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.goodleaf.firstapp.goodleafapp.dbinteraction.MySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class ProductDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.PRODUCT_NO, MySQLiteHelper.PRODUCT_DESCRIPTION,
            MySQLiteHelper.PRODUCT_PRICE, MySQLiteHelper.PRODUCT_TYPE};

    public ProductDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Product createProduct(String productNo, String productDescription,
                                 String productPrice, String productType) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.PRODUCT_NO, productNo);
        values.put(MySQLiteHelper.PRODUCT_DESCRIPTION, productDescription);
        values.put(MySQLiteHelper.PRODUCT_PRICE, productPrice);
        values.put(MySQLiteHelper.PRODUCT_TYPE, productType);
        database.insert(MySQLiteHelper.TABLE_PRODUCTS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PRODUCTS,
                allColumns, MySQLiteHelper.PRODUCT_NO + " = " + productNo, null,
                null, null, null);
        cursor.moveToFirst();
        Product newProduct = cursorToProduct(cursor);
        cursor.close();
        return newProduct;
    }

    public void deleteProduct(Product product) {
        String id = product.getProductNo();
        System.out.println("Product deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_PRODUCTS, MySQLiteHelper.PRODUCT_NO
                + " = " + id, null);
    }

    public void deleteAllProducts() {
        database.delete(MySQLiteHelper.TABLE_PRODUCTS, null, null);
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PRODUCTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product product = cursorToProduct(cursor);
            products.add(product);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return products;
    }

    public List<Product> getProducts(String productNo, String productDescription) {
        List<Product> products = new ArrayList<>();

        Cursor cursor;
        if (productNo.length() > 0) {
            cursor = database.query(MySQLiteHelper.TABLE_PRODUCTS,
                    allColumns, MySQLiteHelper.PRODUCT_NO + "=" + productNo,
                    null, null, null, null, null);
        } else {
            cursor = database.query(MySQLiteHelper.TABLE_PRODUCTS,
                    allColumns, MySQLiteHelper.PRODUCT_DESCRIPTION + " LIKE '%" + productDescription + "%'",
                    null, null, null, null, null);
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product product = cursorToProduct(cursor);
            products.add(product);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return products;
    }

    private Product cursorToProduct(Cursor cursor) {
        Product product = new Product();
        product.setProductNo(cursor.getString(0));
        product.setProductDescription(cursor.getString(1));
        product.setProductPrice(cursor.getString(2));
        product.setProductType(cursor.getString(3));
        return product;
    }


    public Product updateProduct(String productNo, String productDescription,
                                 String productPrice, String productType) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.PRODUCT_NO, productNo);
        values.put(MySQLiteHelper.PRODUCT_DESCRIPTION, productDescription);
        values.put(MySQLiteHelper.PRODUCT_PRICE, productPrice);
        values.put(MySQLiteHelper.PRODUCT_TYPE, productType);
        database.update(MySQLiteHelper.TABLE_PRODUCTS, values,
                MySQLiteHelper.PRODUCT_NO + " = " + productNo, null);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PRODUCTS,
                allColumns, MySQLiteHelper.PRODUCT_NO + " = " + productNo, null,
                null, null, null);
        cursor.moveToFirst();
        Product newProduct = cursorToProduct(cursor);
        cursor.close();
        return newProduct;
    }
}