package vn.edu.fpt.instalite.database;

import java.util.List;

import vn.edu.fpt.instalite.dto.PostDTO;
import vn.edu.fpt.instalite.dto.UploadPostDTO;
import vn.edu.fpt.instalite.models.Post;

public interface PostDAO {
    boolean addPost(UploadPostDTO post);
    void updatePost(Post post);
    boolean deletePost(String id);
    PostDTO getPostById(String postId);
    List<PostDTO> getPostsByUserId(String postId);
    List<PostDTO> getAllPosts();
}
