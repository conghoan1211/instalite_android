package vn.edu.fpt.instalite.services;

import android.content.Context;

import vn.edu.fpt.instalite.models.User;
import vn.edu.fpt.instalite.repositories.LikeCommentRepository;
import vn.edu.fpt.instalite.sessions.DataLocalManager;

public class LikeCommentService {
    private LikeCommentRepository repository;
    public LikeCommentService(Context context) {
        repository = new LikeCommentRepository(context);
    }

    public boolean toggleLikePost(String postId, String userId) {
        if (postId.isEmpty() || userId.isEmpty()) {
            throw new IllegalArgumentException("The input data is not valid.");
        }

        return repository.toggleLike(postId, userId);
    }

    public int getLikesPost (String postId) {
        if (postId.isEmpty() ) {
            throw new IllegalArgumentException("Cannot find the post!");
        }
        return repository.getLikesCount(postId);
    }
    public boolean isLikedPost (String postId, String userId) {
        if (postId == null || postId.isEmpty() ) {
            throw new IllegalArgumentException("Input userID is invalid!");
        }
        if (userId == null || userId.isEmpty() ) {
            throw new IllegalArgumentException("Input postId is invalid!");
        }
        return repository.isPostLiked(userId, postId);
    }
}
