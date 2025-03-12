package vn.edu.fpt.instalite.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UploadPostDTO {
    private String userId;
    private String content;
    private String imageUrl;
    private int likes;
    private int comments;
    private int views;
    private int privacy;
    private long createdAt;

    public UploadPostDTO(String userId, String content, String imageUrl, int likes, int comments, int views, int privacy, long createdAt) {
        this.userId = userId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.comments = comments;
        this.views = views;
        this.privacy = privacy;
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
