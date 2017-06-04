package shantanu.housemate.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import shantanu.housemate.Data.RetailerData;

/**
 * Created by SHAAN on 04-02-17.
 */
public class RetailerDatabaseHelper {

    public static final String KEY_ROWID = "id";
    public static final String SHOPNAME = "shopname";
    public static final String EMAIL = "email";
    public static final String PHONE_NO = "phone_no";
    public static final String ADDRESS = "address";
    public static final String CITY = "city";
    public static final String PINCODE = "pincode";
    public static final String ROS = "ros";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private static final String TABLE_NAME = "RETAILER";
    private static final String DATABASE_TABLE = "retailer_database";
    private static final int DATABASE_VERSION = 1;

    private DbHelper retailHelper;
    private final Context retailContext;
    private SQLiteDatabase retailDatabase;

    public RetailerDatabaseHelper(Context c) {
        this.retailContext = c;
    }

    public RetailerDatabaseHelper open() throws SQLException {
        retailHelper = new DbHelper(retailContext);
        retailDatabase = retailHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        retailHelper.close();
    }


    public void putData(RetailerData retailerData) {
        ContentValues cv = new ContentValues();
        cv.put(SHOPNAME, retailerData.getShopName());
        cv.put(EMAIL, retailerData.getEmail());
        cv.put(PHONE_NO, retailerData.getPhoneNo());
        cv.put(ADDRESS, retailerData.getAddress());
        cv.put(CITY, retailerData.getCity());
        cv.put(PINCODE, retailerData.getPinCode());
        cv.put(ROS, retailerData.getRadiusOfService());
        cv.put(USERNAME, retailerData.getUsername());
        cv.put(PASSWORD, retailerData.getPassword());
        retailDatabase.insert(TABLE_NAME, null, cv);
    }

