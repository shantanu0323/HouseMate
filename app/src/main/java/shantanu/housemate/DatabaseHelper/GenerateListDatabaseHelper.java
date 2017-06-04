package shantanu.housemate.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import shantanu.housemate.Data.GenerateListData;

/**
 * Created by SHAAN on 04-02-17.
 */
public class GenerateListDatabaseHelper {

    public static final String KEY_ROWID = "id";
    public static final String ITEMNAME = "name";
    public static final String ITEMPRICE = "price";
    public static final String QUANTITY = "quantity";
    public static final String USERNAME = "username";

    private static final String TABLE_NAME = "GENERATE_LIST";
    private static final String DATABASE_TABLE = "grocery_database";
    private static final int DATABASE_VERSION = 1;

    private DbHelper groceryHelper;
    private final Context groceryContext;
    private SQLiteDatabase groceryDatabase;

    public GenerateListDatabaseHelper(Context c) {
        this.groceryContext = c;
    }

    public GenerateListDatabaseHelper open() throws SQLException {
        groceryHelper = new DbHelper(groceryContext);
        try {
            groceryDatabase = groceryHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.e("SQL ERROR", e.getMessage());
        }
        return this;
    }

    public void close() {
        groceryHelper.close();
    }

    public void putData(GenerateListData groceryData) {
        ContentValues cv = new ContentValues();
        cv.put(ITEMNAME, groceryData.getItemName());
        cv.put(ITEMPRICE, groceryData.getItemPrice());
        cv.put(QUANTITY, groceryData.getQuantity());
        cv.put(USERNAME, groceryData.getUsername());
        groceryDatabase.insert(TABLE_NAME, null, cv);
    }

    public Cursor getInfo() {
        String[] columns = new String[]{KEY_ROWID, ITEMNAME, ITEMPRICE, QUANTITY, USERNAME};
        Cursor cursor = groceryDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public GenerateListData getData() {
        String[] columns = new String[]{KEY_ROWID, ITEMNAME, ITEMPRICE, QUANTITY, USERNAME};
        Cursor c = groceryDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (c.getCount() == 0)
            return new GenerateListData();

        int iRowId = c.getColumnIndex(KEY_ROWID);
        int iItemName = c.getColumnIndex(ITEMNAME);
        int iItemPrice = c.getColumnIndex(ITEMPRICE);
        int iQuantity = c.getColumnIndex(QUANTITY);
        int iUsername = c.getColumnIndex(USERNAME);

        GenerateListData groceryData = new GenerateListData();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            groceryData.setItemName(c.getString(iItemName));
            groceryData.setItemPrice(c.getString(iItemPrice));
            groceryData.setQuantity(c.getString(iQuantity));
            groceryData.setUsername(c.getString(iUsername));
        }

        return groceryData;
    }

