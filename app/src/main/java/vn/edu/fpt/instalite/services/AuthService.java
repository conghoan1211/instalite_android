package vn.edu.fpt.instalite.services;

import android.content.Context;
import android.util.Patterns;

import vn.edu.fpt.instalite.dto.RegisterDTO;
import vn.edu.fpt.instalite.models.User;
import vn.edu.fpt.instalite.repositories.AuthenRepository;
import vn.edu.fpt.instalite.repositories.PostRepository;

public class AuthService {
    private AuthenRepository repo;
    public AuthService(Context context) {
        repo = new AuthenRepository(context);
    }

    public User login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Please input all the fields!");
        }
        User result = repo.Login(username, password);
        return result != null ? result : null;
    }

    public boolean register (RegisterDTO input)  {
        if(input.getUsername().isEmpty() || input.getEmail().isEmpty() || input.getPassword().isEmpty() || input.getRePassword().isEmpty()) {
            throw new IllegalArgumentException("Please input all the fields!");
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(input.getEmail()).matches()) {
            throw new IllegalArgumentException("Email is not correct format!");
        }
        if (!input.getPassword().equals(input.getRePassword())) {
            throw new IllegalArgumentException("Passwords do not match!");
        }
        String result = repo.Register(input);
        if (result.isEmpty()) return true;
        throw new IllegalArgumentException(result);
    }

}