    public Cursor getInfo() {
        String[] columns = new String[]{KEY_ROWID, SHOPNAME, EMAIL, PHONE_NO, ADDRESS, CITY,
                PINCODE, ROS, USERNAME, PASSWORD};
        Cursor cursor = retailDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public RetailerData getData(String usernameToEdit) {
        String[] columns = new String[]{KEY_ROWID, SHOPNAME, EMAIL, PHONE_NO,
                ADDRESS, CITY, PINCODE, ROS, USERNAME, PASSWORD};
        Cursor cursor = retailDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new RetailerData();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iShopName = cursor.getColumnIndex(SHOPNAME);
        int iEmail = cursor.getColumnIndex(EMAIL);
        int iPhoneNo = cursor.getColumnIndex(PHONE_NO);
        int iAddress = cursor.getColumnIndex(ADDRESS);
        int iCity = cursor.getColumnIndex(CITY);
        int iPincode = cursor.getColumnIndex(PINCODE);
        int iRos = cursor.getColumnIndex(ROS);
        int iUsername = cursor.getColumnIndex(USERNAME);
        int iPassword = cursor.getColumnIndex(PASSWORD);

        RetailerData retailerData = new RetailerData();
        retailerData.setUsername("empty");
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(iUsername).equals(usernameToEdit)) {
                retailerData.setShopName(cursor.getString(iShopName));
                retailerData.setEmail(cursor.getString(iEmail));
                retailerData.setPhoneNo(cursor.getString(iPhoneNo));
                retailerData.setAddress(cursor.getString(iAddress));
                retailerData.setCity(cursor.getString(iCity));
                retailerData.setPinCode(cursor.getString(iPincode));
                retailerData.setRadiusOfService(cursor.getInt(iRos));
                retailerData.setUsername(cursor.getString(iUsername));
                retailerData.setPassword(cursor.getString(iPassword));
            }
        }
        return retailerData;
    }

    public void editInfo(RetailerData retailerData) {
        ContentValues cv = new ContentValues();
        cv.put(SHOPNAME, retailerData.getShopName());
        cv.put(EMAIL, retailerData.getEmail());
        cv.put(PHONE_NO, retailerData.getPhoneNo());
        cv.put(ADDRESS, retailerData.getAddress());
        cv.put(CITY, retailerData.getCity());
        cv.put(PINCODE, retailerData.getPinCode());
        cv.put(ROS, retailerData.getRadiusOfService());
        cv.put(USERNAME, retailerData.getUsername());
        cv.put(PASSWORD, retailerData.getPassword());

        retailDatabase.insert(TABLE_NAME, null, cv);
    }

    public ArrayList<RetailerData> getDataOfCity(String city) {
        String[] columns = new String[]{KEY_ROWID, SHOPNAME, EMAIL, PHONE_NO,
                ADDRESS, CITY, PINCODE, ROS, USERNAME, PASSWORD};
        Cursor cursor = retailDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new ArrayList<RetailerData>();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iShopName = cursor.getColumnIndex(SHOPNAME);
        int iEmail = cursor.getColumnIndex(EMAIL);
        int iPhoneNo = cursor.getColumnIndex(PHONE_NO);
        int iAddress = cursor.getColumnIndex(ADDRESS);
        int iCity = cursor.getColumnIndex(CITY);
        int iPincode = cursor.getColumnIndex(PINCODE);
        int iRos = cursor.getColumnIndex(ROS);
        int iUsername = cursor.getColumnIndex(USERNAME);
        int iPassword = cursor.getColumnIndex(PASSWORD);

        ArrayList<RetailerData> retailerDatas = new ArrayList<>();
        RetailerData retailerData;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(iCity).equalsIgnoreCase(city)) {
                retailerData = new RetailerData();
                retailerData.setShopName(cursor.getString(iShopName));
                retailerData.setEmail(cursor.getString(iEmail));
                retailerData.setPhoneNo(cursor.getString(iPhoneNo));
                retailerData.setAddress(cursor.getString(iAddress));
                retailerData.setCity(cursor.getString(iCity));
                retailerData.setPinCode(cursor.getString(iPincode));
                retailerData.setRadiusOfService(cursor.getInt(iRos));
                retailerData.setUsername(cursor.getString(iUsername));
                retailerData.setPassword(cursor.getString(iPassword));
                retailerDatas.add(retailerData);
            }
        }
        return retailerDatas;
    }

    public ArrayList<RetailerData> getAllData() {
        String[] columns = new String[]{KEY_ROWID, SHOPNAME, EMAIL, PHONE_NO,
                ADDRESS, CITY, PINCODE, ROS, USERNAME, PASSWORD};
        Cursor cursor = retailDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new ArrayList<RetailerData>();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iShopName = cursor.getColumnIndex(SHOPNAME);
        int iEmail = cursor.getColumnIndex(EMAIL);
        int iPhoneNo = cursor.getColumnIndex(PHONE_NO);
        int iAddress = cursor.getColumnIndex(ADDRESS);
        int iCity = cursor.getColumnIndex(CITY);
        int iPincode = cursor.getColumnIndex(PINCODE);
        int iRos = cursor.getColumnIndex(ROS);
        int iUsername = cursor.getColumnIndex(USERNAME);
        int iPassword = cursor.getColumnIndex(PASSWORD);

        ArrayList<RetailerData> retailerDatas = new ArrayList<>();
        RetailerData retailerData;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            retailerData = new RetailerData();
            retailerData.setShopName(cursor.getString(iShopName));
            retailerData.setEmail(cursor.getString(iEmail));
            retailerData.setPhoneNo(cursor.getString(iPhoneNo));
            retailerData.setAddress(cursor.getString(iAddress));
            retailerData.setCity(cursor.getString(iCity));
            retailerData.setPinCode(cursor.getString(iPincode));
            retailerData.setRadiusOfService(cursor.getInt(iRos));
            retailerData.setUsername(cursor.getString(iUsername));
            retailerData.setPassword(cursor.getString(iPassword));
            retailerDatas.add(retailerData);

        }

        return retailerDatas;
    }

    public void clearDatabase() {
        String AUTOINCREMENT_RESET = "DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "';";
        retailDatabase.delete(TABLE_NAME, null, null);
        retailDatabase.execSQL(AUTOINCREMENT_RESET);
    }

    /*
    public void updateInfo(String oldUser, String newUser, String oldPass, String newPass) {
        String UPDATE_USERNAME = "UPDATE " + TABLE_NAME +
                " SET " + USERNAME + " = '" + newUser + "'" +
                " WHERE " + USERNAME + " = '" + oldUser + "';";
        String UPDATE_PASSWORD = "UPDATE " + TABLE_NAME +
                " SET " + PASSWORD + " = '" + newPass + "'" +
                " WHERE " + PASSWORD + " = '" + oldPass + "';";
        retailDatabase.execSQL(UPDATE_USERNAME);
        retailDatabase.execSQL(UPDATE_PASSWORD);
    }
    */

    public void deleteData(String username) {
        String DELETE = "DELETE FROM " + TABLE_NAME +
                " WHERE " + USERNAME + " = " + "'" + username + "';";
        retailDatabase.execSQL(DELETE);
    }

    public String[] retrieveUsernames() {
        Cursor c = retailDatabase.query(TABLE_NAME, new String[]{USERNAME}, null, null, null, null, null);
        if (c.getCount() == 0)
            return new String[]{"empty"};

        int iUsername = c.getColumnIndex(USERNAME);

        String[] result = new String[100];
        int i = 0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext(), i++) {
            result[i] = c.getString(iUsername);
        }

        return result;
    }

    public void deletePrevious(String previousUsername) {
        //delete existing entry
        retailDatabase.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " +
                USERNAME + " = '" + previousUsername + "';");

    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, TABLE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SHOPNAME + " VARCHAR2(20) NOT NULL, " +
                    EMAIL + " VARCHAR2(30) NOT NULL, " +
                    PHONE_NO + " VARCHAR2(11) NOT NULL, " +
                    ADDRESS + " VARCHAR2(150) NOT NULL, " +
                    CITY + " VARCHAR2(20) NOT NULL, " +
                    PINCODE + " VARCHAR2(7) NOT NULL, " +
                    ROS + " INTEGER NOT NULL, " +
                    USERNAME + " VARCHAR2(20) UNIQUE, " +
                    PASSWORD + " VARCHAR2(20));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE + ";");
            onCreate(db);
        }
    }

}




