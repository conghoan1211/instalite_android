package vn.edu.fpt.instalite.database;

import java.util.List;

import vn.edu.fpt.instalite.models.User;

public interface UserDAO {
    List<User> getAllUsers();
    User getUserById(String userId);
    boolean updateUser(User user);
}
