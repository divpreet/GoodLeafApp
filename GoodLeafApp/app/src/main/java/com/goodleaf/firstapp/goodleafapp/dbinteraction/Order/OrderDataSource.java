package com.goodleaf.firstapp.goodleafapp.dbinteraction.Order;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.goodleaf.firstapp.goodleafapp.dbinteraction.MySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumnsForOrderDetails = {MySQLiteHelper.ORDERDETAILS_NO, MySQLiteHelper.ORDER_NO,
            MySQLiteHelper.PRODUCT_NO, MySQLiteHelper.QUANTITY, MySQLiteHelper.PRICE,
            MySQLiteHelper.QUANTITY_PENDING};

    private String[] allColumnsForOrder = {MySQLiteHelper.ORDER_NO, MySQLiteHelper.ORDER_DATE,
            MySQLiteHelper.ORDER_PRICE,
            MySQLiteHelper.CUSTOMER_NO, MySQLiteHelper.STATUS};

    public OrderDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Order createOrder(String orderNo, String orderDate, String orderPrice,
                                String customerNo, String status) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.ORDER_NO, orderNo);
        values.put(MySQLiteHelper.ORDER_DATE, orderDate);
        values.put(MySQLiteHelper.ORDER_PRICE, orderPrice);
        values.put(MySQLiteHelper.CUSTOMER_NO, customerNo);
        values.put(MySQLiteHelper.STATUS, status);
        database.insert(MySQLiteHelper.TABLE_ORDERS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_ORDERS,
                allColumnsForOrder, MySQLiteHelper.ORDER_NO + " = " + orderNo, null,
                null, null, null);
        cursor.moveToFirst();
        Order newOrder = cursorToOrder(cursor);
        cursor.close();
        return newOrder;
    }

    public OrderDetails createOrderDetails(String orderDetailsNo, String orderNo,
                             String productNo, int quantity,
                             String price, int quantityPending) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.ORDERDETAILS_NO, orderDetailsNo);
        values.put(MySQLiteHelper.ORDER_NO, orderNo);
        values.put(MySQLiteHelper.PRODUCT_NO, productNo);
        values.put(MySQLiteHelper.QUANTITY, quantity);
        values.put(MySQLiteHelper.PRICE, price);
        values.put(MySQLiteHelper.QUANTITY_PENDING, quantityPending);
        database.insert(MySQLiteHelper.TABLE_ORDERDETAILS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_ORDERDETAILS,
                allColumnsForOrderDetails, MySQLiteHelper.ORDERDETAILS_NO + " = " + orderDetailsNo, null,
                null, null, null);
        cursor.moveToFirst();
        OrderDetails newOrderDetails = cursorToOrderDetails(cursor);
        cursor.close();
        return newOrderDetails;
    }
    public void deleteOrder(Order order) {
        String id = order.getOrderNo();
        System.out.println("Order deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_ORDERDETAILS, MySQLiteHelper.ORDER_NO
                + " = " + id, null);
        database.delete(MySQLiteHelper.TABLE_ORDERS, MySQLiteHelper.ORDER_NO
                + " = " + id, null);
    }
    public void deleteAllOrders() {
        database.delete(MySQLiteHelper.TABLE_ORDERDETAILS, null, null);
        database.delete(MySQLiteHelper.TABLE_ORDERS, null, null);
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ORDERS,
                allColumnsForOrder, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Order order = cursorToOrder(cursor);
            orders.add(order);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return orders;
    }

    public Order updateOrder(String orderNo, String orderDate, String orderPrice,
                             String customerNo, String status) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.ORDER_NO, orderNo);
        values.put(MySQLiteHelper.ORDER_DATE, orderDate);
        values.put(MySQLiteHelper.ORDER_PRICE, orderPrice);
        values.put(MySQLiteHelper.CUSTOMER_NO, customerNo);
        values.put(MySQLiteHelper.STATUS, status);
        database.update(MySQLiteHelper.TABLE_ORDERS, values,
                MySQLiteHelper.ORDER_NO + " = " + orderNo, null);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_ORDERS,
                allColumnsForOrder, MySQLiteHelper.ORDER_NO + " = " + orderNo, null,
                null, null, null);
        cursor.moveToFirst();
        Order newOrder = cursorToOrder(cursor);
        cursor.close();
        return newOrder;
    }

    public void updateOrderStatus(String orderNo){
        List<Order> orderList = getOrders(orderNo,null,null,null);
        if(orderList.size() == 1) {
            Order currentOrder = orderList.get(0);
            List<OrderDetails> allOrderDetails = getAllOrderDetailsForOrder(orderNo);
            String status = "Completed";
            for (int i = 0; i < allOrderDetails.size(); i++) {
                if (allOrderDetails.get(i).getQuantityPending() > 0) {
                    status = "Pending";
                    break;
                }
            }
            updateOrder(currentOrder.getOrderNo(),currentOrder.getOrderDate(),currentOrder.getOrderPrice(),currentOrder.getCustomerNo(),status);
        }
    }

    public OrderDetails updateOrderDetails(String orderDetailsNo, String orderNo,
                                    String productNo, int quantity,
                                    String price, int quantityPending) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.ORDERDETAILS_NO, orderDetailsNo);
        values.put(MySQLiteHelper.ORDER_NO, orderNo);
        values.put(MySQLiteHelper.PRODUCT_NO, productNo);
        values.put(MySQLiteHelper.QUANTITY, quantity);
        values.put(MySQLiteHelper.PRICE, price);
        values.put(MySQLiteHelper.QUANTITY_PENDING, quantityPending);
        database.update(MySQLiteHelper.TABLE_ORDERDETAILS, values,
                MySQLiteHelper.ORDERDETAILS_NO + " = " + orderDetailsNo, null);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_ORDERDETAILS,
                allColumnsForOrderDetails, MySQLiteHelper.ORDERDETAILS_NO + " = " + orderDetailsNo, null,
                null, null, null);
        cursor.moveToFirst();
        OrderDetails newOrderDetails = cursorToOrderDetails(cursor);
        cursor.close();
        return newOrderDetails;
    }

    public List<OrderDetails> getAllOrderDetails() {
        List<OrderDetails> orderDetails = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ORDERDETAILS,
                allColumnsForOrderDetails, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OrderDetails orderDetail = cursorToOrderDetails(cursor);
            orderDetails.add(orderDetail);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return orderDetails;
    }

    public List<OrderDetails> getAllOrderDetailsForOrder(String orderNo) {
        List<OrderDetails> orderDetails = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ORDERDETAILS,
                allColumnsForOrderDetails, MySQLiteHelper.ORDER_NO + "=" + orderNo,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OrderDetails orderDetail = cursorToOrderDetails(cursor);
            orderDetails.add(orderDetail);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return orderDetails;
    }

    private Order cursorToOrder(Cursor cursor) {
        Order order = new Order();
        order.setOrderNo(cursor.getString(0));
        order.setOrderDate(cursor.getString(1));
        order.setOrderPrice(cursor.getString(2));
        order.setCustomerNo(cursor.getString(3));
        order.setStatus(cursor.getString(4));
        return order;
    }

    private OrderDetails cursorToOrderDetails(Cursor cursor) {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderDetailsNo(cursor.getString(0));
        orderDetails.setOrderNo(cursor.getString(1));
        orderDetails.setProductNo(cursor.getString(2));
        orderDetails.setQuantity(cursor.getInt(3));
        orderDetails.setPrice(cursor.getString(4));
        orderDetails.setQuantityPending(cursor.getInt(5));
        return orderDetails;
    }
    
    public List<Order> getOrders(String orderNo, String orderCustomerNo, String orderDate, String orderStatus){
        List<Order> orders = new ArrayList<>();

        Cursor cursor;
        if (orderNo.length() > 0) {
            cursor = database.query(MySQLiteHelper.TABLE_ORDERS,
                    allColumnsForOrder, MySQLiteHelper.ORDER_NO + "=" + orderNo,
                    null, null, null, null, null);
        } else {
            String query = "";
            if(orderCustomerNo.length() > 0){
                query += MySQLiteHelper.CUSTOMER_NO + "=" + orderCustomerNo;
            }
            if(orderDate.length() > 0){
                if(query.length() > 0){
                    query += "OR ";
                }
                query += MySQLiteHelper.ORDER_DATE + " LIKE '%" + orderDate + "%'";
            }
            if(orderStatus.length() > 0){
                if(query.length() > 0){
                    query += "OR ";
                }
                query += MySQLiteHelper.STATUS + " LIKE '%" + orderStatus + "%'";
            }
            cursor = database.query(MySQLiteHelper.TABLE_ORDERS,
                    allColumnsForOrder, query,
                    null, null, null, null, null);
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Order order = cursorToOrder(cursor);
            orders.add(order);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return orders;
    }
}