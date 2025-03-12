package vn.edu.fpt.instalite.database;

import vn.edu.fpt.instalite.models.Comment;

public interface LikeCommentDAO {
    boolean toggleLike(String postId, String userId);
    boolean commentPost(Comment input);
    int getLikesCount(String postId);
    boolean isPostLiked(String userId, String postId);
}
