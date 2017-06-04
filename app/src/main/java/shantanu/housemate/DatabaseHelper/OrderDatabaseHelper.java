package shantanu.housemate.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import shantanu.housemate.Data.OrderData;

/**
 * Created by SHAAN on 04-02-17.
 */
public class OrderDatabaseHelper {

    public static final String KEY_ROWID = "id";
    public static final String PAYMENT_TYPE = "payment_type";
    public static final String BILL = "bill";
    public static final String DOD = "selected";
    public static final String CUST_USERNAME = "cust_username";
    public static final String RETAIL_USERNAME = "retail_username";

    private static final String TABLE_NAME = "ORDERDATA";
    private static final String DATABASE_TABLE = "order_database";
    private static final int DATABASE_VERSION = 1;

    private DbHelper orderHelper;
    private final Context orderContext;
    private SQLiteDatabase orderDatabase;

    public OrderDatabaseHelper(Context c) {
        this.orderContext = c;
    }

    public OrderDatabaseHelper open() throws SQLException {
        orderHelper = new DbHelper(orderContext);
        try {
            orderDatabase = orderHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.e("SQL ERROR", e.getMessage());
        }
        return this;
    }

    public void close() {
        orderHelper.close();
    }

    public void putData(OrderData orderData) {
        ContentValues cv = new ContentValues();
        cv.put(PAYMENT_TYPE, orderData.getPaymentType());
        cv.put(BILL, orderData.getBill());
        cv.put(DOD, orderData.getDod());
        cv.put(CUST_USERNAME, orderData.getCustomerUsername());
        cv.put(RETAIL_USERNAME, orderData.getRetailerUsername());
        orderDatabase.insert(TABLE_NAME, null, cv);
    }

    public Cursor getInfo() {
        String[] columns = new String[]{KEY_ROWID, PAYMENT_TYPE, BILL, DOD, CUST_USERNAME, RETAIL_USERNAME};
        Cursor cursor = orderDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public OrderData getOrderDataOf(String customerUsername) {
        String[] columns = new String[]{KEY_ROWID, PAYMENT_TYPE, BILL, DOD, CUST_USERNAME, RETAIL_USERNAME};
        Cursor cursor = orderDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new OrderData();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iPaymentType = cursor.getColumnIndex(PAYMENT_TYPE);
        int iBill = cursor.getColumnIndex(BILL);
        int iDod = cursor.getColumnIndex(DOD);
        int iCustomerUsername = cursor.getColumnIndex(CUST_USERNAME);
        int iRetailerUsername = cursor.getColumnIndex(RETAIL_USERNAME);

        OrderData orderData = new OrderData();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(iCustomerUsername).equals(customerUsername)) {
                orderData.setPaymentType(cursor.getString(iPaymentType));
                orderData.setBill(cursor.getString(iBill));
                orderData.setDod(cursor.getString(iDod));
                orderData.setCustomerUsername(cursor.getString(iCustomerUsername));
                orderData.setRetailerUsername(cursor.getString(iRetailerUsername));
            }
        }

