package vn.edu.fpt.instalite.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.UUID;

import vn.edu.fpt.instalite.database.AuthenDAO;
import vn.edu.fpt.instalite.database.DatabaseHelper;
import vn.edu.fpt.instalite.dto.RegisterDTO;
import vn.edu.fpt.instalite.models.User;
import vn.edu.fpt.instalite.utils.Common;

public class AuthenRepository implements AuthenDAO {
    private DatabaseHelper dbHelper;

    public AuthenRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public User Login(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        User user = null;

        try {
            cursor = db.rawQuery("SELECT * FROM users WHERE (email = ? OR username = ?) AND password = ?",
                    new String[]{username, username, Common.hashPassword(password)});
            if (cursor.moveToFirst()) {
                user = new User();
                user.setUserId(cursor.getString(cursor.getColumnIndexOrThrow("userId")));
                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                user.setBio(cursor.getString(cursor.getColumnIndexOrThrow("bio")));
                user.setAvatar(cursor.getString(cursor.getColumnIndexOrThrow("avatar")));
                user.setFollowersCount(cursor.getInt(cursor.getColumnIndexOrThrow("followersCount")));
                user.setFollowingCount(cursor.getInt(cursor.getColumnIndexOrThrow("followingCount")));
                user.setPostsCount(cursor.getInt(cursor.getColumnIndexOrThrow("postsCount")));
                user.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow("createdAt")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return user;
    }

    @Override
    public String Register(RegisterDTO input) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM users WHERE username = ? OR email = ?",
                    new String[]{input.getUsername(), input.getEmail()});
            if (cursor.moveToFirst()) return "Username hoặc email đã tồn tại!";

            String hashedPassword = Common.hashPassword(input.getPassword());
            if (hashedPassword == null) return "Lỗi khi mã hóa mật khẩu!";

            String userId = UUID.randomUUID().toString();

            ContentValues values = new ContentValues();
            values.put("userId", userId);
            values.put("username", input.getUsername());
            values.put("email", input.getEmail());
            values.put("password", hashedPassword);
            values.put("createdAt", System.currentTimeMillis());

            long result = db.insert("users", null, values);
            return result != -1 ? "" : "Đăng ký thất bại, thử lại!";
        } catch (Exception e) {
            return "Lỗi hệ thống: " + e.getMessage();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

}
