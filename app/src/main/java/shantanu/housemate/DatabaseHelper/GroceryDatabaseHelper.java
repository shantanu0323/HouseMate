package shantanu.housemate.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import shantanu.housemate.Data.GroceryData;

/**
 * Created by SHAAN on 04-02-17.
 */
public class GroceryDatabaseHelper {

    public static final String KEY_ROWID = "id";
    public static final String ITEMNAME = "name";
    public static final String ITEMPRICE = "price";

    private static final String TABLE_NAME = "GROCERY";
    private static final String DATABASE_TABLE = "grocery_database";
    private static final int DATABASE_VERSION = 1;

    private DbHelper groceryHelper;
    private final Context groceryContext;
    private SQLiteDatabase groceryDatabase;

    public GroceryDatabaseHelper(Context c) {
        this.groceryContext = c;
    }

    public GroceryDatabaseHelper open() throws SQLException {
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


    public void putData(GroceryData groceryData) {
        ContentValues cv = new ContentValues();
        cv.put(ITEMNAME, groceryData.getItemName());
        cv.put(ITEMPRICE, groceryData.getItemPrice());
        groceryDatabase.insert(TABLE_NAME, null, cv);
    }

    public Cursor getInfo() {
        String[] columns = new String[]{KEY_ROWID, ITEMNAME, ITEMPRICE};
        Cursor cursor = groceryDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public GroceryData getData() {
        String[] columns = new String[]{KEY_ROWID, ITEMNAME, ITEMPRICE};
        Cursor c = groceryDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (c.getCount() == 0)
            return new GroceryData();

        int iRowId = c.getColumnIndex(KEY_ROWID);
        int iItemName = c.getColumnIndex(ITEMNAME);
        int iItemPrice = c.getColumnIndex(ITEMPRICE);


        GroceryData groceryData = new GroceryData();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            groceryData.setItemName(c.getString(iItemName));
            groceryData.setItemPrice(c.getString(iItemPrice));
        }

        return groceryData;
    }

    public ArrayList<GroceryData> getAllData() {
        String[] columns = new String[]{KEY_ROWID, ITEMNAME, ITEMPRICE};
        Cursor cursor = groceryDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new ArrayList<GroceryData>();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iItemName = cursor.getColumnIndex(ITEMNAME);
        int iItemPrice = cursor.getColumnIndex(ITEMPRICE);

        ArrayList<GroceryData> groceryDatas = new ArrayList<>();
        GroceryData groceryData;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            groceryData = new GroceryData();
            groceryData.setItemName(cursor.getString(iItemName));
            groceryData.setItemPrice(cursor.getString(iItemPrice));
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

    public GroceryData retrieveData(String itemName) {
        String[] columns = new String[]{KEY_ROWID, ITEMNAME, ITEMPRICE};
        Cursor cursor = groceryDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.getCount() == 0)
            return new GroceryData();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iItemName = cursor.getColumnIndex(ITEMNAME);
        int iItemPrice = cursor.getColumnIndex(ITEMPRICE);

        GroceryData groceryData = new GroceryData();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(iItemName).equals(itemName)) {
                groceryData.setItemName(cursor.getString(iItemName));
                groceryData.setItemPrice(cursor.getString(iItemPrice));
            }
        }
        return groceryData;
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
                    ITEMPRICE + " INTEGER NOT NULL );");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE + ";");
            onCreate(db);
        }
    }

}