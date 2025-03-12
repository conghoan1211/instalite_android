package vn.edu.fpt.instalite.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import vn.edu.fpt.instalite.database.DatabaseHelper;
import vn.edu.fpt.instalite.database.LikeCommentDAO;
import vn.edu.fpt.instalite.models.Comment;
import vn.edu.fpt.instalite.models.User;
import vn.edu.fpt.instalite.services.NotificationService;
import vn.edu.fpt.instalite.sessions.DataLocalManager;

public class LikeCommentRepository implements LikeCommentDAO {
    private DatabaseHelper dbHelper;
    public LikeCommentRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public boolean toggleLike(String postId, String userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Cần writable vì sẽ thay đổi dữ liệu
        Cursor cursor = null;
        boolean isSuccessful = false;
        try {
            String checkQuery = "SELECT id FROM likes WHERE postId = ? AND userId = ?";
            cursor = db.rawQuery(checkQuery, new String[]{postId, userId});
            if (cursor.moveToFirst()) {
                int rowsDeleted = db.delete("likes", "postId = ? AND userId = ?", new String[]{postId, userId});
                isSuccessful = rowsDeleted > 0;
            } else {
                ContentValues values = new ContentValues();
                values.put("postId", postId);
                values.put("userId", userId);
                values.put("createdAt", System.currentTimeMillis());
                long newRowId = db.insert("likes", null, values);
                isSuccessful = newRowId != -1;
            }
            updatePostLikesCount(db, postId);
            return isSuccessful;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    // Hàm phụ để cập nhật số lượng likes trong bảng posts
    private void updatePostLikesCount(SQLiteDatabase db, String postId) {
        String countQuery = "SELECT COUNT(*) FROM likes WHERE postId = ?";
        Cursor countCursor = db.rawQuery(countQuery, new String[]{postId});
        int likesCount = 0;
        if (countCursor.moveToFirst()) {
            likesCount = countCursor.getInt(0);
        }
        countCursor.close();

        ContentValues values = new ContentValues();
        values.put("likes", likesCount);
        db.update("posts", values, "id = ?", new String[]{postId});
    }

    public int getLikesCount(String postId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM likes WHERE postId = ?", new String[]{postId});
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return 0;
    }

    public boolean isPostLiked(String userId, String postId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            String checkQuery = "SELECT id FROM likes WHERE postId = ? AND userId = ?";
            cursor = db.rawQuery(checkQuery, new String[]{postId, userId});
            if (cursor.moveToFirst()) {
               return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }



    @Override
    public boolean commentPost(Comment input) {
        return false;
    }
}
