package shantanu.housemate.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import shantanu.housemate.Data.CustomerData;

/**
 * Created by SHAAN on 04-02-17.
 */
public class CustomerDatabaseHelper {

    public static final String KEY_ROWID = "id";
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String EMAIL = "email";
    public static final String PHONE_NO = "phone_no";
    public static final String ADDRESS = "address";
    public static final String CITY = "city";
    public static final String PINCODE = "pincode";
    public static final String NOG = "nog";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private static final String TABLE_NAME = "CUSTOMER";
    private static final String DATABASE_TABLE = "customer_database";
    private static final int DATABASE_VERSION = 1;

    private DbHelper custHelper;
    private final Context custContext;
    private SQLiteDatabase custDatabase;

    public CustomerDatabaseHelper(Context c) {
        this.custContext = c;
    }

    public CustomerDatabaseHelper open() throws SQLException {
        custHelper = new DbHelper(custContext);
        custDatabase = custHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        custHelper.close();
    }


    public void putData(CustomerData customerData) {
        ContentValues cv = new ContentValues();
        cv.put(FIRSTNAME, customerData.getFirstName());
        cv.put(LASTNAME, customerData.getLastName());
        cv.put(EMAIL, customerData.getEmail());
        cv.put(PHONE_NO, customerData.getPhoneNo());
        cv.put(ADDRESS, customerData.getAddress());
        cv.put(CITY, customerData.getCity());
        cv.put(PINCODE, customerData.getPinCode());
        cv.put(NOG, customerData.getNoOfG());
        cv.put(USERNAME, customerData.getUsername());
        cv.put(PASSWORD, customerData.getPassword());
        custDatabase.insert(TABLE_NAME, null, cv);
    }

    public void deletePrevious(String username) {
        //delete existing entry
        custDatabase.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " +
                USERNAME + " = '" + username + "';");

    }

    public void editData(CustomerData customerData) {
        ContentValues cv = new ContentValues();
        cv.put(FIRSTNAME, customerData.getFirstName());
        cv.put(LASTNAME, customerData.getLastName());
        cv.put(EMAIL, customerData.getEmail());
        cv.put(PHONE_NO, customerData.getPhoneNo());
        cv.put(ADDRESS, customerData.getAddress());
        cv.put(CITY, customerData.getCity());
        cv.put(PINCODE, customerData.getPinCode());
        cv.put(NOG, customerData.getNoOfG());
        cv.put(USERNAME, customerData.getUsername());
        cv.put(PASSWORD, customerData.getPassword());

        custDatabase.insert(TABLE_NAME, null, cv);
    }

    public Cursor getInfo() {
        String[] columns = new String[]{KEY_ROWID, FIRSTNAME, LASTNAME, EMAIL, PHONE_NO,
                ADDRESS, CITY, PINCODE, NOG, USERNAME, PASSWORD};
        Cursor cursor = custDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public ArrayList<CustomerData> getAllData() {
        String[] columns = new String[]{KEY_ROWID, FIRSTNAME, LASTNAME, EMAIL, PHONE_NO,
                ADDRESS, CITY, PINCODE, NOG, USERNAME, PASSWORD};
        Cursor cursor = custDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new ArrayList<CustomerData>();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iFirstName = cursor.getColumnIndex(FIRSTNAME);
        int iLastName = cursor.getColumnIndex(LASTNAME);
        int iEmail = cursor.getColumnIndex(EMAIL);
        int iPhoneNo = cursor.getColumnIndex(PHONE_NO);
        int iAddress = cursor.getColumnIndex(ADDRESS);
        int iCity = cursor.getColumnIndex(CITY);
        int iPincode = cursor.getColumnIndex(PINCODE);
        int iNog = cursor.getColumnIndex(NOG);
        int iUsername = cursor.getColumnIndex(USERNAME);
        int iPassword = cursor.getColumnIndex(PASSWORD);

        ArrayList<CustomerData> customerDatas = new ArrayList<>();
        CustomerData customerData;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            customerData = new CustomerData();
            customerData.setFirstName(cursor.getString(iFirstName));
            customerData.setLastName(cursor.getString(iLastName));
            customerData.setEmail(cursor.getString(iEmail));
            customerData.setPhoneNo(cursor.getString(iPhoneNo));
            customerData.setAddress(cursor.getString(iAddress));
            customerData.setCity(cursor.getString(iCity));
            customerData.setPinCode(cursor.getString(iPincode));
            customerData.setNoOfG(cursor.getInt(iNog));
            customerData.setUsername(cursor.getString(iUsername));
            customerData.setPassword(cursor.getString(iPassword));
            customerDatas.add(customerData);

        }

        return customerDatas;
    }

    public CustomerData getData(String usernameToEdit) {
        String[] columns = new String[]{KEY_ROWID, FIRSTNAME, LASTNAME, EMAIL, PHONE_NO,
                ADDRESS, CITY, PINCODE, NOG, USERNAME, PASSWORD};
        Cursor cursor = custDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new CustomerData();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iFirstName = cursor.getColumnIndex(FIRSTNAME);
        int iLastName = cursor.getColumnIndex(LASTNAME);
        int iEmail = cursor.getColumnIndex(EMAIL);
        int iPhoneNo = cursor.getColumnIndex(PHONE_NO);
        int iAddress = cursor.getColumnIndex(ADDRESS);
        int iCity = cursor.getColumnIndex(CITY);
        int iPincode = cursor.getColumnIndex(PINCODE);
        int iNog = cursor.getColumnIndex(NOG);
        int iUsername = cursor.getColumnIndex(USERNAME);
        int iPassword = cursor.getColumnIndex(PASSWORD);

        CustomerData customerData = new CustomerData();
        customerData.setUsername("empty");
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(iUsername).equals(usernameToEdit)) {
                customerData.setFirstName(cursor.getString(iFirstName));
                customerData.setLastName(cursor.getString(iLastName));
                customerData.setEmail(cursor.getString(iEmail));
                customerData.setPhoneNo(cursor.getString(iPhoneNo));
                customerData.setAddress(cursor.getString(iAddress));
                customerData.setCity(cursor.getString(iCity));
                customerData.setPinCode(cursor.getString(iPincode));
                customerData.setNoOfG(cursor.getInt(iNog));
                customerData.setUsername(cursor.getString(iUsername));
                customerData.setPassword(cursor.getString(iPassword));
            }
        }
        return customerData;
    }

    public void clearDatabase() {
        String AUTOINCREMENT_RESET = "DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "';";
        custDatabase.delete(TABLE_NAME, null, null);
        custDatabase.execSQL(AUTOINCREMENT_RESET);
    }

    public void deleteData(String username) {
        String DELETE = "DELETE FROM " + TABLE_NAME +
                " WHERE " + USERNAME + " = " + "'" + username + "';";
        custDatabase.execSQL(DELETE);
    }

    public String[] retrieveUsernames() {
        Cursor c = custDatabase.query(TABLE_NAME, new String[]{USERNAME}, null, null, null, null, null);
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

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, TABLE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FIRSTNAME + " VARCHAR2(20) NOT NULL, " +
                    LASTNAME + " VARCHAR2(20) NOT NULL, " +
                    EMAIL + " VARCHAR2(30) NOT NULL, " +
                    PHONE_NO + " VARCHAR2(11) NOT NULL, " +
                    ADDRESS + " VARCHAR2(150) NOT NULL, " +
                    CITY + " VARCHAR2(20) NOT NULL, " +
                    PINCODE + " VARCHAR2(7) NOT NULL, " +
                    NOG + " INTEGER NOT NULL, " +
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