    public ArrayList<GenerateListData> getSelectedDataOfCustomer(String itemName) {
        String[] columns = new String[]{KEY_ROWID, ITEMNAME, ITEMPRICE, QUANTITY, USERNAME};
        Cursor cursor = groceryDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new ArrayList<GenerateListData>();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iItemName = cursor.getColumnIndex(ITEMNAME);
        int iItemPrice = cursor.getColumnIndex(ITEMPRICE);
        int iQuantity = cursor.getColumnIndex(QUANTITY);
        int iUsername = cursor.getColumnIndex(USERNAME);

        ArrayList<GenerateListData> groceryDatas = new ArrayList<>();
        GenerateListData groceryData;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if ((cursor.getString(iItemName).equals(itemName)) &&
                    Boolean.parseBoolean(cursor.getString(iQuantity))) {
                groceryData = new GenerateListData();
                groceryData.setItemName(cursor.getString(iItemName));
                groceryData.setItemPrice(cursor.getString(iItemPrice));
                groceryData.setQuantity(cursor.getString(iQuantity));
                groceryData.setUsername(cursor.getString(iUsername));
                groceryDatas.add(groceryData);
            }
        }
        return groceryDatas;
    }

    public ArrayList<GenerateListData> getUnselectedDataOfCustomer(String itemName) {
        String[] columns = new String[]{KEY_ROWID, ITEMNAME, ITEMPRICE, QUANTITY, USERNAME};
        Cursor cursor = groceryDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new ArrayList<GenerateListData>();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iItemName = cursor.getColumnIndex(ITEMNAME);
        int iItemPrice = cursor.getColumnIndex(ITEMPRICE);
        int iQuantity = cursor.getColumnIndex(QUANTITY);
        int iUsername = cursor.getColumnIndex(USERNAME);

        ArrayList<GenerateListData> groceryDatas = new ArrayList<>();
        GenerateListData groceryData;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (((cursor.getString(iItemName).equals(itemName)) ||
                    (cursor.getString(iItemName).equals("empty"))) &&
                    !(Boolean.parseBoolean(cursor.getString(iQuantity)))) {
                groceryData = new GenerateListData();
                groceryData.setItemName(cursor.getString(iItemName));
                groceryData.setItemPrice(cursor.getString(iItemPrice));
                groceryData.setQuantity(cursor.getString(iQuantity));
                groceryData.setUsername(cursor.getString(iUsername));
                groceryDatas.add(groceryData);
            }
        }
        return groceryDatas;
    }

    public ArrayList<GenerateListData> getDataOfGrocery(String itemName) {
        String[] columns = new String[]{KEY_ROWID, ITEMNAME, ITEMPRICE, QUANTITY, USERNAME};
        Cursor cursor = groceryDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new ArrayList<GenerateListData>();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iItemName = cursor.getColumnIndex(ITEMNAME);
        int iItemPrice = cursor.getColumnIndex(ITEMPRICE);
        int iQuantity = cursor.getColumnIndex(QUANTITY);
        int iUsername = cursor.getColumnIndex(USERNAME);

        ArrayList<GenerateListData> groceryDatas = new ArrayList<>();
        GenerateListData groceryData;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if ((cursor.getString(iItemName).equals(itemName))) {
                groceryData = new GenerateListData();
                groceryData.setItemName(cursor.getString(iItemName));
                groceryData.setItemPrice(cursor.getString(iItemPrice));
                groceryData.setQuantity(cursor.getString(iQuantity));
                groceryData.setUsername(cursor.getString(iUsername));
                groceryDatas.add(groceryData);
            }
        }
        return groceryDatas;
    }

    public ArrayList<GenerateListData> getAllData() {
        String[] columns = new String[]{KEY_ROWID, ITEMNAME, ITEMPRICE, QUANTITY, USERNAME};
        Cursor cursor = groceryDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new ArrayList<GenerateListData>();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iItemName = cursor.getColumnIndex(ITEMNAME);
        int iItemPrice = cursor.getColumnIndex(ITEMPRICE);
        int iQuantity = cursor.getColumnIndex(QUANTITY);
        int iUsername = cursor.getColumnIndex(USERNAME);

        ArrayList<GenerateListData> groceryDatas = new ArrayList<>();
        GenerateListData groceryData;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            groceryData = new GenerateListData();
            groceryData.setItemName(cursor.getString(iItemName));
            groceryData.setItemPrice(cursor.getString(iItemPrice));
            groceryData.setQuantity(cursor.getString(iQuantity));
            groceryData.setUsername(cursor.getString(iUsername));
            groceryDatas.add(groceryData);
        }
        return groceryDatas;
    }

    public void deleteData(String itemName) {
        String DELETE = "DELETE FROM " + TABLE_NAME +
                " WHERE " + ITEMNAME + " = " + "'" + itemName + "';";
        groceryDatabase.execSQL(DELETE);
    }

    public void clearDatabase() {
        String AUTOINCREMENT_RESET = "DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                TABLE_NAME + "';";
        groceryDatabase.delete(TABLE_NAME, null, null);
        groceryDatabase.execSQL(AUTOINCREMENT_RESET);
    }

    public void deleteInfo(String itemName) {
        String DELETE = "DELETE FROM " + TABLE_NAME +
                " WHERE " + ITEMNAME + " = " + "'" + itemName + "';";
        groceryDatabase.execSQL(DELETE);
    }

    public GenerateListData retrieveData(String itemName) {
        String[] columns = new String[]{KEY_ROWID, ITEMNAME, ITEMPRICE, QUANTITY, USERNAME};
        Cursor cursor = groceryDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new GenerateListData();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iItemName = cursor.getColumnIndex(ITEMNAME);
        int iItemPrice = cursor.getColumnIndex(ITEMPRICE);
        int iQuantity = cursor.getColumnIndex(QUANTITY);
        int iUsername = cursor.getColumnIndex(USERNAME);

        GenerateListData groceryData = new GenerateListData();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(iItemName).equals(itemName)) {
                groceryData.setItemName(cursor.getString(iItemName));
                groceryData.setItemPrice(cursor.getString(iItemPrice));
                groceryData.setQuantity(cursor.getString(iQuantity));
                groceryData.setUsername(cursor.getString(iUsername));
            }
        }
        return groceryData;
    }

    public void editData(GenerateListData groceryData) {
        ContentValues cv = new ContentValues();
        cv.put(ITEMNAME, groceryData.getItemName());
        cv.put(ITEMPRICE, groceryData.getItemPrice());
        cv.put(QUANTITY, groceryData.getQuantity());
        cv.put(USERNAME,groceryData.getUsername());
        groceryDatabase.insert(TABLE_NAME, null, cv);
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, TABLE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ITEMNAME + " VARCHAR2(20) NOT NULL, " +
                    ITEMPRICE + " INTEGER NOT NULL, " +
                    QUANTITY + " VARCHAR2(10) NOT NULL, " +
                    USERNAME + " VARCHAR2(20) NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE + ";");
            onCreate(db);
        }
    }

}