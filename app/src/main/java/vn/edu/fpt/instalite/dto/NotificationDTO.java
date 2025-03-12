package vn.edu.fpt.instalite.dto;

public class NotificationDTO {
    private int id;
    private String userId; // Người gửi thông báo
    private String fromUserId; // Người gửi thông báo
    private String username; // Tên người gửi
    private int relatedId;
    private String avatar; // Avatar người gửi
    private String content; // Nội dung thông báo
    private long createdAt; // Thời gian tạo
    private int type; // ID bài viết (nếu có)

    public NotificationDTO(int id, String userId, String fromUserId, String username, int relatedId, String avatar, String content, long createdAt, int type) {
        this.id = id;
        this.userId = userId;
        this.fromUserId = fromUserId;
        this.username = username;
        this.relatedId = relatedId;
        this.avatar = avatar;
        this.content = content;
        this.createdAt = createdAt;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(int relatedId) {
        this.relatedId = relatedId;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}