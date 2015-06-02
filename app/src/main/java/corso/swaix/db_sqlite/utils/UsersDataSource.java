package corso.swaix.db_sqlite.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import corso.swaix.db_sqlite.models.User;

public class UsersDataSource {

    // Database fields
    private SQLiteDatabase database;

    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME};

    public UsersDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public User createUser(String User) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, User);
        long insertId = database.insert(MySQLiteHelper.TABLE_USERS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        User newUser = cursorToUser(cursor);
        cursor.close();
        return newUser;
    }

    public void deleteUser(User User) {
        long id = User.getId();
        System.out.println("User deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_USERS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<User> getAllUsers() {
        List<User> Users = new ArrayList<User>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User User = cursorToUser(cursor);
            Users.add(User);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return Users;
    }

    private User cursorToUser(Cursor cursor) {
        User User = new User();
        User.setId(cursor.getLong(0));
        User.setName(cursor.getString(1));
        return User;
    }
} 
