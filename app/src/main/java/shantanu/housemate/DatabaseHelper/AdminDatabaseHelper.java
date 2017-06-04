package shantanu.housemate.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import shantanu.housemate.Data.AdminData;

/**
 * Created by SHAAN on 04-02-17.
 */
public class AdminDatabaseHelper {

    public static final String KEY_ROWID = "id";
    public static final String NAME = "name";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private static final String TABLE_NAME = "ADMIN";
    private static final String DATABASE_TABLE = "admin_database";
    private static final int DATABASE_VERSION = 1;

    private DbHelper adminHelper;
    private final Context adminContext;
    private SQLiteDatabase adminDatabase;

    public AdminDatabaseHelper(Context c) {
        this.adminContext = c;
    }

    public AdminDatabaseHelper open() throws SQLException {
        adminHelper = new DbHelper(adminContext);
        try {
            adminDatabase = adminHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.e("SQL ERROR", e.getMessage());
        }
        return this;
    }

    public void close(){
        adminHelper.close();
    }


    public void putInfo(AdminData adminData) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, adminData.getName());
        cv.put(USERNAME, adminData.getUsername());
        cv.put(PASSWORD, adminData.getPassword());
        adminDatabase.insert(TABLE_NAME, null, cv);
    }

    public Cursor getInfo() {
        String[] columns = new String[]{ KEY_ROWID, NAME, USERNAME, PASSWORD};
        Cursor cursor = adminDatabase.query(TABLE_NAME,columns,null,null,null,null,null);
        cursor.moveToFirst();
        return cursor;
    }

    public AdminData getData() {
        String[] columns = new String[]{ KEY_ROWID, NAME, USERNAME, PASSWORD};
        Cursor c = adminDatabase.query(TABLE_NAME,columns,null,null,null,null,null);
        if(c.getCount() == 0)
            return new AdminData();

        int iRowId = c.getColumnIndex(KEY_ROWID);
        int iName = c.getColumnIndex(NAME);
        int iUsername = c.getColumnIndex(USERNAME);
        int iPassword = c.getColumnIndex(PASSWORD);

        AdminData adminData = new AdminData();
        String result = "";
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            adminData.setName(c.getString(iName));
            adminData.setUsername(c.getString(iUsername));
            adminData.setPassword(c.getString(iPassword));

            result +=   c.getString(iRowId) + " " +
                        c.getString(iName) + " " +
                        c.getString(iUsername) + " " +
                        c.getString(iPassword) + "\n";
        }

        return adminData;
    }

    public void clearDatabase() {
        String AUTOINCREMENT_RESET = "DELETE FROM SQLITE_SEQUENCE WHERE ITEMNAME = '" + TABLE_NAME +
                "';";
        adminDatabase.delete(TABLE_NAME, null, null);
        adminDatabase.execSQL(AUTOINCREMENT_RESET);
    }

    /*
    public void updateInfo(String oldUser, String newUser, String oldPass, String newPass) {
        String UPDATE_USERNAME = "UPDATE " + TABLE_NAME +
                " SET " + USERNAME + " = '" + newUser + "'" +
                " WHERE " + USERNAME + " = '" + oldUser + "';";
        String UPDATE_PASSWORD = "UPDATE " + TABLE_NAME +
                " SET " + PASSWORD + " = '" + newPass + "'" +
                " WHERE " + PASSWORD + " = '" + oldPass + "';";
        adminDatabase.execSQL(UPDATE_USERNAME);
        adminDatabase.execSQL(UPDATE_PASSWORD);
    }
    */

    public void deleteInfo(String username) {
        String DELETE = "DELETE FROM " + TABLE_NAME +
                        " WHERE " + USERNAME + " = " + "'" + username + "';";
        adminDatabase.execSQL(DELETE);
    }

    public AdminData retrieveData(String username) {
        String[] columns = new String[]{ KEY_ROWID, NAME, USERNAME, PASSWORD};
        Cursor cursor = adminDatabase.query(TABLE_NAME,columns,null,null,null,null,null);
        if (cursor.getCount() == 0)
            return new AdminData();

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iUsername = cursor.getColumnIndex(USERNAME);
        int iPassword = cursor.getColumnIndex(PASSWORD);
        int iName = cursor.getColumnIndex(NAME);

        AdminData adminData = new AdminData();
        adminData.setUsername("empty");
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(iUsername).equals(username)) {
                adminData.setUsername(cursor.getString(iUsername));
                adminData.setPassword(cursor.getString(iPassword));
                adminData.setName(cursor.getString(iName));
            }
        }
        return adminData;
    }

    public String[] retrieveUsernamesAndPassword() {
        Cursor c = adminDatabase.query(TABLE_NAME, new String[]{USERNAME, PASSWORD}, null,null,null,null,
                null);
        if(c.getCount() == 0)
            return new String[]{"empty"};

        int iUsername = c.getColumnIndex(USERNAME);
        int iPassword = c.getColumnIndex(PASSWORD);

        String[] data = new String[2];
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            data[0] = c.getString(iUsername);
            data[1] = c.getString(iPassword);
        }

        return data;
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, TABLE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME + " VARCHAR2(20) NOT NULL, " +
                    USERNAME + " VARCHAR2(20) UNIQUE, " +
                    PASSWORD + " VARCHAR2(20));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE + ";" );
            onCreate(db);
        }
    }

}