        return orderData;
    }

    public OrderData getOrderDataOfRetailer(String retailerUsername) {
        String[] columns = new String[]{KEY_ROWID, PAYMENT_TYPE, BILL, DOD, CUST_USERNAME, RETAIL_USERNAME};
        Cursor cursor = orderDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new OrderData();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iPaymentType = cursor.getColumnIndex(PAYMENT_TYPE);
        int iBill = cursor.getColumnIndex(BILL);
        int iDod = cursor.getColumnIndex(DOD);
        int iCustomerUsername = cursor.getColumnIndex(CUST_USERNAME);
        int iRetailerUsername = cursor.getColumnIndex(RETAIL_USERNAME);

        OrderData orderData = new OrderData();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(iRetailerUsername).equals(retailerUsername)) {
                orderData.setPaymentType(cursor.getString(iPaymentType));
                orderData.setBill(cursor.getString(iBill));
                orderData.setDod(cursor.getString(iDod));
                orderData.setCustomerUsername(cursor.getString(iCustomerUsername));
                orderData.setRetailerUsername(cursor.getString(iRetailerUsername));
            }
        }

        return orderData;
    }

    public ArrayList<OrderData> getDodDataOfCustomer(String cust_username) {
        String[] columns = new String[]{KEY_ROWID, PAYMENT_TYPE, BILL, DOD, CUST_USERNAME, RETAIL_USERNAME};
        Cursor cursor = orderDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new ArrayList<OrderData>();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iPaymentType = cursor.getColumnIndex(PAYMENT_TYPE);
        int iBill = cursor.getColumnIndex(BILL);
        int iDod = cursor.getColumnIndex(DOD);
        int iCustomerUsername = cursor.getColumnIndex(CUST_USERNAME);
        int iRetailerUsername = cursor.getColumnIndex(RETAIL_USERNAME);

        ArrayList<OrderData> orderDatas = new ArrayList<>();
        OrderData orderData;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if ((cursor.getString(iCustomerUsername).equals(cust_username)) &&
                    Boolean.parseBoolean(cursor.getString(iDod))) {
                orderData = new OrderData();
                orderData.setPaymentType(cursor.getString(iPaymentType));
                orderData.setBill(cursor.getString(iBill));
                orderData.setDod(cursor.getString(iDod));
                orderData.setCustomerUsername(cursor.getString(iCustomerUsername));
                orderData.setRetailerUsername(cursor.getString(iRetailerUsername));
                orderDatas.add(orderData);
            }
        }
        return orderDatas;
    }

    public ArrayList<OrderData> getUnselectedDataOfCustomer(String cust_username) {
        String[] columns = new String[]{KEY_ROWID, PAYMENT_TYPE, BILL, DOD, CUST_USERNAME, RETAIL_USERNAME};
        Cursor cursor = orderDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new ArrayList<OrderData>();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iPaymentType = cursor.getColumnIndex(PAYMENT_TYPE);
        int iBill = cursor.getColumnIndex(BILL);
        int iDod = cursor.getColumnIndex(DOD);
        int iCustomerUsername = cursor.getColumnIndex(CUST_USERNAME);
        int iRetailerUsername = cursor.getColumnIndex(RETAIL_USERNAME);

        ArrayList<OrderData> orderDatas = new ArrayList<>();
        OrderData orderData;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (((cursor.getString(iCustomerUsername).equals(cust_username)) ||
                    (cursor.getString(iCustomerUsername).equals("empty"))) &&
                    !(Boolean.parseBoolean(cursor.getString(iDod)))) {
                orderData = new OrderData();
                orderData.setPaymentType(cursor.getString(iPaymentType));
                orderData.setBill(cursor.getString(iBill));
                orderData.setDod(cursor.getString(iDod));
                orderData.setCustomerUsername(cursor.getString(iCustomerUsername));
                orderData.setRetailerUsername(cursor.getString(iRetailerUsername));
                orderDatas.add(orderData);
            }
        }
        return orderDatas;
    }

    public ArrayList<OrderData> getDataOfCustomer(String cust_username) {
        String[] columns = new String[]{KEY_ROWID, PAYMENT_TYPE, BILL, DOD, CUST_USERNAME, RETAIL_USERNAME};
        Cursor cursor = orderDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new ArrayList<OrderData>();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iPaymentType = cursor.getColumnIndex(PAYMENT_TYPE);
        int iBill = cursor.getColumnIndex(BILL);
        int iDod = cursor.getColumnIndex(DOD);
        int iCustomerUsername = cursor.getColumnIndex(CUST_USERNAME);
        int iRetailerUsername = cursor.getColumnIndex(RETAIL_USERNAME);

        ArrayList<OrderData> orderDatas = new ArrayList<>();
        OrderData orderData;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if ((cursor.getString(iCustomerUsername).equals(cust_username))) {
                orderData = new OrderData();
                orderData.setPaymentType(cursor.getString(iPaymentType));
                orderData.setBill(cursor.getString(iBill));
                orderData.setDod(cursor.getString(iDod));
                orderData.setCustomerUsername(cursor.getString(iCustomerUsername));
                orderData.setRetailerUsername(cursor.getString(iRetailerUsername));
                orderDatas.add(orderData);
            }
        }
        return orderDatas;
    }

    public ArrayList<OrderData> getAllData() {
        String[] columns = new String[]{KEY_ROWID, PAYMENT_TYPE, BILL, DOD, CUST_USERNAME, RETAIL_USERNAME};
        Cursor cursor = orderDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new ArrayList<OrderData>();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iPaymentType = cursor.getColumnIndex(PAYMENT_TYPE);
        int iBill = cursor.getColumnIndex(BILL);
        int iDod = cursor.getColumnIndex(DOD);
        int iCustomerUsername = cursor.getColumnIndex(CUST_USERNAME);
        int iRetailerUsername = cursor.getColumnIndex(RETAIL_USERNAME);

        ArrayList<OrderData> orderDatas = new ArrayList<>();
        OrderData orderData;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            orderData = new OrderData();
            orderData.setPaymentType(cursor.getString(iPaymentType));
            orderData.setBill(cursor.getString(iBill));
            orderData.setDod(cursor.getString(iDod));
            orderData.setCustomerUsername(cursor.getString(iCustomerUsername));
            orderData.setRetailerUsername(cursor.getString(iRetailerUsername));
            orderDatas.add(orderData);
        }
        return orderDatas;
    }

    public void deleteData(String customerUsername) {
        String DELETE = "DELETE FROM " + TABLE_NAME +
                " WHERE " + CUST_USERNAME + " = " + "'" + customerUsername + "';";
        orderDatabase.execSQL(DELETE);
    }

    public void clearDatabase() {
        String AUTOINCREMENT_RESET = "DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                TABLE_NAME + "';";
        orderDatabase.delete(TABLE_NAME, null, null);
        orderDatabase.execSQL(AUTOINCREMENT_RESET);
    }

    public void deleteInfo(String itemName) {
        String DELETE = "DELETE FROM " + TABLE_NAME +
                " WHERE " + PAYMENT_TYPE + " = " + "'" + itemName + "';";
        orderDatabase.execSQL(DELETE);
    }

    public OrderData retrieveData(String itemName) {
        String[] columns = new String[]{KEY_ROWID, PAYMENT_TYPE, BILL, DOD, CUST_USERNAME, RETAIL_USERNAME};
        Cursor cursor = orderDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new OrderData();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iPaymentType = cursor.getColumnIndex(PAYMENT_TYPE);
        int iBill = cursor.getColumnIndex(BILL);
        int iDod = cursor.getColumnIndex(DOD);
        int iCustomerUsername = cursor.getColumnIndex(CUST_USERNAME);
        int iRetailerUsername = cursor.getColumnIndex(RETAIL_USERNAME);

        OrderData orderData = new OrderData();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(iPaymentType).equals(itemName)) {
                orderData.setPaymentType(cursor.getString(iPaymentType));
                orderData.setBill(cursor.getString(iBill));
                orderData.setDod(cursor.getString(iDod));
                orderData.setCustomerUsername(cursor.getString(iCustomerUsername));
                orderData.setRetailerUsername(cursor.getString(iRetailerUsername));
            }
        }
        return orderData;
    }

    public void editData(OrderData orderData) {
        ContentValues cv = new ContentValues();
        cv.put(PAYMENT_TYPE, orderData.getPaymentType());
        cv.put(BILL, orderData.getBill());
        cv.put(DOD, orderData.getDod());
        cv.put(CUST_USERNAME, orderData.getCustomerUsername());
        cv.put(RETAIL_USERNAME, orderData.getRetailerUsername());
        orderDatabase.insert(TABLE_NAME, null, cv);
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, TABLE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PAYMENT_TYPE + " VARCHAR2(20), " +
                    BILL + " VARCHAR2(10), " +
                    DOD + " VARCHAR2(10), " +
                    CUST_USERNAME + " VARCHAR2(20) NOT NULL, " +
                    RETAIL_USERNAME + " VARCHAR2(20));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE + ";");
            onCreate(db);
        }
    }

}