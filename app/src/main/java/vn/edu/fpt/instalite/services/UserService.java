package vn.edu.fpt.instalite.services;

import android.content.Context;

import java.util.List;

import vn.edu.fpt.instalite.dto.PostDTO;
import vn.edu.fpt.instalite.models.User;
import vn.edu.fpt.instalite.repositories.PostRepository;
import vn.edu.fpt.instalite.repositories.UserRepository;

public class UserService {
    private UserRepository repo;

    public UserService(Context context) {
        repo = new UserRepository(context);
    }

    public List<User> getAllUsers() {
        return repo.getAllUsers();
    }

    public User getUserById(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Input UserId is not valid!");
        }
        return repo.getUserById(userId);
    }

    public boolean updateUser(User user) {
        return repo.updateUser(user);
    }

}
