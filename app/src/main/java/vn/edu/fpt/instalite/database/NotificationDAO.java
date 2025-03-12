package vn.edu.fpt.instalite.database;

import java.util.List;

import vn.edu.fpt.instalite.dto.NotificationDTO;

public interface NotificationDAO {
    List<NotificationDTO> getNotifications(String currentUserId);
    String getUserIdByPostId(int postId);
      boolean insertNotification(String userId, String fromUserId, int type, int relatedId, String content) ;
}
