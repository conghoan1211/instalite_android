package vn.edu.fpt.instalite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "instalite.db";
    private static final int DATABASE_VERSION = 12;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bảng users (Thông tin người dùng - profile)
        String createUsersTable = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId TEXT UNIQUE DEFAULT (uuid())," +
                "username TEXT NOT NULL," +
                "email TEXT UNIQUE," +
                "password TEXT," +
                "bio TEXT," +
                "avatar TEXT," +
                "followersCount INTEGER DEFAULT 0," +
                "followingCount INTEGER DEFAULT 0," +
                "postsCount INTEGER DEFAULT 0," +
                "createdAt INTEGER)";
        db.execSQL(createUsersTable);

        // Bảng posts (Bài đăng)
        String createPostsTable = "CREATE TABLE posts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId TEXT," +
                "content TEXT," +
                "imageUrl TEXT," +
                "likes INTEGER DEFAULT 0," +
                "comments INTEGER DEFAULT 0," +
                "views INTEGER DEFAULT 0," +
                "privacy INTEGER DEFAULT 0," + // 0: public, 1: private
                "createdAt INTEGER," +
                "FOREIGN KEY (userId) REFERENCES users(userId))";
        db.execSQL(createPostsTable);

        // Bảng comments (Bình luận)
        String createCommentsTable = "CREATE TABLE comments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "postId INTEGER," +
                "userId TEXT," +
                "content TEXT," +
                "createdAt INTEGER," +
                "FOREIGN KEY (postId) REFERENCES posts(id)," +
                "FOREIGN KEY (userId) REFERENCES users(userId))";
        db.execSQL(createCommentsTable);

        // Bảng likes (Lượt thích)
        String createLikesTable = "CREATE TABLE likes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "postId INTEGER," +
                "userId TEXT," +
                "createdAt INTEGER," +
                "FOREIGN KEY (postId) REFERENCES posts(id)," +
                "FOREIGN KEY (userId) REFERENCES users(userId))";
        db.execSQL(createLikesTable);

        // Bảng followers (Quan hệ theo dõi)
        String createFollowersTable = "CREATE TABLE followers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "followerId TEXT," + // Người theo dõi
                "followingId TEXT," + // Người được theo dõi
                "createdAt INTEGER," +
                "FOREIGN KEY (followerId) REFERENCES users(userId)," +
                "FOREIGN KEY (followingId) REFERENCES users(userId))";
        db.execSQL(createFollowersTable);

        // Bảng notifications (Thông báo)
        String createNotificationsTable = "CREATE TABLE notifications (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId TEXT," +
                "type INTEGER," + // 0: like, 1: comment, 2: follow
                "relatedId INTEGER," + // Id của post/comment/follow
                "content TEXT, " +
                "fromUserId TEXT," +
                "isRead INTEGER DEFAULT 0," +
                "createdAt INTEGER," +
                "FOREIGN KEY (userId) REFERENCES users(userId)," +
                "FOREIGN KEY (relatedId) REFERENCES posts(id)," +
                "FOREIGN KEY (fromUserId) REFERENCES users(userId))";
        db.execSQL(createNotificationsTable);

        // Bảng messages (Tin nhắn chat)
        String createMessagesTable = "CREATE TABLE messages (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "senderId TEXT," +
                "receiverId TEXT," +
                "content TEXT," +
                "isRead INTEGER DEFAULT 0," +
                "createdAt INTEGER," +
                "FOREIGN KEY (senderId) REFERENCES users(userId)," +
                "FOREIGN KEY (receiverId) REFERENCES users(userId))";
        db.execSQL(createMessagesTable);
        // Thêm dữ liệu mẫu vào bảng users
        insertSampleUsers(db);

        // Thêm dữ liệu mẫu vào bảng posts
        insertSamplePosts(db);

    }
    private void insertSampleUsers(SQLiteDatabase db) {
        // Tạo thời gian hiện tại tính bằng milliseconds
        long currentTime = System.currentTimeMillis();

        ContentValues[] userValues = new ContentValues[5];

        userValues[0] = new ContentValues();
        userValues[0].put("userId", "user_001");
        userValues[0].put("username", "admin");
        userValues[0].put("email", "nguyenvan@example.com");
        userValues[0].put("password", "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f");  //password123
        userValues[0].put("bio", "Xin chào, tôi là Nguyễn Văn");
        userValues[0].put("avatar", "img_parisnight");
        userValues[0].put("followersCount", 120);
        userValues[0].put("followingCount", 85);
        userValues[0].put("postsCount", 25);
        userValues[0].put("createdAt", currentTime - 7776000000L); // 90 ngày trước

        userValues[1] = new ContentValues();
        userValues[1].put("userId", "user_003");
        userValues[1].put("username", "phamhoan");
        userValues[1].put("email", "hoanpham12112003@gmail.com");
        userValues[1].put("password", "48b93588e522e4c5fe17ca8f781df7fcb26df2812ba3043d658292c47d012886"); //Abc@123456
        userValues[1].put("bio", "Developer | Tech enthusiast");
        userValues[1].put("avatar", "img_cat");
        userValues[1].put("followersCount", 87);
        userValues[1].put("followingCount", 134);
        userValues[1].put("postsCount", 15);
        userValues[1].put("createdAt", currentTime - 4320000000L); // 50 ngày trước

        // Chèn tất cả users vào database
        for (ContentValues values : userValues) {
            db.insert("users", null, values);
        }
    }

    private void insertSamplePosts(SQLiteDatabase db) {
        // Tạo thời gian hiện tại
        long currentTime = System.currentTimeMillis();
        ContentValues[] postValues = new ContentValues[10];

        // Post 2
        postValues[0] = new ContentValues();
        postValues[0].put("userId", "user_003");
        postValues[0].put("content", "Hôm nay là một ngày tuyệt vời! #sunshinengày tuyệt vời! #sunshinengày tuyệt vời! #sunshinengày tuyệt vời! #sunshinengày tuyệt vời! #sunshine");
        postValues[0].put("imageUrl", "img_cat2, img_cat, img_parisnight");
        postValues[0].put("likes", 0);
        postValues[0].put("comments", 0);
        postValues[0].put("views", 0);
        postValues[0].put("privacy", 0); // public
        postValues[0].put("createdAt", currentTime - 172800000L); // 2 ngày trước

        // Chèn tất cả posts vào database
        for (ContentValues values : postValues) {
            db.insert("posts", null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa các bảng cũ nếu tồn tại
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS posts");
        db.execSQL("DROP TABLE IF EXISTS comments");
        db.execSQL("DROP TABLE IF EXISTS likes");
        db.execSQL("DROP TABLE IF EXISTS followers");
        db.execSQL("DROP TABLE IF EXISTS notifications");
        db.execSQL("DROP TABLE IF EXISTS messages");
        onCreate(db);
    }
}
