package vn.edu.fpt.instalite.services;

import android.content.Context;

import java.util.List;

import vn.edu.fpt.instalite.dto.NotificationDTO;
import vn.edu.fpt.instalite.repositories.AuthenRepository;
import vn.edu.fpt.instalite.repositories.NotificationRepository;

public class NotificationService {
    private NotificationRepository repo;
    public NotificationService(Context context) {
        repo = new NotificationRepository(context);
    }
    public List<NotificationDTO> getNotifications(String currentUserId) {
        if (currentUserId == null || currentUserId.isEmpty()) {
            throw new IllegalArgumentException("Input currentUserId is not valid!");
        }
        List<NotificationDTO> list = repo.getNotifications(currentUserId);
        return list;
    }
    public boolean insertNotification(String userId, String fromUserId, int relatedId, String content) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Input currentUserId is not valid!");
        }
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Input content is not valid!");
        }
        if (relatedId == 0) {
            throw new IllegalArgumentException("Input postId is not valid!");
        }
        boolean result = repo.insertNotification(userId, fromUserId, 0, relatedId, content);
        return result;
    }

    public String getUserIdByPostId(int postId) {
        if (postId == 0) {
            throw new IllegalArgumentException("Input postId is not valid!");
        }
        return repo.getUserIdByPostId(postId);
    }
}
