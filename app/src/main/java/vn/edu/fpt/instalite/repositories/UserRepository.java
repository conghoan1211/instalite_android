package vn.edu.fpt.instalite.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vn.edu.fpt.instalite.database.DatabaseHelper;
import vn.edu.fpt.instalite.database.UserDAO;
import vn.edu.fpt.instalite.models.User;

public class UserRepository implements UserDAO {
    private DatabaseHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM users";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    User user = new User(
                            cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("userId")),
                            cursor.getString(cursor.getColumnIndexOrThrow("username")),
                            cursor.getString(cursor.getColumnIndexOrThrow("email")),
                            cursor.getString(cursor.getColumnIndexOrThrow("password")),
                            cursor.getString(cursor.getColumnIndexOrThrow("bio")),
                            cursor.getString(cursor.getColumnIndexOrThrow("avatar")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("followersCount")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("followingCount")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("postsCount")),
                            cursor.getLong(cursor.getColumnIndexOrThrow("createdAt"))
                    );
                    users.add(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }

    @Override
    public User getUserById(String userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;
        String query = "SELECT * FROM users " +
                "WHERE userId = ?";

        Cursor cursor = db.rawQuery(query, new String[]{userId});
        if (cursor.moveToFirst()) {
            try {
                user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("userId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("username")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password")),
                        cursor.getString(cursor.getColumnIndexOrThrow("bio")),
                        cursor.getString(cursor.getColumnIndexOrThrow("avatar")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("followersCount")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("followingCount")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("postsCount")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("createdAt"))
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();
        return user;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("username", user.getUsername());
            values.put("avatar", user.getAvatar());
            values.put("bio", user.getBio());

            int rowsUpdated = db.update("users", values, "userId = ?", new String[]{user.getUserId()});
            return rowsUpdated > 0;
        } catch (Exception e) {
            throw new IllegalArgumentException("UserRepository: Update user failed: " + e.getMessage());
        } finally {
            db.close();
        }
    }
}
