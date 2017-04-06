package com.goodleaf.firstapp.goodleafapp.dbinteraction;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_CUSTOMER = "Customer";
    public static final String CUSTOMER_NO = "customerNo";
    public static final String CUSTOMER_NAME = "customerName";
    public static final String BUSINESS_NAME = "businessName";
    public static final String CUSTOMER_ADDRESS = "customerAddress";
    public static final String CUSTOMER_CONTACT = "customerContact";
    public static final String CUSTOMER_ADDRESS1 = "customerAddress1";
    public static final String CUSTOMER_CONTACT1 = "customerContact1";
    public static final String TABLE_SUPPLIER = "Supplier";
    public static final String SUPPLIER_NO = "supplierNo";
    public static final String SUPPLIER_NAME = "supplierName";
    public static final String BUSINESS_NAME_SUPP = "businessName";
    public static final String SUPPLIER_ADDRESS = "supplierAddress";
    public static final String SUPPLIER_CONTACT = "supplierContact";
    public static final String SUPPLIER_ACCOUNT_NUMBER = "supplierAccountNumber";
    public static final String SUPPLIER_ACCOUNT_NAME = "supplierAccountName";
    public static final String SUPPLIER_BANK_NAME = "supplierBankName";
    public static final String SUPPLIER_BANK_CODE = "supplierBankCode";
    public static final String TABLE_PRODUCTS = "Products";
    public static final String PRODUCT_NO = "productNo";
    public static final String PRODUCT_DESCRIPTION = "productDescription";
    public static final String PRODUCT_PRICE = "productPrice";
    public static final String PRODUCT_TYPE = "productType";
    public static final String TABLE_ORDERDETAILS = "OrderDetails";
    public static final String ORDERDETAILS_NO = "orderDetailNo";
    public static final String ORDER_NO = "orderNo";
    public static final String QUANTITY = "quantity";
    public static final String PRICE = "price";
    public static final String QUANTITY_PENDING = "quantityPending";
    public static final String TABLE_ORDERS = "Orders";
    public static final String ORDER_DATE = "orderDate";
    public static final String ORDER_PRICE = "orderPrice";
    public static final String STATUS = "status";
    public static final String PURCHASEDETAILS_NO = "purchaseDetailNo";
    public static final String TABLE_PURCHASEDETAILS = "PurchaseDetails";
    public static final String PURCHASE_NO = "purchaseNo";
    public static final String PURCHASE_DATE = "purchaseDate";
    public static final String PURCHASE_PRICE = "purchasePrice";
    public static final String TABLE_PURCHASE = "Purchase";
    public static final String TABLE_SHIPMENTDETAILS = "ShipmentDetails";
    public static final String TABLE_TRANSACTIONS = "Transactions";
    public static final String TABLE_OVERALLBALANCES = "OverallBalances";
    public static final String TRANSACTION_NO = "transactionNo";
    public static final String TRANSACTION_DATE = "transactionDate";
    public static final String TRANSACTION_DESC = "transactionDesc";
    public static final String TRANSACTION_TYPE = "transactionType";
    public static final String DEBIT = "debit";
    public static final String CREDIT = "credit";
    public static final String BALANCE = "balance";
    public static final String SHIPMENT_NO = "shipmentNo";
    public static final String SHIPMENT_DATE = "shipmentDate";
    public static final String SHIPMENT_EVALUATION = "shipmentEvaluation";
    public static final String MISC_CHARGES = "miscCharges";
    private static final String DATABASE_NAME = "goodleaf.db";
    private static final int DATABASE_VERSION = 1;
    // Database creation sql statement
    private static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + TABLE_CUSTOMER +
            "(" + CUSTOMER_NO + " VARCHAR(20) NOT NULL PRIMARY KEY, " + CUSTOMER_NAME +
            " VARCHAR(50), " + BUSINESS_NAME + " VARCHAR(50), " + CUSTOMER_ADDRESS +
            " VARCHAR(100), " + CUSTOMER_CONTACT + " VARCHAR(20)," +
            CUSTOMER_ADDRESS1 + " VARCHAR(100), " + CUSTOMER_CONTACT1 + " VARCHAR(20));";

    private static final String CREATE_SUPPLIER_TABLE = "CREATE TABLE " + TABLE_SUPPLIER +
            "(" + SUPPLIER_NO + " VARCHAR(20) NOT NULL PRIMARY KEY, " + SUPPLIER_NAME +
            " VARCHAR(50), " + BUSINESS_NAME_SUPP + " VARCHAR(50), " + SUPPLIER_ADDRESS +
            " VARCHAR(100), " + SUPPLIER_CONTACT + " VARCHAR(20), " +
            SUPPLIER_ACCOUNT_NUMBER + " VARCHAR(20), " + SUPPLIER_ACCOUNT_NAME + " VARCHAR(50), " +
            SUPPLIER_BANK_NAME + " VARCHAR(20), " + SUPPLIER_BANK_CODE + " VARCHAR(20));";

    private static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS +
            "(" + PRODUCT_NO + " VARCHAR(20) NOT NULL PRIMARY KEY, " + PRODUCT_DESCRIPTION +
            " VARCHAR(100), " + PRODUCT_PRICE + " VARCHAR(10), " + PRODUCT_TYPE + " VARCHAR(40));";

    private static final String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS +
            "(" + ORDER_NO + " VARCHAR(20) NOT NULL PRIMARY KEY, " + ORDER_DATE + " DATE, " +
            ORDER_PRICE + " VARCHAR(10), " +
            CUSTOMER_NO + " VARCHAR(20), " + STATUS + " VARCHAR(10)" +
            ", FOREIGN KEY (" + CUSTOMER_NO + ") REFERENCES " + TABLE_CUSTOMER + "(" + CUSTOMER_NO + "));";

    private static final String CREATE_ORDERDETAILS_TABLE = "CREATE TABLE " + TABLE_ORDERDETAILS +
            "(" + ORDERDETAILS_NO + " VARCHAR(20) NOT NULL PRIMARY KEY, " + ORDER_NO +
            " VARCHAR(20) NOT NULL, "+ PRODUCT_NO +" VARCHAR(20), " + QUANTITY + " int, "+
            PRICE + " VARCHAR(20), " + QUANTITY_PENDING +" VARCHAR(20),  " +
            "FOREIGN KEY (" + ORDER_NO + ") REFERENCES " + TABLE_ORDERS + "(" + ORDER_NO + ")," +
            "FOREIGN KEY (" + PRODUCT_NO + ") REFERENCES " + TABLE_PRODUCTS + "(" + PRODUCT_NO + "));";

    private static final String CREATE_PURCHASEDETAILS_TABLE = "CREATE TABLE " + TABLE_PURCHASEDETAILS +
            "("+ PURCHASEDETAILS_NO + " VARCHAR(20) NOT NULL PRIMARY KEY, " + PURCHASE_NO +
            " VARCHAR(20) NOT NULL, " + PRODUCT_NO + " VARCHAR(20), " +
            QUANTITY_PENDING + " int, " + PRICE + " VARCHAR(20), " +
            "FOREIGN KEY (" + PURCHASE_NO + ") REFERENCES " + TABLE_PURCHASE + "(" + PURCHASE_NO + ")," +
            "FOREIGN KEY (" + PRODUCT_NO + ") REFERENCES " + TABLE_PRODUCTS + "("+ PRODUCT_NO + "));";

    private static final String CREATE_PURCHASE_TABLE = "CREATE TABLE " + TABLE_PURCHASE +
            "("+ PURCHASE_NO + " VARCHAR(20) NOT NULL PRIMARY KEY, " + PURCHASE_DATE + " DATE, " +
            PURCHASEDETAILS_NO + " VARCHAR(20), " + PURCHASE_PRICE + " VARCHAR(10), " +
            SUPPLIER_NO + " VARCHAR(20), " + STATUS + " VARCHAR(10)," +
            "FOREIGN KEY (" + SUPPLIER_NO + ") REFERENCES " + TABLE_SUPPLIER + "(" + SUPPLIER_NO + "));";

    private static final String CREATE_SHIPMENTDETAILS_TABLE = "CREATE TABLE " + TABLE_SHIPMENTDETAILS +
            "("+ SHIPMENT_NO + " VARCHAR(20) NOT NULL PRIMARY KEY, "+ SHIPMENT_DATE + " DATE, " + ORDER_NO + " VARCHAR(20), " +
            PRODUCT_NO + " VARCHAR(20), " + QUANTITY +" int, " + SHIPMENT_EVALUATION + " VARCHAR(20), " +
            MISC_CHARGES + " VARCHAR(20), FOREIGN KEY ("+ ORDER_NO + ") REFERENCES " + TABLE_ORDERS +"("+ ORDER_NO + "), " +
            "FOREIGN KEY (" + PRODUCT_NO + ") REFERENCES " + TABLE_PRODUCTS + "(" + PRODUCT_NO + "));";

    private static final String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS +
            "("+ TRANSACTION_NO +" VARCHAR(20) NOT NULL PRIMARY KEY, " + TRANSACTION_DATE + " DATE, " +
            TRANSACTION_DESC + " VARCHAR(100), " + CUSTOMER_NO + " VARCHAR(20), " + SUPPLIER_NO + " VARCHAR(20), " +
            TRANSACTION_TYPE + " VARCHAR(20) CHECK (" + TRANSACTION_TYPE + " in " +
            "('Cash', 'Cheque', 'Draft', 'Online')), " + DEBIT + " VARCHAR(20), " +
            CREDIT + " VARCHAR(20), FOREIGN KEY (" + CUSTOMER_NO + ") REFERENCES " + TABLE_CUSTOMER + "(" + CUSTOMER_NO + "), " +
            "FOREIGN KEY ("+ SUPPLIER_NO + ") REFERENCES " + TABLE_SUPPLIER + "(" + SUPPLIER_NO + "));";

    private static final String CREATE_OVERALLBALANCES_TABLE = "CREATE TABLE " + TABLE_OVERALLBALANCES +
            "(" + CUSTOMER_NO + " VARCHAR(20), " + SUPPLIER_NO + " VARCHAR(20), " + BALANCE + " VARCHAR(20), " +
            "FOREIGN KEY (" + CUSTOMER_NO + ") REFERENCES " + TABLE_CUSTOMER + "(" + CUSTOMER_NO + "), " +
            "FOREIGN KEY (" + SUPPLIER_NO + ") REFERENCES " + TABLE_SUPPLIER + "(" + SUPPLIER_NO + "));";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_CUSTOMER_TABLE);
        database.execSQL(CREATE_SUPPLIER_TABLE);
        database.execSQL(CREATE_PRODUCTS_TABLE);
        database.execSQL(CREATE_ORDERS_TABLE);
        database.execSQL(CREATE_ORDERDETAILS_TABLE);
        database.execSQL(CREATE_PURCHASE_TABLE);
        database.execSQL(CREATE_PURCHASEDETAILS_TABLE);
        database.execSQL(CREATE_SHIPMENTDETAILS_TABLE);
        database.execSQL(CREATE_TRANSACTIONS_TABLE);
        database.execSQL(CREATE_OVERALLBALANCES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OVERALLBALANCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIPMENTDETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASEDETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERDETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPLIER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        onCreate(db);
    }
}
