package vn.edu.fpt.instalite.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostDTO implements Serializable {
    private String id, userId, username, avatar, content, imageUrl;
    private int likes, comments, views, privacy;
    private long createdAt;

    public PostDTO(String id, String userId, String username, String avatar, String content, String imageUrl, int likes, int comments, int views, int privacy, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.avatar = avatar;
        this.content = content;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.comments = comments;
        this.views = views;
        this.privacy = privacy;
        this.createdAt = createdAt;
    }

    // Constructor và getter/setter giữ nguyên, chỉ thêm phương thức này
    // Improved method to get image URLs
    public List<String> getImageUrls() {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // Split the string and filter out any empty entries
        return Arrays.stream(imageUrl.split(","))
                .map(String::trim)
                .filter(url -> !url.isEmpty())
                .collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
