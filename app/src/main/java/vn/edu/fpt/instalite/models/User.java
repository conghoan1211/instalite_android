package vn.edu.fpt.instalite.models;

public class User {
    private int id;
    private String userId, username, email, password, bio, avatar;
    private int followersCount, followingCount, postsCount;
    private long createdAt;

    public User() {
    }

    public User(int id, String userId, String username, String email, String password, String bio, String avatar, int followersCount, int followingCount, int postsCount, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.avatar = avatar;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.postsCount = postsCount;
        this.createdAt = createdAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(int postsCount) {
        this.postsCount = postsCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", bio='" + bio + '\'' +
                ", avatar='" + avatar + '\'' +
                ", followersCount=" + followersCount +
                ", followingCount=" + followingCount +
                ", postsCount=" + postsCount +
                ", createdAt=" + createdAt +
                '}';
    }
}
