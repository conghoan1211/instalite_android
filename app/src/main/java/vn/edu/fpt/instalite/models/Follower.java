package vn.edu.fpt.instalite.models;

public class Follower {
    private String id;
    private String followerId;
    private String followingId;
    private long createdAt;

    public Follower(String id, String followerId, String followingId, long createdAt) {
        this.id = id;
        this.followerId = followerId;
        this.followingId = followingId;
        this.createdAt = createdAt;
    }

    // Getters
    public String getId() { return id; }
    public String getFollowerId() { return followerId; }
    public String getFollowingId() { return followingId; }
    public long getCreatedAt() { return createdAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setFollowerId(String followerId) { this.followerId = followerId; }
    public void setFollowingId(String followingId) { this.followingId = followingId; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}