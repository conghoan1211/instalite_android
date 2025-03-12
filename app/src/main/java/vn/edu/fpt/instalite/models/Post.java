package vn.edu.fpt.instalite.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Post {
    @NotNull(message = "ID cannot be null")
    private String id;

    @NotNull(message = "User ID cannot be null")
    private String userId;

    @Size(max = 500, message = "Content cannot exceed 500 characters")
    private String content;
    private String imageUrl; // Có thể null nếu không có ảnh

    @Min(value = 0, message = "Likes cannot be negative")
    private int likes;

    @Min(value = 0, message = "Comments cannot be negative")
    private int comments;

    @Min(value = 0, message = "Views cannot be negative")
    private int views;

    @Min(value = 0, message = "Privacy must be 0 or greater")
    private int privacy;

    @Min(value = 0, message = "Created time must be a valid timestamp")
    private long createdAt;

    public Post(String id, String userId, String content, String imageUrl, int likes, int comments, int views, int privacy, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.comments = comments;
        this.views = views;
        this.privacy = privacy;
        this.createdAt = createdAt;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    // Getters và Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
