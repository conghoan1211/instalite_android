package vn.edu.fpt.instalite.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import vn.edu.fpt.instalite.database.DatabaseHelper;
import vn.edu.fpt.instalite.database.PostDAO;
import vn.edu.fpt.instalite.dto.PostDTO;
import vn.edu.fpt.instalite.dto.UploadPostDTO;
import vn.edu.fpt.instalite.models.Post;

public class PostRepository implements PostDAO {
    private DatabaseHelper dbHelper;

    public PostRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    @Override
    public boolean addPost(UploadPostDTO post) throws IllegalArgumentException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("userId", post.getUserId());
            values.put("content", post.getContent());
            values.put("imageUrl", post.getImageUrl());
            values.put("likes", post.getLikes());
            values.put("comments", post.getComments());
            values.put("views", post.getViews());
            values.put("privacy", post.getPrivacy());
            values.put("createdAt", post.getCreatedAt());

            long newRowId = db.insert("posts", null, values);
            if (newRowId != -1) {
                // ✅ Nếu thêm post thành công → cập nhật số lượng post cho user
                updatePostsCountForUser(post.getUserId());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e("PostRepository", "Insert post failed: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    @Override
    public boolean deletePost(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT userId FROM posts WHERE id = ?", new String[]{id});
            String userId = null;
            if (cursor != null && cursor.moveToFirst()) {
                userId = cursor.getString(0);
                cursor.close();
            }
            long newRowId = db.delete("posts", "id=?", new String[]{id});
            db.delete("notifications", "relatedId = ?", new String[]{id});
            if(newRowId == -1) {
                return false;
            }
            // 3. Nếu có userId → giảm postsCount
            if (userId != null) {
                db.execSQL("UPDATE users SET postsCount = MAX(postsCount - 1, 0) WHERE userId = ?", new String[]{userId});
            }
        } catch (Exception e) {
            Log.e("PostRepository", "Delete post failed: " + e.getMessage());
        } finally {
            db.close();
        }
        return true;
    }

    @Override
    public PostDTO getPostById(String postId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        PostDTO post = null;

        String query = "SELECT p.id, p.userId, u.username, u.avatar, p.content, p.imageUrl, " +
                "p.likes, p.views, p.comments, p.privacy, p.createdAt " +
                "FROM posts p " +
                "JOIN users u ON p.userId = u.userId " +
                "WHERE p.id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{postId});

        if (cursor.moveToFirst()) {
            try {
                post = new PostDTO(
                        String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("id"))),
                        cursor.getString(cursor.getColumnIndexOrThrow("userId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("username")),
                        cursor.getString(cursor.getColumnIndexOrThrow("avatar")),
                        cursor.getString(cursor.getColumnIndexOrThrow("content")),
                        cursor.getString(cursor.getColumnIndexOrThrow("imageUrl")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("likes")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("comments")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("views")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("privacy")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("createdAt"))
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();
        return post;
    }

    public List<PostDTO> getPostsByUserId(String userId) {
        List<PostDTO> posts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM posts WHERE userId = ? ORDER BY createdAt DESC";
        Cursor cursor = db.rawQuery(query, new String[]{userId});

        if (cursor.moveToFirst()) {
            do {
                try {
                    PostDTO post = new PostDTO(
                            cursor.getString(cursor.getColumnIndexOrThrow("id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("userId")),
                            null, // username không cần vì lấy từ User
                            null, // avatar không cần vì lấy từ User
                            cursor.getString(cursor.getColumnIndexOrThrow("content")),
                            cursor.getString(cursor.getColumnIndexOrThrow("imageUrl")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("likes")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("comments")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("views")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("privacy")),
                            cursor.getLong(cursor.getColumnIndexOrThrow("createdAt"))
                    );
                    posts.add(post);
                } catch (Exception e) {
                    Log.e("PostRepository", "Error parsing post: " + e.getMessage());
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return posts;
    }

    @Override
    public List<PostDTO> getAllPosts() {
        List<PostDTO> posts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT p.id, p.userId, u.username, u.avatar, p.content, p.imageUrl, " +
                "p.likes, p.views, p.comments, p.privacy, p.createdAt " +
                "FROM posts p " +
                "JOIN users u ON p.userId = u.userId " +
                "ORDER BY p.createdAt DESC";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    PostDTO post = new PostDTO(
                            cursor.getString(cursor.getColumnIndexOrThrow("id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("userId")),
                            cursor.getString(cursor.getColumnIndexOrThrow("username")),
                            cursor.getString(cursor.getColumnIndexOrThrow("avatar")),
                            cursor.getString(cursor.getColumnIndexOrThrow("content")),
                            cursor.getString(cursor.getColumnIndexOrThrow("imageUrl")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("likes")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("comments")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("views")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("privacy")),
                            cursor.getLong(cursor.getColumnIndexOrThrow("createdAt"))
                    );
                    posts.add(post);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return posts;
    }

    @Override
    public void updatePost(Post post) {
        // Kiểm tra điều kiện đơn giản
        if (post.getId() == null || post.getId().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (post.getContent() != null && post.getContent().length() > 500) {
            throw new IllegalArgumentException("Content cannot exceed 500 characters");
        }
        if (post.getPrivacy() < 0 || post.getPrivacy() > 1) {
            throw new IllegalArgumentException("Privacy must be 0 or 1");
        }
        // Cập nhật database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", post.getContent());
        values.put("imageUrl", post.getImageUrl());
        values.put("privacy", post.getPrivacy());

        int rowsAffected = db.update("posts", values, "id = ?", new String[]{post.getId()});
        db.close();

        if (rowsAffected == 0)
            throw new IllegalStateException("No post found with ID: " + post.getId());
    }


    public void updatePostsCountForUser(String userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            // Tăng postsCount thêm 1
            db.execSQL("UPDATE users SET postsCount = postsCount + 1 WHERE userId = ?", new String[]{userId});
        } catch (Exception e) {
            Log.e("UserRepository", "Update postsCount failed: " + e.getMessage());
        } finally {
            db.close();
        }
    }
    public void decreasePostsCountForUser(String userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("UPDATE users SET postsCount = MAX(postsCount - 1, 0) WHERE userId = ?", new String[]{userId});
        } catch (Exception e) {
            Log.e("UserRepository", "Decrease postsCount failed: " + e.getMessage());
        } finally {
            db.close();
        }
    }
}