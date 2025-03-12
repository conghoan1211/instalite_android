package vn.edu.fpt.instalite.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import vn.edu.fpt.instalite.database.DatabaseHelper;
import vn.edu.fpt.instalite.database.NotificationDAO;
import vn.edu.fpt.instalite.dto.NotificationDTO;

public class NotificationRepository implements NotificationDAO {
    private DatabaseHelper dbHelper;

    public NotificationRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<NotificationDTO> getNotifications(String currentUserId) {
        List<NotificationDTO> notifications = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Lấy tất cả thông báo dành cho currentUserId
        String query = "SELECT n.id, n.type, n.relatedId, n.content, n.createdAt, " +
                "u.username AS fromUsername, u.avatar AS fromAvatar, n.fromUserId " +
                "FROM notifications n " +
                "JOIN users u ON n.fromUserId = u.userId " +
                "WHERE n.userId = ? " +
                "ORDER BY n.createdAt DESC";

        Cursor cursor = db.rawQuery(query, new String[]{currentUserId});

        if (cursor.moveToFirst()) {
            do {
                try {
                    NotificationDTO notification = new NotificationDTO(
                            cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                            currentUserId,
                            cursor.getString(cursor.getColumnIndexOrThrow("fromUserId")),
                            cursor.getString(cursor.getColumnIndexOrThrow("fromUsername")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("relatedId")),
                            cursor.getString(cursor.getColumnIndexOrThrow("fromAvatar")),
                            cursor.getString(cursor.getColumnIndexOrThrow("content")),
                            cursor.getLong(cursor.getColumnIndexOrThrow("createdAt")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("type"))
                    );
                    notifications.add(notification);
                } catch (Exception e) {
                    Log.e("NotificationRepo", "Error parsing notification: " + e.getMessage());
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notifications;
    }

    public String getUserIdByPostId(int postId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String userId = null;

        String query = "SELECT userId FROM posts WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(postId)});

        if (cursor.moveToFirst()) {
            userId = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
        }

        cursor.close();
        db.close();
        return userId;
    }

    public boolean insertNotification(String userId, String fromUserId, int type, int relatedId, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("userId", userId);           // Người nhận thông báo
            values.put("fromUserId", fromUserId);   // Người gây ra thông báo (like, comment, follow)
            values.put("type", type);               // 0: like, 1: comment, 2: follow
            values.put("relatedId", relatedId);     // ID của post/comment/follow
            values.put("content", content);
            values.put("isRead", 0);
            values.put("createdAt", System.currentTimeMillis());

            long newRowId = db.insert("notifications", null, values);
            return newRowId != -1;
        } catch (Exception e) {
            Log.e("PostRepository", "Insert notification failed: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }
}
