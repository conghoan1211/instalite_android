package vn.edu.fpt.instalite.models;

public class Comment {
    private String id;
    private int postId;
    private String userId;
    private String content;
    private long createdAt;

    public Comment(String id, int postId, String userId, String content, long createdAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getters và Setters
    public String getId() { return id; }
    public int getPostId() { return postId; }
    public String getUserId() { return userId; }
    public String getContent() { return content; }
    public long getCreatedAt() { return createdAt; }
    public void setId(String id) { this.id = id; }
    public void setPostId(int postId) { this.postId = postId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setContent(String content) { this.content = content; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}