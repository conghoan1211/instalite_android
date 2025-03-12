package vn.edu.fpt.instalite.models;

public class Message {
    private String id;
    private String senderId;
    private String receiverId;
    private String content;
    private int isRead;
    private long createdAt;

    public Message(String id, String senderId, String receiverId, String content,
                   int isRead, long createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // Getters
    public String getId() { return id; }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public int getIsRead() { return isRead; }
    public long getCreatedAt() { return createdAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
    public void setContent(String content) { this.content = content; }
    public void setIsRead(int isRead) { this.isRead = isRead; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}