package vn.edu.fpt.instalite.models;

public class Like {
    private String id;
    private int postId;
    private String userId;
    private long createdAt;

    public Like(String id, int postId, String userId, long createdAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    // Getters
    public String getId() { return id; }
    public int getPostId() { return postId; }
    public String getUserId() { return userId; }
    public long getCreatedAt() { return createdAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setPostId(int postId) { this.postId = postId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}