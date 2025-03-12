package vn.edu.fpt.instalite.database;

import vn.edu.fpt.instalite.dto.RegisterDTO;
import vn.edu.fpt.instalite.models.User;

public interface AuthenDAO {
     User Login(String username, String password);
     String Register(RegisterDTO input);
}
