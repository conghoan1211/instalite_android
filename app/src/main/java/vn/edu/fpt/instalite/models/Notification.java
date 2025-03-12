package vn.edu.fpt.instalite.models;

public class Notification {
    private String id;
    private String userId;
    private int type;
    private int relatedId;
    private String fromUserId;
    private int isRead;
    private long createdAt;

    public Notification(String id, String userId, int type, int relatedId, String fromUserId,
                        int isRead, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.relatedId = relatedId;
        this.fromUserId = fromUserId;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public int getType() { return type; }
    public int getRelatedId() { return relatedId; }
    public String getFromUserId() { return fromUserId; }
    public int getIsRead() { return isRead; }
    public long getCreatedAt() { return createdAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setType(int type) { this.type = type; }
    public void setRelatedId(int relatedId) { this.relatedId = relatedId; }
    public void setFromUserId(String fromUserId) { this.fromUserId = fromUserId; }
    public void setIsRead(int isRead) { this.isRead = isRead; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}